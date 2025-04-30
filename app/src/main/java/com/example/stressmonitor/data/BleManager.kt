package com.example.stressmonitor.data

import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCallback
import android.bluetooth.BluetoothGattCharacteristic
import android.bluetooth.BluetoothGattDescriptor
import android.bluetooth.BluetoothProfile
import android.content.Context
import kotlinx.coroutines.suspendCancellableCoroutine
import no.nordicsemi.android.ble.BleManager
import no.nordicsemi.android.ble.callback.DataReceivedCallback
import no.nordicsemi.android.ble.data.Data
import java.util.UUID
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

/**
 * Интерфейс для работы с BLE-устройствами.
 * Определяет методы для чтения физиологических данных.
 */
interface BleManager {
    /**
     * Считывает значение GSR (кожно-гальванической реакции) с BLE-устройства.
     * @param device Устройство Bluetooth.
     * @return Значение GSR в микросименсах.
     */
    suspend fun readGsr(device: BluetoothDevice): Float

    /**
     * Считывает температуру кожи с BLE-устройства.
     * @param device Устройство Bluetooth.
     * @return Температура кожи в градусах Цельсия.
     */
    suspend fun readSkinTemp(device: BluetoothDevice): Float

    /**
     * Считывает модуль ускорения с BLE-устройства.
     * @param device Устройство Bluetooth.
     * @return Модуль ускорения в м/с².
     */
    suspend fun readAccel(device: BluetoothDevice): Float
}

/**
 * Реализация [BleManager] на основе библиотеки Nordic BLE.
 * Обеспечивает подключение к BLE-устройствам и чтение характеристик.
 *
 * @property context Контекст приложения.
 */
class BleManagerImpl(context: Context) : no.nordicsemi.android.ble.BleManager(context), BleManager {
    companion object {
        /** UUID сервиса и характеристики для GSR (замените на реальные UUID вашего устройства) */
        private val GSR_SERVICE_UUID = UUID.fromString("0000180D-0000-1000-8000-00805F9B34FB")
        private val GSR_CHARACTERISTIC_UUID = UUID.fromString("00002A37-0000-1000-8000-00805F9B34FB")
        
        /** UUID сервиса и характеристики для температуры кожи */
        private val SKIN_TEMP_SERVICE_UUID = UUID.fromString("00001809-0000-1000-8000-00805F9B34FB")
        private val SKIN_TEMP_CHARACTERISTIC_UUID = UUID.fromString("00002A1C-0000-1000-8000-00805F9B34FB")
        
        /** UUID сервиса и характеристики для акселерометра */
        private val ACCEL_SERVICE_UUID = UUID.fromString("0000180F-0000-1000-8000-00805F9B34FB")
        private val ACCEL_CHARACTERISTIC_UUID = UUID.fromString("00002A19-0000-1000-8000-00805F9B34FB")
    }

    private var gatt: BluetoothGatt? = null

    override suspend fun readGsr(device: BluetoothDevice): Float = suspendCancellableCoroutine { cont ->
        connect(device)
            .useAutoConnect(false)
            .timeout(10000)
            .usePreferredPhy(BluetoothDevice.PHY_LE_1M_MASK)
            .then { gatt ->
                this.gatt = gatt
                readCharacteristic(gatt, GSR_SERVICE_UUID, GSR_CHARACTERISTIC_UUID)
            }
            .with { data ->
                val value = data.getFloatValue(Data.FORMAT_FLOAT, 0)
                cont.resume(value)
            }
            .enqueue()
    }

    override suspend fun readSkinTemp(device: BluetoothDevice): Float = suspendCancellableCoroutine { cont ->
        connect(device)
            .useAutoConnect(false)
            .timeout(10000)
            .then { gatt ->
                readCharacteristic(gatt, SKIN_TEMP_SERVICE_UUID, SKIN_TEMP_CHARACTERISTIC_UUID)
            }
            .with { data ->
                val value = data.getFloatValue(Data.FORMAT_FLOAT, 0)
                cont.resume(value)
            }
            .enqueue()
    }

    override suspend fun readAccel(device: BluetoothDevice): Float = suspendCancellableCoroutine { cont ->
        connect(device)
            .useAutoConnect(false)
            .timeout(10000)
            .then { gatt ->
                readCharacteristic(gatt, ACCEL_SERVICE_UUID, ACCEL_CHARACTERISTIC_UUID)
            }
            .with { data ->
                val x = data.getFloatValue(Data.FORMAT_FLOAT, 0)
                val y = data.getFloatValue(Data.FORMAT_FLOAT, 4)
                val z = data.getFloatValue(Data.FORMAT_FLOAT, 8)
                val magnitude = Math.sqrt((x * x + y * y + z * z).toDouble()).toFloat()
                cont.resume(magnitude)
            }
            .enqueue()
    }

    /**
     * Читает значение характеристики BLE-устройства.
     * @param gatt Объект GATT для работы с BLE.
     * @param serviceUuid UUID сервиса.
     * @param characteristicUuid UUID характеристики.
     */
    private fun readCharacteristic(
        gatt: BluetoothGatt,
        serviceUuid: UUID,
        characteristicUuid: UUID
    ) {
        val service = gatt.getService(serviceUuid)
        val characteristic = service?.getCharacteristic(characteristicUuid)
        if (characteristic != null) {
            gatt.readCharacteristic(characteristic)
        }
    }

    override fun getGattCallback(): BluetoothGattCallback {
        return object : BluetoothGattCallback() {
            override fun onConnectionStateChange(gatt: BluetoothGatt, status: Int, newState: Int) {
                if (newState == BluetoothProfile.STATE_CONNECTED) {
                    gatt.discoverServices()
                }
            }

            override fun onServicesDiscovered(gatt: BluetoothGatt, status: Int) {
                if (status == BluetoothGatt.GATT_SUCCESS) {
                    // Уведомляем о готовности к чтению характеристик
                }
            }
        }
    }
}
