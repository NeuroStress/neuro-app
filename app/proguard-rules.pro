# TensorFlow Lite
-keep class org.tensorflow.lite.** { *; }
-keep class org.tensorflow.lite.support.** { *; }

# BLE
-keep class no.nordicsemi.android.ble.** { *; }
-keep class no.nordicsemi.android.ble.ktx.** { *; }

# Google Fit
-keep class com.google.android.gms.fitness.** { *; }
-keep class com.google.android.gms.auth.** { *; }

# Coroutines
-keep class kotlinx.coroutines.** { *; }
-keep class kotlinx.coroutines.internal.** { *; }

# Keep model files
-keep class com.example.stressmonitor.data.TfliteModelInterpreter { *; }
-keep class com.example.stressmonitor.domain.model.** { *; } 