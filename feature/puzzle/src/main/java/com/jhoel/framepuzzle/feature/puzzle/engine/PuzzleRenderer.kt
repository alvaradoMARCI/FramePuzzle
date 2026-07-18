package com.jhoel.framepuzzle.feature.puzzle.engine

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.graphics.RectF
import android.graphics.Shader
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color as ComposeColor
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Dp
import com.jhoel.framepuzzle.feature.puzzle.domain.PuzzleBoard
import com.jhoel.framepuzzle.feature.puzzle.domain.PuzzlePiece
import kotlin.math.sqrt

/**
 * Utilidades de renderizado del puzzle.
 *
 * Renderiza piezas con forma real de puzzle (tabs = salientes, blanks = entrantes)
 * para que el tablero se sienta como un puzzle físico y no como una cuadrícula
 * de tarjetas (sección 9 y 18 del Master Document).
 *
 * Las formas se calculan por pieza según sus vecinos: si la pieza de al lado
 * tiene un tab, esta necesita un blank, y viceversa. Esto crea piezas que
 * encajan entre sí.
 */
object PuzzleRenderer {

    /**
     * Tamaño relativo del tab/blank respecto al lado de la pieza.
     */
    private const val TAB_RATIO = 0.20f

    /**
     * Construye un Path con la forma de una pieza de puzzle dadas las conexiones
     * con sus 4 vecinos (arriba, derecha, abajo, izquierda).
     *
     * @param x origen X de la pieza.
     * @param y origen Y de la pieza.
     * @param w ancho de la pieza.
     * @param h alto de la pieza.
     * @param top conexión arriba: 0 = plana, 1 = tab saliente, -1 = blank entrante.
     * @param right conexión derecha.
     * @param bottom conexión abajo.
     * @param left conexión izquierda.
     */
    fun buildPiecePath(
        x: Float, y: Float, w: Float, h: Float,
        top: Int, right: Int, bottom: Int, left: Int,
    ): Path {
        val path = Path()
        val tab = minOf(w, h) * TAB_RATIO

        // Esquina superior izquierda
        path.moveTo(x, y)

        // Borde superior (de izquierda a derecha)
        if (top == 0) {
            path.lineTo(x + w, y)
        } else {
            val mid = x + w / 2
            val dir = -top // si top=1 (tab saliente), va hacia arriba (negativo)
            path.lineTo(mid - tab, y)
            path.cubicTo(
                mid - tab, y + dir * tab * 0.5f,
                mid - tab * 0.5f, y + dir * tab,
                mid, y + dir * tab,
            )
            path.cubicTo(
                mid + tab * 0.5f, y + dir * tab,
                mid + tab, y + dir * tab * 0.5f,
                mid + tab, y,
            )
            path.lineTo(x + w, y)
        }

        // Borde derecho (de arriba a abajo)
        if (right == 0) {
            path.lineTo(x + w, y + h)
        } else {
            val mid = y + h / 2
            val dir = right // si right=1, va hacia la derecha (positivo)
            path.lineTo(x + w, mid - tab)
            path.cubicTo(
                x + w + dir * tab * 0.5f, mid - tab,
                x + w + dir * tab, mid - tab * 0.5f,
                x + w + dir * tab, mid,
            )
            path.cubicTo(
                x + w + dir * tab, mid + tab * 0.5f,
                x + w + dir * tab * 0.5f, mid + tab,
                x + w, mid + tab,
            )
            path.lineTo(x + w, y + h)
        }

        // Borde inferior (de derecha a izquierda)
        if (bottom == 0) {
            path.lineTo(x, y + h)
        } else {
            val mid = x + w / 2
            val dir = bottom // si bottom=1, va hacia abajo (positivo)
            path.lineTo(mid + tab, y + h)
            path.cubicTo(
                mid + tab, y + h + dir * tab * 0.5f,
                mid + tab * 0.5f, y + h + dir * tab,
                mid, y + h + dir * tab,
            )
            path.cubicTo(
                mid - tab * 0.5f, y + h + dir * tab,
                mid - tab, y + h + dir * tab * 0.5f,
                mid - tab, y + h,
            )
            path.lineTo(x, y + h)
        }

        // Borde izquierdo (de abajo a arriba)
        if (left == 0) {
            path.lineTo(x, y)
        } else {
            val mid = y + h / 2
            val dir = -left // si left=1 (tab saliente), va hacia la izquierda (negativo)
            path.lineTo(x, mid + tab)
            path.cubicTo(
                x + dir * tab * 0.5f, mid + tab,
                x + dir * tab, mid + tab * 0.5f,
                x + dir * tab, mid,
            )
            path.cubicTo(
                x + dir * tab, mid - tab * 0.5f,
                x + dir * tab * 0.5f, mid - tab,
                x, mid - tab,
            )
            path.lineTo(x, y)
        }

        path.close()
        return path
    }

