package com.example.stressmonitor.domain.model

/**
 * Класс для хранения данных с сенсоров.
 * Содержит все необходимые физиологические показатели для анализа стресса.
 *
 * @property heartRate Частота сердечных сокращений (ударов в минуту).
 * @property hrv Вариабельность сердечного ритма (миллисекунды).
 * @property gsr Кожно-гальваническая реакция (микросименсы).
 * @property skinTemp Температура кожи (градусы Цельсия).
 * @property accel Данные акселерометра (g).
 */
data class SensorData(
    val heartRate: Float,
    val hrv: Float,
    val gsr: Float,
    val skinTemp: Float,
    val accel: Float
) {
    /**
     * Преобразует данные сенсоров в массив float для передачи в модель.
     * @return Массив значений в порядке: ЧСС, ВСР, КГР, температура, акселерометр.
     */
    fun toFloatArray(): FloatArray {
        return floatArrayOf(heartRate, hrv, gsr, skinTemp, accel)
    }
} 