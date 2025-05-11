package com.example.stressmonitor.data

import android.content.Context
import androidx.health.connect.client.HealthConnectClient
import androidx.health.connect.client.permission.HealthPermission
import androidx.health.connect.client.permission.PermissionController
import androidx.health.connect.client.records.HeartRateRecord
import androidx.health.connect.client.records.HrvSdnnRecord
import androidx.health.connect.client.request.ReadRecordsRequest
import androidx.health.connect.client.time.TimeRangeFilter
import java.time.Instant
import java.time.temporal.ChronoUnit

/**
 * Менеджер для работы с Health Connect API.
 */
class FitManager(context: Context) {

    private val client = HealthConnectClient.getOrCreate(context)

    // Какие права нам нужны:
    private val permissions = setOf(
        HealthPermission.read(HeartRateRecord::class),
        HealthPermission.read(HrvSdnnRecord::class)
    )

    /**
     * Вызывать перед чтением данных, чтобы запросить или проверить разрешения.
     * @throws SecurityException если пользователь не дал права.
     */
    suspend fun ensurePermissions() {
        val granted = client.permissionController
            .getGrantedPermissions(permissions)
            .first()
        if (!granted.containsAll(permissions)) {
            // Запускает UI Health Connect для выдачи прав
            PermissionController
                .getOrCreate(context)
                .requestPermissions(permissions)
                .await()
        }
    }

    /**
     * Последнее значение пульса (bpm) за последние 5 минут.
     */
    suspend fun getLatestHeartRate(): Float {
        val end = Instant.now()
        val start = end.minus(5, ChronoUnit.MINUTES)

        val request = ReadRecordsRequest(
            recordType = HeartRateRecord::class,
            timeRangeFilter = TimeRangeFilter.between(start, end),
            ascendingOrder = false,
            limit = 1
        )

        val response = client.readRecords(request)
        return response.records.firstOrNull()?.beatsPerMinute ?: 0f
    }

    /**
     * Последнее значение SDNN (HRV, мс) за последние 5 минут.
     */
    suspend fun getLatestHrv(): Float {
        val end = Instant.now()
        val start = end.minus(5, ChronoUnit.MINUTES)

        val request = ReadRecordsRequest(
            recordType = HrvSdnnRecord::class,
            timeRangeFilter = TimeRangeFilter.between(start, end),
            ascendingOrder = false,
            limit = 1
        )

        val response = client.readRecords(request)
        return response.records.firstOrNull()?.sdnn ?: 0f
    }
}
