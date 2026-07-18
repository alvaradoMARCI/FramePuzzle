# FramePuzzle

> **"Arma tus recuerdos"**

Transforma tus fotografías personales en **puzzles interactivos**. FramePuzzle convierte una imagen en una experiencia, permitiéndote revivir tus recuerdos mientras los armas pieza por pieza.

| | |
|---|---|
| **Plataforma** | Android 9+ (API 28) |
| **Lenguaje** | Kotlin |
| **UI** | Jetpack Compose |
| **Arquitectura** | Clean Architecture + MVVM (multi-módulo) |
| **DB** | Room Database |
| **Config** | DataStore |
| **Cámara** | CameraX |
| **DI** | Hilt |
| **Versión** | 0.1.0-alpha |

---

## Características

- **Sistema de recuerdos** — guarda la imagen original y una copia editada. La original jamás se modifica.
- **Editor no destructivo** — recorte, rotación, brillo, contraste, saturación, temperatura, exposición, filtros propios (Vintage, Nostalgia, Cinemático, B&N, Recuerdo antiguo).
- **Motor puzzle propio** — dos modos: Clásico (intercambio de piezas) y Deslizante (ranura vacía). Cuatro niveles de dificultad: Fácil, Normal, Difícil, Personalizado.
- **Animación al completar** — piezas que se unen con pequeñas separaciones para conservar la identidad de puzzle, con efectos visuales dorados.
- **Sistema de progreso** — XP, niveles, logros y evolución del avatar.
- **Biblioteca** — recuerdos, álbumes (automáticos y manuales), historial y favoritos.
- **Seguridad** — PIN cifrado (SHA-256 + EncryptedSharedPreferences), biometría con fallback a PIN, cifrado AES-256/GCM vía Android Keystore.
- **Respaldo** — archivo `.fpbackup` (ZIP cifrado con manifest e integrity check SHA-256).
- **Transferencia entre teléfonos** — QR cifrado para handshake P2P, sin dependencia de servidores.
- **100% local-first** — no se requiere nube obligatoria.

---

## Estructura del repositorio

```
FramePuzzle/
├── app/                          # MainActivity, Application, navegación
├── core/
│   ├── database/                 # Room, entidades, DAOs, DI
│   ├── storage/                  # LocalStorageManager (FramePuzzle/)
│   ├── security/                 # CryptoManager, PinManager, BiometricManagerHelper
│   ├── designsystem/             # Tema, colores, tipografía, componentes
│   └── utils/                    # TimeUtils, ImageUtils, FramePuzzleResult, logger
├── feature/
│   ├── camera/                   # CameraX + galería + creación de recuerdos
│   ├── editor/                   # Editor no destructivo + filtros
│   ├── puzzle/                   # Motor puzzle + UI + animaciones
│   ├── library/                  # Biblioteca + álbumes + búsqueda
│   ├── profile/                  # Usuario + avatar + XP + logros + seguridad
│   ├── backup/                   # .fpbackup cifrado
│   ├── transfer/                 # QR + transferencia entre teléfonos
│   └── settings/                 # DataStore + preferencias
├── docs/                         # Documentación viva
│   ├── PRODUCT_VISION.md
│   ├── ARCHITECTURE.md
│   ├── DATABASE.md
│   ├── SECURITY.md
│   ├── PUZZLE_ENGINE.md
│   ├── TRANSFER_SYSTEM.md
│   ├── ROADMAP.md
│   ├── DECISIONS.md
│   └── AUTHORSHIP.md
├── README.md
├── CHANGELOG.md
└── FramePuzzle_Master_Document.md
```

---

## Cómo compilar

1. Clona el repositorio.
2. Abre el proyecto en **Android Studio Hedgehog o superior**.
3. Espera a que Gradle sincronice.
4. Conecta un dispositivo Android 9+ o inicia un emulador.
5. Pulsa **Run** (▶).

> **Nota:** Este proyecto se generó siguiendo el `FramePuzzle_Master_Document.md`. Revisa `docs/ROADMAP.md` para ver el estado de cada fase.

---

## Documentación

Toda la documentación viva está en `docs/`:

- [`PRODUCT_VISION.md`](docs/PRODUCT_VISION.md) — Visión del producto.
- [`ARCHITECTURE.md`](docs/ARCHITECTURE.md) — Arquitectura técnica.
- [`DATABASE.md`](docs/DATABASE.md) — Esquema de base de datos.
- [`SECURITY.md`](docs/SECURITY.md) — Seguridad y privacidad.
- [`PUZZLE_ENGINE.md`](docs/PUZZLE_ENGINE.md) — Motor puzzle.
- [`TRANSFER_SYSTEM.md`](docs/TRANSFER_SYSTEM.md) — Sistema de transferencia.
- [`ROADMAP.md`](docs/ROADMAP.md) — Roadmap y estado de fases.
- [`DECISIONS.md`](docs/DECISIONS.md) — Registro de decisiones técnicas.
- [`AUTHORSHIP.md`](docs/AUTHORSHIP.md) — Autoría y roles.

---

## Seguridad

- **No se suben credenciales al repositorio.** El token GitHub del agente vive en variables de entorno.
- Los recuerdos se guardan en almacenamiento **interno** (no público).
- Respaldo y transferencias están **cifrados** con AES-256/GCM.
- El PIN se almacena como hash SHA-256 dentro de EncryptedSharedPreferences.

Si encuentras una vulnerabilidad, no abras un issue público: contacta directamente al creador.

---

## Autoría

FramePuzzle es una creación humana de **Jhoel**, desarrollada con apoyo de inteligencia artificial. La IA funciona como herramienta; la visión, dirección y decisiones finales pertenecen al creador.

Ver [`docs/AUTHORSHIP.md`](docs/AUTHORSHIP.md) para más detalle.

---

## Licencia

Propietaria. Todos los derechos reservados © Jhoel.
