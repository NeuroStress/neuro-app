package com.example.stressmonitor.data

import android.bluetooth.BluetoothDevice
import com.example.stressmonitor.model.SensorData
import com.example.stressmonitor.model.StressLevel

/**
 * Репозиторий для работы с данными о стрессе.
 * Объединяет данные из различных источников (BLE, Google Fit) и выполняет анализ.
 *
 * @property ble Менеджер для работы с BLE-устройствами.
 * @property fit Менеджер для работы с Google Fit.
 * @property model Интерпретатор TensorFlow Lite модели.
 * @property device BLE-устройство для сбора данных.
 */
class StressRepository(
    private val ble: BleManager,
    private val fit: FitManager,
    private val model: TfliteModelInterpreter,
    private val device: BluetoothDevice
) {
    /**
     * Собирает данные со всех сенсоров.
     * @return Объект [SensorData] с текущими значениями всех показателей.
     */
    suspend fun fetchSensorData(): SensorData {
        val hr = fit.getLatestHeartRate()
        val hrv = fit.getLatestHrv()
        val gsr = ble.readGsr(device)
        val temp = ble.readSkinTemp(device)
        val accel = ble.readAccel(device)
        return SensorData(hr, hrv, gsr, temp, accel)
    }

    /**
     * Анализирует собранные данные и определяет уровень стресса.
     * @return Текущий уровень стресса пользователя.
     */
    suspend fun analyze(): StressLevel {
        val data = fetchSensorData()
        return model.predict(data.toFloatArray())
    }
}
