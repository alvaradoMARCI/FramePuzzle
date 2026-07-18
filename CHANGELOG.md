# Changelog

Todos los cambios notables de FramePuzzle se documentan en este archivo.

El formato está basado en [Keep a Changelog](https://keepachangelog.com/es-ES/1.1.0/),
y este proyecto se adhiere a [Semantic Versioning](https://semver.org/lang/es/spec/v2.0.0.html).

---

## [0.1.0-alpha] — 2026-07-18

Versión Alpha inicial. Estructura completa del proyecto e implementación de las 8 fases del FramePuzzle_Master_Document.

### Added

#### Fase 0 — Preparación del proyecto
- Repositorio GitHub `FramePuzzle` creado.
- Configuración Gradle multi-módulo (settings, build, version catalog).
- Manifiesto Android con permisos (cámara, galería, biometría, vibración, internet).
- Estructura modular: `app`, `core/{database,storage,security,designsystem,utils}`, `feature/{camera,editor,puzzle,library,profile,backup,transfer,settings}`.
- Tema FramePuzzle (paleta dorada, modo oscuro por defecto).
- Documentación base (`docs/`).

#### Fase 1 — Núcleo de FramePuzzle
- `FramePuzzleApp` con `@HiltAndroidApp`.
- `MainActivity` single-activity con edge-to-edge.
- `FramePuzzleNavHost` con navegación bottom bar (Inicio, Biblioteca, Crear, Perfil).
- Design system completo: `FramePuzzleColors`, `FramePuzzleTheme`, `FramePuzzleTypography`, `FramePuzzleShapes`, `FramePuzzleLoading`, `FramePuzzleXpBar`, `FramePuzzleEmptyState`, `FramePuzzleLogoBadge`.
- `LocalStorageManager` con estructura `FramePuzzle/{memories,original,edited,puzzles,backup}`.
- `UserRepository`, `User` (con XP y levelUp).

#### Fase 2 — Sistema de creación de recuerdos
- `CameraXHelper` con CameraX (preview, captura, lensFacing frontal/trasera).
- `CameraScreen` con permisos en tiempo de ejecución, selección desde galería (`GetContent`), preview fluida.
- `CreateMemoryFromImageUseCase` (crea copia interna, no toca el original).
- `ImportImageUseCase`.

#### Fase 3 — Editor de imágenes
- `ImageProcessor` con filtros propios FramePuzzle (Vintage, Nostalgia, Cinemático, B&N, Recuerdo antiguo) vía ColorMatrix.
- Ajustes no destructivos: brillo, contraste, saturación, temperatura, exposición, rotación, recorte.
- `EditorScreen` con sliders, chips de filtro y preview en vivo.
- La imagen original **nunca** se modifica.

#### Fase 4 — Motor puzzle
- `PuzzleEngine` propio en Kotlin (sin dependencias externas).
- División de imagen, creación de piezas, mezcla aleatoria (clásica + sliding con solvencia garantizada).
- Validación de movimientos (swap clásico / slide con vecinos).
- Detección de victoria.
- `composeFinalImage` con gap visual para conservar identidad de puzzle.
- `PuzzleScreen` con grid, selector de dificultad y animación de celebración dorada.
- 4 niveles: Fácil (9 piezas), Normal (16), Difícil (36), Personalizado.

#### Fase 5 — Biblioteca, perfil y progreso
- `LibraryScreen` con tabs (Recuerdos, Álbumes, Historial, Favoritos), buscador y grid.
- `ProfileScreen` con avatar, nivel, XP, estadísticas, logros y accesos a seguridad/backup/transfer/settings.
- `AchievementRepository` y `AchievementEntity`.
- `XpEvent` (10 eventos con recompensas diferentes).
- `Avatar`, `AvatarFrame` (5 niveles: NONE, GOLD, EMERALD, AMETHYST, LEGEND) y `AvatarBadge`.

#### Fase 6 — Privacidad y seguridad
- `CryptoManager` AES-256/GCM con Android Keystore.
- `PinManager` con hash SHA-256 + salt dentro de EncryptedSharedPreferences.
- `BiometricManagerHelper` con fallback a PIN.
- `SecurityScreen` con información de protección de acceso, datos y acciones sensibles.
- `PinLockScreen` con numpad y dots de progreso.
- `dataExtractionRules.xml` desactiva cloud-backup.

#### Fase 7 — Transferencia y respaldo
- `BackupManager` con formato `.fpbackup` (ZIP cifrado, manifest con SHA-256).
- `BackupScreen` con crear/restaurar.
- `TransferManager` con token cifrado (`FPT1.<iv>.<ct>`), QR rendering con ZXing.
- `TransferScreen` con generación y escaneo de QR.

#### Fase 8 — Optimización y documentación
- Documentación completa en `docs/` (9 archivos).
- `CHANGELOG.md`.
- `FramePuzzle_Master_Document.md` incluido como referencia.
- Tokens de diseño (`FramePuzzleSpacing`, `FramePuzzleSizes`, `FramePuzzleDurations`).
- `FramePuzzleResult<T>` + `Failure` para reemplazar excepciones en dominio.

### Seguridad
- ✅ Sin secretos en el código.
- ✅ Datos protegidos (cifrado local AES-256/GCM).
- ✅ Respaldos cifrados con integridad SHA-256.
- ✅ Transferencias con OTP y cifrado.
- ✅ PIN nunca en texto plano.

### Known Issues / Limitaciones (Alpha)
- La compilación real requiere Android Studio + Android SDK (no compilable en este entorno).
- El picker de archivo `.fpbackup` para restaurar usa SAF (pendiente de conectar).
- El escaneo de QR abre la cámara pero no completa el handshake P2P (siguiente iteración).
- Los álbumes automáticos por fecha/eventos están modelados pero no se generan automáticamente todavía.
- Las pruebas unitarias están pendientes (esqueleto en `app/src/test/`).

---

## Próximos hitos

- [ ] `0.2.0-alpha` — Pruebas unitarias para PuzzleEngine, ImageProcessor, CryptoManager, PinManager.
- [ ] `0.3.0-alpha` — Conexión completa de transferencia P2P tras QR.
- [ ] `0.4.0-alpha` — Álbumes automáticos (fecha, evento, categoría).
- [ ] `0.5.0-alpha` — Línea temporal cronológica.
- [ ] `0.9.0-beta` — Beta cerrada con usuarios invitados.
- [ ] `1.0.0` — Release público.

---

[0.1.0-alpha]: https://github.com/alvaradoMARCI/FramePuzzle/releases/tag/v0.1.0-alpha
