package com.example.stressmonitor.domain.model

data class SensorData(
    val heartRate: Float,      // bpm
    val hrv: Float,            // SDNN или другой HF/LF показатель
    val gsr: Float,            // skin conductance
    val skinTemp: Float,       // °C
    val accelMagnitude: Float  // RMS ускорения за сэмпл
) {
    fun toFloatArray(): FloatArray = floatArrayOf(
        heartRate, hrv, gsr, skinTemp, accelMagnitude
    )
}
