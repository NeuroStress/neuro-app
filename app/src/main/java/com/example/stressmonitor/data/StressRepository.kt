package com.example.stressmonitor.data

import android.bluetooth.BluetoothDevice
import com.example.stressmonitor.domain.model.SensorData

class StressRepository(
    private val ble: BleManager,
    private val fit: FitManager,
    private val model: TfliteModelInterpreter,
    private val device: BluetoothDevice
) {
    suspend fun fetchSensorData(): SensorData {
        val hr = fit.getLatestHeartRate()
        val hrv = fit.getLatestHrv()
        val gsr = ble.readGsr(device)
        val temp = ble.readSkinTemp(device)
        val accel = ble.readAccel(device)
        return SensorData(hr, hrv, gsr, temp, accel)
    }

    suspend fun analyze(): StressLevel {
        val data = fetchSensorData()
        return model.predict(data.toFloatArray())
    }
}