    /**
     * Renderiza el tablero completo en un bitmap, con piezas en sus posiciones
     * actuales y forma real con tabs/blanks.
     *
     * @param board tablero con atlas y piezas.
     * @param gapPx separación visual entre piezas (2-4px recomendado).
     * @param cornerRadius radio de esquinas (para look moderno).
     * @return bitmap renderizado.
     */
    fun renderBoard(
        board: PuzzleBoard,
        gapPx: Int = 3,
        cornerRadius: Float = 8f,
    ): Bitmap {
        val source = board.sourceBitmap ?: return Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888)
        val grid = board.gridSize
        val pieceW = board.pieceWidth.toFloat()
        val pieceH = board.pieceHeight.toFloat()

        // Tamaño total con gaps
        val totalW = (pieceW * grid + gapPx * (grid + 1)).toInt()
        val totalH = (pieceH * grid + gapPx * (grid + 1)).toInt()

        val out = Bitmap.createBitmap(totalW, totalH, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(out)
        canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR)

        val piecePaint = Paint(Paint.ANTI_ALIAS_FLAG or Paint.FILTER_BITMAP_FLAG)
        val borderPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            style = Paint.Style.STROKE
            strokeWidth = 1f
            color = Color.argb(40, 0, 0, 0)
        }
        val shadowPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.argb(60, 0, 0, 0)
        }

        // Generar conexiones entre piezas (determinísticas: piece.id par/impar).
        // top/right/bottom/left: 0 = plana, 1 = tab, -1 = blank.
        fun connection(pieceId: Int, neighborId: Int?): Int {
            if (neighborId == null) return 0 // borde del tablero
            // Una pieza tiene tab, la otra tiene blank: usar paridad del id menor.
            val min = minOf(pieceId, neighborId)
            return if (min % 2 == 0) 1 else -1
        }

        // Para cada slot del tablero, buscar qué pieza está ahí y dibujarla.
        for (slot in 0 until grid * grid) {
            val piece = board.pieceAtSlot[slot] ?: continue
            if (piece.id == board.pieces.size - 1 && board.emptySlotIndex >= 0) {
                // Slot vacío en sliding puzzle: dejar hueco.
                continue
            }
            val row = slot / grid
            val col = slot % grid

            // Posición destino con gap.
            val dstX = gapPx + col * (pieceW + gapPx)
            val dstY = gapPx + row * (pieceH + gapPx)

            // Posición origen en el atlas (targetIndex).
            val srcRow = piece.targetIndex / grid
            val srcCol = piece.targetIndex % grid
            val srcX = srcCol * pieceW
            val srcY = srcRow * pieceH

            // Conexiones: comparar piece.id con piezas vecinas EN TARGET INDEX.
            // (Las piezas encajan según su posición correcta, no la actual.)
            val topNeighbor = neighborByTarget(board, piece.targetIndex, 0, -1, grid)
            val rightNeighbor = neighborByTarget(board, piece.targetIndex, 1, 0, grid)
            val bottomNeighbor = neighborByTarget(board, piece.targetIndex, 0, 1, grid)
            val leftNeighbor = neighborByTarget(board, piece.targetIndex, -1, 0, grid)

            val topConn = connection(piece.id, topNeighbor?.id)
            val rightConn = connection(piece.id, rightNeighbor?.id)
            val bottomConn = connection(piece.id, bottomNeighbor?.id)
            val leftConn = connection(piece.id, leftNeighbor?.id)

            val path = buildPiecePath(
                x = dstX, y = dstY, w = pieceW, h = pieceH,
                top = topConn, right = rightConn, bottom = bottomConn, left = leftConn,
            )

            // Sombra sutil debajo de la pieza (profundidad).
            canvas.save()
            canvas.translate(2f, 3f)
            canvas.drawPath(path, shadowPaint)
            canvas.restore()

            // Clip al path y dibujar la región del atlas.
            canvas.save()
            canvas.clipPath(path)
            val srcRect = android.graphics.Rect(
                srcX.toInt(), srcY.toInt(),
                (srcX + pieceW).toInt(), (srcY + pieceH).toInt(),
            )
            val dstRect = android.graphics.Rect(
                dstX.toInt(), dstY.toInt(),
                (dstX + pieceW).toInt(), (dstY + pieceH).toInt(),
            )
            canvas.drawBitmap(source, srcRect, dstRect, piecePaint)
            canvas.restore()

            // Borde sutil.
            canvas.drawPath(path, borderPaint)
        }
        return out
    }

    /**
     * Encuentra la pieza vecina a una posición target dada (targetRow, targetCol)
     * sumando (dCol, dRow). Devuelve null si está fuera del tablero.
     */
    private fun neighborByTarget(
        board: PuzzleBoard,
        targetIndex: Int,
        dCol: Int,
        dRow: Int,
        gridSize: Int,
    ): PuzzlePiece? {
        val row = targetIndex / gridSize
        val col = targetIndex % gridSize
        val newRow = row + dRow
        val newCol = col + dCol
        if (newRow < 0 || newRow >= gridSize || newCol < 0 || newCol >= gridSize) return null
        val newTarget = newRow * gridSize + newCol
        return board.pieces.firstOrNull { it.targetIndex == newTarget }
    }
}
