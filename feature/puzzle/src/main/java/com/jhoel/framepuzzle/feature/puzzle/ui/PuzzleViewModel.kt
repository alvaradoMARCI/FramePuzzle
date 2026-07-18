package com.jhoel.framepuzzle.feature.puzzle.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jhoel.framepuzzle.core.database.entity.PuzzleDifficulty as DbDifficulty
import com.jhoel.framepuzzle.core.database.entity.PuzzleEntity
import com.jhoel.framepuzzle.core.database.entity.PuzzleType as DbType
import com.jhoel.framepuzzle.feature.library.data.MemoryRepository
import com.jhoel.framepuzzle.feature.puzzle.PuzzleUiState
import com.jhoel.framepuzzle.feature.puzzle.data.PuzzleRepository
import com.jhoel.framepuzzle.feature.puzzle.domain.PuzzleBoard
import com.jhoel.framepuzzle.feature.puzzle.domain.PuzzleConfig
import com.jhoel.framepuzzle.feature.puzzle.domain.PuzzleDifficulty
import com.jhoel.framepuzzle.feature.puzzle.domain.PuzzleType
import com.jhoel.framepuzzle.feature.puzzle.engine.PuzzleEngine
import com.jhoel.framepuzzle.feature.library.data.UserRepository
import com.jhoel.framepuzzle.feature.library.domain.XpEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * ViewModel de la pantalla Puzzle (sección 14-18).
 *
 * CORRECCIÓN ANR v0.3.0:
 *  - Todas las llamadas a PuzzleEngine (createBoard, slide, swap) se ejecutan
 *    en Dispatchers.Default (CPU) o Dispatchers.IO (DB/decoding).
 *  - El Main Thread solo toca StateFlow y Compose.
 *
 * Maneja:
 *  - Creación del tablero (motor).
 *  - Movimientos (clásico: swap / deslizante: slide).
 *  - Detección de victoria.
 *  - Estadísticas + XP.
 */
