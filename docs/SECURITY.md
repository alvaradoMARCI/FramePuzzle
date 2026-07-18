# FramePuzzle — Security

> *"Los recuerdos del usuario son información privada."*

## 1. Principios

FramePuzzle aplica seguridad en tres capas (sección 38):

1. **Protección de acceso** — PIN, biometría, bloqueo de aplicación.
2. **Protección de datos** — cifrado local, archivos protegidos, validación de permisos.
3. **Protección de acciones sensibles** — confirmación antes de eliminar, exportar, restaurar respaldos o transferir datos.

## 2. Implementación

### 2.1 PIN

- Almacenado como **hash SHA-256 + salt** dentro de `EncryptedSharedPreferences` (cifrado adicional con `MasterKey` AES-256-GCM).
- El PIN real **nunca** se persiste en texto plano.
- Verificación en memoria únicamente durante el desbloqueo.

Implementación: `core/security/pin/PinManager.kt`.

### 2.2 Biometría

- `BiometricManagerHelper` encapsula `androidx.biometric.BiometricPrompt`.
- Solo se usa como **atajo**; el PIN siempre está disponible como fallback (`setNegativeButtonText("Usar PIN")`).
- No se almacenan datos biométricos: Android Keystore gestiona la autenticación.

### 2.3 Cifrado de datos sensibles

`CryptoManager` (ubicado en `core/security/crypto`):

- Algoritmo: **AES/GCM/NoPadding** (256 bits).
- Claves: **Android Keystore** (no salen del dispositivo).
- IV: generado aleatoriamente por cada cifrado (12 bytes, incluido en el payload).

Usos:

- Cifrar archivos de respaldo `.fpbackup`.
- Cifrar tokens de transferencia entre teléfonos.
- Cifrar el PIN (capa adicional sobre EncryptedSharedPreferences).

### 2.4 Almacenamiento

- Todos los archivos viven en **almacenamiento interno** (`/data/data/<pkg>/files/FramePuzzle/`), no accesibles desde otras apps sin permiso explícito vía `FileProvider`.
- No se crea ninguna carpeta visible en almacenamiento público.
- `dataExtractionRules.xml` desactiva cloud-backup y device-transfer de Android.

### 2.5 Acciones sensibles (sección 38)

La UI debe solicitar confirmación explícita antes de:

- Eliminar recuerdos.
- Exportar información.
- Restaurar respaldos.
- Transferir datos a otro teléfono.

Esto se implementa con `AlertDialog` antes de invocar los repositorios.

## 3. Permisos

| Permiso | Uso |
|---------|-----|
| `CAMERA` | Captura de fotografías (CameraX). |
| `READ_MEDIA_IMAGES` (Android 13+) | Importar de galería. |
| `READ_EXTERNAL_STORAGE` (≤ API 32) | Compatibilidad anterior. |
| `USE_BIOMETRIC` | Desbloqueo biométrico. |
| `INTERNET` / `ACCESS_NETWORK_STATE` | Transferencia acelerada opcional. |
| `VIBRATE` | Vibración háptica al completar puzzles. |

## 4. Reglas del agente IA (sección 47)

- Nunca guardar tokens dentro del código.
- Nunca subir credenciales.
- Nunca exponer claves privadas.
- Nunca compartir información sensible.
- Usar variables de entorno, GitHub Secrets, archivos ignorados, rotación de claves.

El token GitHub del agente **no** se versiona en el repositorio. Vive en variables de entorno locales.
