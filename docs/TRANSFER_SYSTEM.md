# FramePuzzle — Transfer System

> *"Permitir cambiar de teléfono sin perder ningún recuerdo."*

## 1. Objetivo (sección 37)

Permitir migrar recuerdos entre dispositivos sin nube obligatoria y sin servidores permanentes.

## 2. Método principal: QR

### Flujo

```
Teléfono antiguo:
  Genera código de transferencia (QR cifrado)
        ↓
Teléfono nuevo:
  Escanea código
        ↓
  Se inicia transferencia segura (P2P)
```

## 3. Implementación

### 3.1 Token

`TransferToken` (serializable con `kotlinx.serialization`):

```kotlin
data class TransferToken(
    val deviceName: String,
    val sessionId: String,
    val timestamp: Long,
    val port: Int,
    val otp: String,
    val dataSizeBytes: Long,
)
```

### 3.2 Cifrado

El token se serializa a JSON y se cifra con `CryptoManager` (AES-256/GCM). El resultado se codifica como:

```
FPT1.<iv_base64>.<ciphertext_base64>
```

Esto se renderiza como QR con ZXing (`ErrorCorrectionLevel.M`, margen 1).

### 3.3 Render QR

`TransferManager.renderQrBitmap(content, size = 600)` devuelve un `Bitmap` listando para mostrar en Compose.

### 3.4 Decodificación

El teléfono receptor escanea el QR (vía `zxing-android-embedded`), obtiene el string, lo decodifica con `TransferManager.decodeToken(qr)` y recupera el `TransferToken` original.

## 4. Características

- **Transferencia directa** (P2P) tras el handshake.
- **Uso opcional de internet** para acelerar (cuando ambos dispositivos están en la misma red).
- **Datos protegidos** (OTP de un solo uso, cifrado AES-256).
- **Sin dependencia permanente de servidores**.

## 5. Consideraciones de seguridad

- El OTP caduca tras `timestamp + TTL` (configurable, por defecto 5 minutos).
- La sesión se invalida tras un único uso.
- En caso de fallo de red, se permite reintento con nuevo token.

## 6. Módulos relacionados

- `feature:transfer` — UI y `TransferManager`.
- `core:security` — `CryptoManager`.
- `feature:backup` — genera el payload a transferir (reutiliza el formato `.fpbackup`).