@HiltViewModel
class PuzzleViewModel @Inject constructor(
    private val memoryRepository: MemoryRepository,
    private val puzzleRepository: PuzzleRepository,
    private val userRepository: UserRepository,
    private val engine: PuzzleEngine,
) : ViewModel() {

    private val _uiState = MutableStateFlow(PuzzleUiState())
    val uiState: StateFlow<PuzzleUiState> = _uiState.asStateFlow()

    private var timerJob: Job? = null
    private var puzzleEntityId: String? = null

    fun load(memoryId: String) {
        // Limpieza previa si hay un tablero anterior (libera bitmap atlas).
        _uiState.value.board?.let { engine.release(it) }

        viewModelScope.launch(Dispatchers.IO) {
            val memory = memoryRepository.getById(memoryId) ?: run {
                _uiState.update { it.copy(isLoading = false, error = "Recuerdo no encontrado") }
                return@launch
            }
            val imagePath = memory.editedImagePath ?: memory.originalImagePath
            val config = PuzzleConfig(PuzzleType.SLIDING, PuzzleDifficulty.NORMAL)

            // createBoard hace decodificación JPEG + división: Dispatchers.Default.
            val board = withContext(Dispatchers.Default) {
                engine.createBoard(imagePath, config)
            }

            val entity = PuzzleEntity(
                id = java.util.UUID.randomUUID().toString(),
                memoryId = memoryId,
                type = DbType.SLIDING,
                difficulty = DbDifficulty.NORMAL,
                pieces = board.pieces.size,
                completed = false,
                time = 0L,
                moves = 0,
                createdDate = System.currentTimeMillis(),
            )
            puzzleRepository.create(entity)
            puzzleEntityId = entity.id

            _uiState.update {
                it.copy(
                    isLoading = false,
                    memoryId = memoryId,
                    imagePath = imagePath,
                    title = memory.title,
                    config = config,
                    board = board,
                )
            }
            startTimer()
        }
    }

    fun setDifficulty(difficulty: PuzzleDifficulty) {
        val current = _uiState.value
        val imagePath = current.imagePath ?: return
        val newConfig = PuzzleConfig(current.config.type, difficulty)
        viewModelScope.launch(Dispatchers.Default) {
            current.board?.let { engine.release(it) }
            val board = engine.createBoard(imagePath, newConfig)
            _uiState.update {
                it.copy(
                    config = newConfig,
                    board = board,
                    moves = 0,
                    elapsedMillis = 0L,
                    isCompleted = false,
                )
            }
            restartTimer()
        }
    }

    fun setType(type: PuzzleType) {
        val current = _uiState.value
        val imagePath = current.imagePath ?: return
        val newConfig = PuzzleConfig(type, current.config.difficulty)
        viewModelScope.launch(Dispatchers.Default) {
            current.board?.let { engine.release(it) }
            val board = engine.createBoard(imagePath, newConfig)
            _uiState.update {
                it.copy(
                    config = newConfig,
                    board = board,
                    moves = 0,
                    elapsedMillis = 0L,
                    isCompleted = false,
                )
            }
            restartTimer()
        }
    }

    /**
     * Puzzle clásico: intercambia dos piezas.
     */
    fun swapPieces(fromIndex: Int, toIndex: Int) {
        val board = _uiState.value.board ?: return
        // swap es operación ligera (solo list copy), OK en Main.
        val newBoard = engine.swap(board, fromIndex, toIndex)
        applyMove(newBoard)
    }

    /**
     * Puzzle deslizante: mueve pieza hacia ranura vacía.
     */
    fun slidePiece(pieceCurrentIndex: Int) {
        val board = _uiState.value.board ?: return
        val newBoard = engine.slide(board, pieceCurrentIndex)
        if (newBoard === board) return // movimiento inválido, sin cambio.
        applyMove(newBoard)
    }

    private fun applyMove(newBoard: PuzzleBoard) {
        val newMoves = _uiState.value.moves + 1
        val solved = engine.isSolved(newBoard)
        _uiState.update {
            it.copy(board = newBoard, moves = newMoves, isCompleted = solved)
        }
        if (solved) {
            onPuzzleSolved()
        }
    }

    private fun onPuzzleSolved() {
        timerJob?.cancel()
        val state = _uiState.value
        val stats = engine.computeStats(
            moves = state.moves,
            timeMillis = state.elapsedMillis,
            perfect = state.moves <= (state.board?.pieces?.size ?: 0),
        )
        puzzleEntityId?.let { id ->
            viewModelScope.launch(Dispatchers.IO) {
                puzzleRepository.markCompleted(id, stats)
            }
        }

        viewModelScope.launch(Dispatchers.IO) {
            val user = userRepository.getCurrent() ?: return@launch
            val event = when (state.config.difficulty) {
                PuzzleDifficulty.EASY -> XpEvent.PUZZLE_SOLVED_EASY
                PuzzleDifficulty.NORMAL -> XpEvent.PUZZLE_SOLVED_NORMAL
                PuzzleDifficulty.HARD -> XpEvent.PUZZLE_SOLVED_HARD
                PuzzleDifficulty.CUSTOM -> XpEvent.PUZZLE_SOLVED_NORMAL
            }
            userRepository.addXp(event, user)
            if (stats.perfect) userRepository.addXp(XpEvent.PUZZLE_SOLVED_PERFECT, user)
        }

        _uiState.update { it.copy(showCompletionAnimation = true) }
    }

    fun consumeCompletion() {
        _uiState.update { it.copy(showCompletionAnimation = false) }
    }

    private fun startTimer() {
        timerJob?.cancel()
        timerJob = viewModelScope.launch {
            while (true) {
                delay(1000)
                _uiState.update { it.copy(elapsedMillis = it.elapsedMillis + 1000) }
            }
        }
    }

    private fun restartTimer() {
        timerJob?.cancel()
        _uiState.update { it.copy(elapsedMillis = 0L) }
        startTimer()
    }

    override fun onCleared() {
        timerJob?.cancel()
        // Liberar bitmap atlas para evitar memory leak.
        _uiState.value.board?.let { engine.release(it) }
        super.onCleared()
    }
}
