# FramePuzzle — Database

Tecnología: **Room Database**.

La base de datos almacena información estructurada del usuario y sus recuerdos. Las **imágenes** se guardan como archivos (ver `core:storage`); la DB solo guarda **rutas relativas**.

## 1. Entidades

### User

```kotlin
@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey val id: String,
    val name: String,
    val avatar: String?,
    val level: Int,
    val xp: Int,
    val createdDate: Long,
)
```

FramePuzzle es **local-first**: el usuario vive en el dispositivo. No hay backend ni cuentas remotas.

### Memory

```kotlin
@Entity(
    tableName = "memories",
    foreignKeys = [ForeignKey(...AlbumEntity, onDelete = SET_NULL)],
)
data class MemoryEntity(
    @PrimaryKey val id: String,
    val title: String,
    val originalImage: String,   // ruta relativa
    val editedImage: String?,    // ruta relativa (null si no se ha editado)
    val createdDate: Long,
    val albumId: String?,
    val progress: Float,
    val favorite: Boolean,
)
```

**Regla de FramePuzzle (sección 9):** *"El recuerdo original nunca debe modificarse."*

Por eso hay dos columnas: `originalImage` y `editedImage`. El flujo es:

```
Original → Ediciones → Puzzle → Experiencia final
```

### Puzzle

```kotlin
@Entity(tableName = "puzzles", foreignKeys = [ForeignKey(...MemoryEntity, onDelete = CASCADE)])
data class PuzzleEntity(
    @PrimaryKey val id: String,
    val memoryId: String,
    val type: PuzzleType,           // CLASSIC | SLIDING
    val difficulty: PuzzleDifficulty, // EASY | NORMAL | HARD | CUSTOM
    val pieces: Int,
    val completed: Boolean,
    val timeMillis: Long,
    val moves: Int,
    val createdDate: Long,
)
```

Un recuerdo puede generar varios puzzles (diferentes tipos o dificultades).

### Album

```kotlin
@Entity(tableName = "albums")
data class AlbumEntity(
    @PrimaryKey val id: String,
    val name: String,
    val cover: String?,
    val type: AlbumType,    // AUTOMATIC | MANUAL
    val createdDate: Long,
)
```

- **Automáticos**: por fecha, eventos, categorías.
- **Manuales**: familia, amigos, viajes, etc.

### Achievement

```kotlin
@Entity(tableName = "achievements")
data class AchievementEntity(
    @PrimaryKey val id: String,
    val name: String,
    val description: String,
    val unlocked: Boolean,
    val unlockedDate: Long?,
    val xpReward: Int,
)
```

## 2. DAOs

- `UserDao`
- `MemoryDao`
- `PuzzleDao`
- `AlbumDao`
- `AchievementDao`

Todos los DAOs exponen `Flow<T>` para observación reactiva desde ViewModels.

## 3. Converters

`FramePuzzleConverters` convierte los enums (`PuzzleType`, `PuzzleDifficulty`, `AlbumType`) a `String` y viceversa. Se persisten como String para que los backups sean legibles.

## 4. Migraciones

Política actual: `fallbackToDestructiveMigration(onDowngrade = true)`.

En producción se deben proveer `Migration` objects específicos antes de cada release.

## 5. Versionado de esquema

`exportSchema = true`. Los esquemas JSON se generan en `core/database/schemas/` para auditoría.
