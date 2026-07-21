# FramePuzzle — Decisiones técnicas

Toda decisión importante se documenta en este archivo con el formato (sección 54):

```
Decisión:
Motivo:
Alternativas evaluadas:
Impacto:
Fecha:
Responsable:
```

---

## D001 — Clean Architecture + MVVM

- **Decisión:** Usar Clean Architecture + MVVM con módulos Gradle separados.
- **Motivo:** El documento maestro lo especifica (sección 28). Permite bajo acoplamiento, alta mantenibilidad y escalabilidad.
- **Alternativas evaluadas:** MVI (más verboso), MVP (legacy),单一 module (más simple pero no escala).
- **Impacto:** Estructura del proyecto, curva de aprendizaje onboarding, velocidad de build (módulos paralelizables).
- **Fecha:** 2026-07-18.
- **Responsable:** Jhoel + IA.

## D002 — Jetpack Compose

- **Decisión:** UI 100% Jetpack Compose (sin XML).
- **Motivo:** Especificado en sección 5. Permite animaciones fluidas y preview en Android Studio.
- **Alternativas evaluadas:** XML + ViewBinding (más lento para iterar).
- **Impacto:** minSdk 28 (Compose requiere API 21+, pero FramePuzzle usa 28).
- **Fecha:** 2026-07-18.
- **Responsable:** Jhoel + IA.

## D003 — Hilt para DI

- **Decisión:** Hilt como framework de inyección de dependencias.
- **Motivo:** Integración oficial con Android, ViewModels, WorkManager.
- **Alternativas evaluadas:** Koin (más simple pero no compila-tiempo), Dagger puro (verboso).
- **Impacto:** Anotaciones `@HiltAndroidApp`, `@AndroidEntryPoint`, `@HiltViewModel` en todo el proyecto.
- **Fecha:** 2026-07-18.
- **Responsable:** Jhoel + IA.

## D004 — Room con enums como String

- **Decisión:** Persistir enums (`PuzzleType`, `PuzzleDifficulty`, `AlbumType`) como `String` vía TypeConverters.
- **Motivo:** Facilita inspección manual de la DB y legibilidad de los archivos de respaldo.
- **Alternativas evaluadas:** Persistir como `Int` ordinal (más compacto pero ilegible; frágil ante reordenamientos).
- **Impacto:** `FramePuzzleConverters` en `core:database`.
- **Fecha:** 2026-07-18.
- **Responsable:** Jhoel + IA.

## D005 — Motor puzzle propio en Kotlin

- **Decisión:** Implementar `PuzzleEngine` desde cero (sección 14), sin depender de motores externos.
- **Motivo:** El documento maestro lo exige explícitamente. Permite control total de animaciones, solvencia del sliding y optimización para gama baja.
- **Alternativas evaluadas:** librerías externas (pérdida de control, licencias, dependencias).
- **Impacto:** Archivo `feature/puzzle/engine/PuzzleEngine.kt` (~250 líneas).
- **Fecha:** 2026-07-18.
- **Responsable:** Jhoel + IA.

## D006 — Cifrado AES-256/GCM con Android Keystore

- **Decisión:** Usar `AES/GCM/NoPadding` con claves gestionadas por Android Keystore.
- **Motivo:** Estándar moderno, autenticación integrada (GCM tag), sin claves en memoria persistente.
- **Alternativas evaluadas:** AES-CBC (más lento, requiere padding separado), ChaCha20 (no soportado por Keystore en todas las APIs).
- **Impacto:** `CryptoManager` en `core:security`, usado por backup y transfer.
- **Fecha:** 2026-07-18.
- **Responsable:** Jhoel + IA.

## D007 — Formato de respaldo .fpbackup

- **Decisión:** Archivo `.fpbackup` = ZIP (DB + imágenes + manifest) **cifrado** con `CryptoManager`. Cabecera mágica `FPBACKUP01`.
- **Motivo:** Permite integridad (SHA-256 en manifest), cifrado y restauración completa en un solo archivo portable.
- **Alternativas evaluadas:** ZIP sin cifrar (inaceptable para recuerdos privados), tar (sin compresión nativa), JSON+base64 (ineficiente para imágenes).
- **Impacto:** `BackupManager` en `feature:backup`.
- **Fecha:** 2026-07-18.
- **Responsable:** Jhoel + IA.

## D008 — Transferencia por QR cifrado

- **Decisión:** Token de transferencia serializado a JSON, cifrado con `CryptoManager`, codificado como `FPT1.<iv_b64>.<ct_b64>` y renderizado como QR.
- **Motivo:** Permite handshake P2P sin servidores, con cifrado extremo a extremo.
- **Alternativas evaluadas:** Bluetooth directo (complejo), Wi-Fi Direct (incompatible en algunos dispositivos), cloud relay (rompe el principio local-first).
- **Impacto:** `TransferManager` en `feature:transfer`.
- **Fecha:** 2026-07-18.
- **Responsable:** Jhoel + IA.

## D009 — Gradle Version Catalog

- **Decisión:** Centralizar versiones en `gradle/libs.versions.toml`.
- **Motivo:** Estándar moderno de Gradle 8+, evita dispersión de versiones.
- **Alternativas evaluadas:** `buildSrc` con Kotlin (más lento para incremental builds).
- **Impacto:** Todos los `build.gradle.kts` referencian `libs.*`.
- **Fecha:** 2026-07-18.
- **Responsable:** Jhoel + IA.
