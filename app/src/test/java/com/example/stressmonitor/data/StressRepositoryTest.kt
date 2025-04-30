package com.example.stressmonitor.data

import com.example.stressmonitor.domain.model.SensorData
import com.example.stressmonitor.domain.model.StressLevel
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import org.mockito.kotlin.any
import kotlin.test.assertEquals

class StressRepositoryTest {
    private lateinit var bleManager: BleManager
    private lateinit var fitManager: FitManager
    private lateinit var modelInterpreter: TfliteModelInterpreter
    private lateinit var repository: StressRepository
    private lateinit var mockDevice: android.bluetooth.BluetoothDevice

    @Before
    fun setup() {
        bleManager = mock(BleManager::class.java)
        fitManager = mock(FitManager::class.java)
        modelInterpreter = mock(TfliteModelInterpreter::class.java)
        mockDevice = mock(android.bluetooth.BluetoothDevice::class.java)
        
        repository = StressRepository(bleManager, fitManager, modelInterpreter, mockDevice)
    }

    @Test
    fun `test analyze returns correct stress level`() = runBlocking {
        // Подготовка моков
        `when`(fitManager.getLatestHeartRate()).thenReturn(75f)
        `when`(fitManager.getLatestHrv()).thenReturn(50f)
        `when`(bleManager.readGsr(any())).thenReturn(5f)
        `when`(bleManager.readSkinTemp(any())).thenReturn(36.5f)
        `when`(bleManager.readAccel(any())).thenReturn(1.0f)
        `when`(modelInterpreter.predict(any())).thenReturn(StressLevel.CALM)

        // Выполнение теста
        val result = repository.analyze()

        // Проверка результата
        assertEquals(StressLevel.CALM, result)
    }

    @Test
    fun `test fetchSensorData returns correct data`() = runBlocking {
        // Подготовка моков
        `when`(fitManager.getLatestHeartRate()).thenReturn(75f)
        `when`(fitManager.getLatestHrv()).thenReturn(50f)
        `when`(bleManager.readGsr(any())).thenReturn(5f)
        `when`(bleManager.readSkinTemp(any())).thenReturn(36.5f)
        `when`(bleManager.readAccel(any())).thenReturn(1.0f)

        // Выполнение теста
        val result = repository.fetchSensorData()

        // Проверка результата
        assertEquals(SensorData(75f, 50f, 5f, 36.5f, 1.0f), result)
    }
} 