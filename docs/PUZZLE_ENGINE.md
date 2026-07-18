# FramePuzzle — Puzzle Engine

> *"El puzzle es la experiencia principal de FramePuzzle."*

## 1. Principios (sección 14)

- **Motor propio en Kotlin**. No depende de motores externos.
- Maneja: división de imagen, creación de piezas, mezcla aleatoria, validación de movimientos, estado del tablero y detección de victoria.

Implementación: `feature/puzzle/engine/PuzzleEngine.kt`.

## 2. Tipos de puzzle (sección 15)

### Classic

La imagen se divide en piezas. El usuario debe reconstruirla intercambiando posiciones.

Mecánica: tap-tap-tap (selecciona pieza A, luego pieza B, se intercambian).

### Sliding

Las piezas están dentro de un tablero con una ranura vacía. El usuario mueve las piezas adyacentes a la ranura hasta completar la imagen.

Mecánica: tap en una pieza adyacente a la ranura vacía.

El mezclado del sliding respeta la **solvencia**: solo se generan configuraciones alcanzables mediante movimientos válidos desde el estado resuelto.

## 3. Generación de piezas (sección 16)

```
sourceBitmap
   ↓ (división en gridSize × gridSize)
piece(0,0)  piece(0,1)  ...  piece(0,N)
piece(1,0)  piece(1,1)  ...
...
```

Cada pieza se guarda como PNG individual en `FramePuzzle/puzzles/piece_<ts>_<index>.png` para evitar mantener en memoria el bitmap completo durante el juego (importante en gama baja).

## 4. Sistema de dificultad (sección 17)

| Nivel | Piezas | Ayuda visual | Hints |
|-------|--------|--------------|-------|
| Fácil | 9 | preview activa | sí |
| Normal | 16 | preview activa | sí |
| Difícil | 36 | preview oculta | no |
| Personalizado | configurable | configurable | configurable |

## 5. Detección de victoria

```kotlin
val isSolved: Boolean
    get() = pieces.all { it.isCorrect }
```

Donde `isCorrect = (currentIndex == targetIndex)`.

## 6. Experiencia al completar (sección 18)

- **Animación fluida**: piezas se unen formando la imagen completa.
- **Pequeñas separaciones visuales** para conservar la identidad de puzzle (`composeFinalImage(gapPx = 2)`).
- **Vibración háptica opcional** (según `AppSettings.hapticsEnabled`).
- **Sonido opcional** (según `AppSettings.soundEnabled`).
- **Celebración del logro** con iconografía dorada y overlay de estadísticas.

## 7. Estadísticas

Cada puzzle resuelto registra:

- `moves`: cantidad de movimientos.
- `timeMillis`: duración total.
- `perfect`: true si `moves <= pieces.size` (sin movimientos extra).

## 8. Limpieza

`PuzzleEngine.cleanup(board)` elimina los PNG temporales de piezas cuando el usuario abandona la pantalla o inicia otro puzzle. Esto evita acumulación de archivos basura en `FramePuzzle/puzzles/`.

## 9. XP otorgada (sección 19)

| Evento | XP |
|--------|----|
| `PUZZLE_SOLVED_EASY` | 15 |
| `PUZZLE_SOLVED_NORMAL` | 30 |
| `PUZZLE_SOLVED_HARD` | 60 |
| `PUZZLE_SOLVED_PERFECT` | +50 (bonus) |
