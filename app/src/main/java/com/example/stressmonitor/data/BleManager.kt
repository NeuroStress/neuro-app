package com.example.stressmonitor.data

import android.Manifest
import android.bluetooth.*
import android.content.Context
import android.content.pm.PackageManager
import android.os.Handler
import android.os.Looper
import androidx.core.content.ContextCompat
import kotlinx.coroutines.suspendCancellableCoroutine
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.util.UUID
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.math.sqrt

/**
 * Интерфейс для работы с BLE-устройствами.
 * Определяет методы для чтения физиологических данных.
 */
interface BleManager {
    /** @return GSR в микросименсах. */
    suspend fun readGsr(device: BluetoothDevice): Float

    /** @return Температура кожи в °C. */
    suspend fun readSkinTemp(device: BluetoothDevice): Float

    /** @return Модуль ускорения в m/s². */
    suspend fun readAccel(device: BluetoothDevice): Float
}

/**
 * Реализация [BleManager] на чистом Android BLE + Coroutines.
 */
class BleManagerImpl(
    private val context: Context
) : BleManager {

    companion object {
        private val GSR_SERVICE_UUID               = UUID.fromString("0000180D-0000-1000-8000-00805F9B34FB")
        private val GSR_CHARACTERISTIC_UUID        = UUID.fromString("00002A37-0000-1000-8000-00805F9B34FB")

        private val SKIN_TEMP_SERVICE_UUID         = UUID.fromString("00001809-0000-1000-8000-00805F9B34FB")
        private val SKIN_TEMP_CHARACTERISTIC_UUID  = UUID.fromString("00002A1C-0000-1000-8000-00805F9B34FB")

        private val ACCEL_SERVICE_UUID             = UUID.fromString("0000180F-0000-1000-8000-00805F9B34FB")
        private val ACCEL_CHARACTERISTIC_UUID      = UUID.fromString("00002A19-0000-1000-8000-00805F9B34FB")
    }

    override suspend fun readGsr(device: BluetoothDevice): Float {
        val raw = readCharacteristic(device, GSR_SERVICE_UUID, GSR_CHARACTERISTIC_UUID)
        return ByteBuffer.wrap(raw)
            .order(ByteOrder.LITTLE_ENDIAN)
            .float
    }

    override suspend fun readSkinTemp(device: BluetoothDevice): Float {
        val raw = readCharacteristic(device, SKIN_TEMP_SERVICE_UUID, SKIN_TEMP_CHARACTERISTIC_UUID)
        return ByteBuffer.wrap(raw)
            .order(ByteOrder.LITTLE_ENDIAN)
            .float
    }

    override suspend fun readAccel(device: BluetoothDevice): Float {
        val raw = readCharacteristic(device, ACCEL_SERVICE_UUID, ACCEL_CHARACTERISTIC_UUID)
        val buf = ByteBuffer.wrap(raw).order(ByteOrder.LITTLE_ENDIAN)
        val x = buf.float
        val y = buf.float
        val z = buf.float
        return sqrt((x * x + y * y + z * z).toDouble()).toFloat()
    }

    /**
     * Connects, discovers services, reads the given characteristic, then closes the GATT.
     * Throws on failure, or if BLUETOOTH_CONNECT isn’t granted.
     */
    private suspend fun readCharacteristic(
        device: BluetoothDevice,
        serviceUuid: UUID,
        characteristicUuid: UUID
    ): ByteArray = suspendCancellableCoroutine { cont ->
        // 1. Permission check (Android 12+)
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.BLUETOOTH_CONNECT)
            != PackageManager.PERMISSION_GRANTED
        ) {
            cont.resumeWithException(SecurityException("Bluetooth CONNECT permission not granted"))
            return@suspendCancellableCoroutine
        }

        var gatt: BluetoothGatt? = null
        val mainHandler = Handler(Looper.getMainLooper())

        val callback = object : BluetoothGattCallback() {
            override fun onConnectionStateChange(g: BluetoothGatt, status: Int, newState: Int) {
                if (status != BluetoothGatt.GATT_SUCCESS) {
                    cont.resumeWithException(Exception("Connection failed (status=$status)"))
                    cleanup()
                    return
                }
                if (newState == BluetoothProfile.STATE_CONNECTED) {
                    gatt = g
                    // Need to call discoverServices on the main thread
                    mainHandler.post { g.discoverServices() }
                } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
                    cont.resumeWithException(Exception("Disconnected before operation"))
                    cleanup()
                }
            }

            override fun onServicesDiscovered(g: BluetoothGatt, status: Int) {
                if (status != BluetoothGatt.GATT_SUCCESS) {
                    cont.resumeWithException(Exception("Service discovery failed (status=$status)"))
                    cleanup()
                    return
                }
                val svc = g.getService(serviceUuid)
                    ?: run {
                        cont.resumeWithException(Exception("Service $serviceUuid not found"))
                        cleanup()
                        return
                    }
                val chr = svc.getCharacteristic(characteristicUuid)
                    ?: run {
                        cont.resumeWithException(Exception("Characteristic $characteristicUuid not found"))
                        cleanup()
                        return
                    }
                mainHandler.post { g.readCharacteristic(chr) }
            }

            override fun onCharacteristicRead(
                g: BluetoothGatt,
                characteristic: BluetoothGattCharacteristic,
                status: Int
            ) {
                if (status != BluetoothGatt.GATT_SUCCESS) {
                    cont.resumeWithException(Exception("Read failed (status=$status)"))
                } else {
                    cont.resume(characteristic.value)
                }
                cleanup()
            }

            private fun cleanup() {
                gatt?.let {
                    mainHandler.post {
                        it.disconnect()
                        it.close()
                    }
                }
            }
        }

        // 2. Kick off the connection
        gatt = device.connectGatt(context, false, callback)

        // 3. If coroutine is cancelled, tear down
        cont.invokeOnCancellation {
            gatt?.disconnect()
            gatt?.close()
        }
    }
}
