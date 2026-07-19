# FramePuzzle ProGuard/R8 rules
-keepattributes *Annotation*, InnerClasses, EnclosingMethod, Signature, Exceptions
-dontoptimize

-keep class com.jhoel.framepuzzle.** { *; }
-keep @dagger.hilt.android.HiltAndroidApp class * { *; }
-keep @dagger.hilt.android.AndroidEntryPoint class * { *; }
-keep @dagger.hilt.android.lifecycle.HiltViewModel class * { *; }
-keep @dagger.hilt.EntryPoint class * { *; }
-keep @dagger.Module class * { *; }

-keep class dagger.hilt.** { *; }
-keep class dagger.internal.** { *; }
-keep class **_Hilt* { *; }
-keep class **_GeneratedInjector { *; }
-keep class **_Factory { *; }
-keep class Hilt_* { *; }
-keepclassmembers class * {
    @javax.inject.Inject *;
    @dagger.Provides *;
}

-keep class androidx.compose.runtime.** { *; }
-keepclassmembers class * {
    @androidx.compose.runtime.Composable <methods>;
}

-keep class kotlin.Metadata { *; }
-keepclassmembers class **$Companion { *; }

-keep class * extends androidx.room.RoomDatabase { *; }
-keep @androidx.room.Entity class * { *; }
-keep @androidx.room.Dao class * { *; }
-keep class **_Impl { *; }
-keepclassmembers class * {
    @androidx.room.* <methods>;
    @androidx.room.* <fields>;
    @androidx.room.TypeConverter <methods>;
}

-keepclassmembers class **$$serializer { *; }
-keepclassmembers @kotlinx.serialization.Serializable class * {
    static **$* *;
    *** Companion;
}

-keep class androidx.camera.** { *; }
-keep class com.google.zxing.** { *; }
-keep class com.journeyapps.barcodescanner.** { *; }
-keep class com.google.crypto.tink.** { *; }
-keep class androidx.security.crypto.** { *; }
-keep class coil.** { *; }
-keep class androidx.navigation.** { *; }
-keep class androidx.lifecycle.** { *; }

-dontwarn com.google.errorprone.annotations.**
-dontwarn javax.annotation.**
-dontwarn com.google.api.client.http.**
-dontwarn com.google.api.client.json.**
-dontwarn org.joda.time.**
-dontwarn coil.**

-keepclassmembers class * extends androidx.lifecycle.ViewModel {
    <init>();
}
-keepclassmembers class * extends androidx.lifecycle.AndroidViewModel {
    <init>(...);
}
