# FramePuzzle app - ProGuard rules

# Hilt
-keep class dagger.hilt.** { *; }
-keep class * extends dagger.hilt.android.lifecycle.HiltViewModel { *; }

# Room
-keep class * extends androidx.room.RoomDatabase { *; }
-keep @androidx.room.Entity class * { *; }
-keepclassmembers class * {
    @androidx.room.* <methods>;
    @androidx.room.* <fields>;
}

# Kotlinx Serialization
-keepattributes *Annotation*, InnerClasses
-dontnote kotlinx.serialization.AnnotationsKt
-keepclassmembers class **$$serializer { *; }
-keepclassmembers @kotlinx.serialization.Serializable class * {
    static **$* *;
}

# CameraX
-keep class androidx.camera.** { *; }

# ZXing
-keep class com.google.zxing.** { *; }
-keep class com.journeyapps.barcodescanner.** { *; }

# Tink (usado por AndroidX SecurityCrypto para EncryptedSharedPreferences)
-keep class com.google.crypto.tink.** { *; }

# ErrorProne annotations (referenciadas por Tink pero no incluidas en runtime)
-dontwarn com.google.errorprone.annotations.**
-dontwarn javax.annotation.**

# Tink KeysDownloader (referencias opcionales a Google HTTP y Joda)
-dontwarn com.google.api.client.http.**
-dontwarn org.joda.time.**

# AndroidX SecurityCrypto
-keep class androidx.security.crypto.** { *; }

# Coil
-dontwarn coil.**

# Compose
-keepclassmembers class androidx.compose.** { *; }

