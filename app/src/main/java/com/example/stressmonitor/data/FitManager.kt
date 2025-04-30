package com.example.stressmonitor.data

import android.content.Context
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.fitness.Fitness
import com.google.android.gms.fitness.FitnessOptions
import com.google.android.gms.fitness.data.DataType
import com.google.android.gms.fitness.request.DataReadRequest
import com.google.android.gms.tasks.Tasks
import kotlinx.coroutines.tasks.await
import java.util.concurrent.TimeUnit

class FitManager(private val context: Context) {
    private val fitnessOptions = FitnessOptions.builder()
        .addDataType(DataType.TYPE_HEART_RATE_BPM, FitnessOptions.ACCESS_READ)
        .addDataType(DataType.TYPE_HEART_RATE_VARIABILITY_SDNN, FitnessOptions.ACCESS_READ)
        .build()

    suspend fun getLatestHeartRate(): Float {
        val account = getSignedInAccount()
        val readRequest = DataReadRequest.Builder()
            .read(DataType.TYPE_HEART_RATE_BPM)
            .setTimeRange(System.currentTimeMillis() - 5*60_000, System.currentTimeMillis(), TimeUnit.MILLISECONDS)
            .build()

        val response = Fitness.getHistoryClient(context, account)
            .readData(readRequest)
            .await()
        
        return response.dataSets
            .flatMap { it.dataPoints }
            .lastOrNull()
            ?.getValue(it.dataType.fields[0])?.asFloat() ?: 0f
    }

    suspend fun getLatestHrv(): Float {
        val account = getSignedInAccount()
        val readRequest = DataReadRequest.Builder()
            .read(DataType.TYPE_HEART_RATE_VARIABILITY_SDNN)
            .setTimeRange(System.currentTimeMillis() - 5*60_000, System.currentTimeMillis(), TimeUnit.MILLISECONDS)
            .build()

        val response = Fitness.getHistoryClient(context, account)
            .readData(readRequest)
            .await()
        
        return response.dataSets
            .flatMap { it.dataPoints }
            .lastOrNull()
            ?.getValue(it.dataType.fields[0])?.asFloat() ?: 0f
    }

    private suspend fun getSignedInAccount(): GoogleSignInAccount {
        val account = GoogleSignIn.getLastSignedInAccount(context)
        if (account != null && GoogleSignIn.hasPermissions(account, fitnessOptions)) {
            return account
        }

        val signInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .addExtension(fitnessOptions)
            .build()

        val signInClient = GoogleSignIn.getClient(context, signInOptions)
        val signInIntent = signInClient.signInIntent
        
        return Tasks.await(signInClient.silentSignIn())
            ?: throw IllegalStateException("Google Sign-In failed")
    }
}
