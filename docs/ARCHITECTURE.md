# FramePuzzle — Architecture

## 1. Arquitectura principal

**Clean Architecture + MVVM**

```
Presentation  ↓  Domain  ↓  Data
```

La arquitectura permite modificar partes del sistema sin afectar todo el proyecto.

## 2. Capas

### Presentation

Gestiona la interfaz y la comunicación con el usuario.

- Pantallas (Jetpack Compose).
- Componentes Compose.
- Estados de interfaz (`*UiState`).
- ViewModels.
- Navegación.
- Eventos del usuario.

Estructura típica:

```
feature_puzzle/
├── ui/
│   ├── PuzzleScreen.kt
│   └── PuzzleViewModel.kt
├── domain/
└── data/
```

### Domain

Contiene la lógica principal del producto. **No depende directamente de Android ni de elementos visuales.**

- Casos de uso (`*UseCase`).
- Reglas de negocio.
- Modelos principales.
- Validaciones.

Casos de uso identificados:

- `CreateMemoryUseCase`
- `GeneratePuzzleUseCase`
- `SolvePuzzleUseCase`
- `TransferMemoryUseCase`
- `RestoreBackupUseCase`
- `UnlockAchievementUseCase`

### Data

Gestiona almacenamiento y fuentes de información.

- Room Database.
- DataStore.
- Archivos locales.
- Repositorios.
- Fuentes de datos.

Repositorios:

- `MemoryRepository`
- `PuzzleRepository`
- `UserRepository`
- `BackupRepository`

## 3. Módulos

### Core

| Módulo | Responsabilidad |
|--------|-----------------|
| `core:database` | Room DB, entidades, DAOs, converters, DI |
| `core:storage` | Almacenamiento interno local (FramePuzzle/) |
| `core:security` | CryptoManager (AES-256/GCM), PinManager, BiometricManagerHelper |
| `core:designsystem` | Tema, colores, tipografía, componentes reutilizables |
| `core:utils` | TimeUtils, ImageUtils, FramePuzzleResult, logger |

### Feature

| Módulo | Responsabilidad |
|--------|-----------------|
| `feature:camera` | Captura (CameraX), galería, importación, creación de recuerdos |
| `feature:editor` | Edición no destructiva, filtros propios, ajustes, marcos |
| `feature:puzzle` | Motor puzzle propio, tablero, piezas, animaciones |
| `feature:library` | Biblioteca, álbumes, búsqueda, favoritos |
| `feature:profile` | Usuario, avatar, XP, niveles, logros, seguridad |
| `feature:backup` | Respaldo cifrado `.fpbackup`, restauración |
| `feature:transfer` | Transferencia entre teléfonos mediante QR |
| `feature:settings` | Configuración general (DataStore) |

## 4. Inyección de dependencias

Hilt. Cada módulo expone sus bindings vía `@Module @InstallIn(SingletonComponent::class)`.

## 5. Estructura del repositorio

```
FramePuzzle/
├── app/                       # MainActivity, navegación, Hilt
├── core/
│   ├── database/
│   ├── storage/
│   ├── security/
│   ├── designsystem/
│   └── utils/
├── feature/
│   ├── camera/
│   ├── editor/
│   ├── puzzle/
│   ├── library/
│   ├── profile/
│   ├── backup/
│   ├── transfer/
│   └── settings/
├── docs/
├── README.md
├── CHANGELOG.md
└── FramePuzzle_Master_Document.md
```

## 6. Principios

- Código limpio.
- Separación de responsabilidades.
- Bajo acoplamiento.
- Alta mantenibilidad.
- Escalabilidad.
- Seguridad desde el inicio.
- Fácil incorporación de nuevas funciones.
