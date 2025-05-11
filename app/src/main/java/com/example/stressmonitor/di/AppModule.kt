// app/src/main/java/com/example/stressmonitor/di/AppModule.kt
package com.example.stressmonitor.di

import android.content.Context
import com.example.stressmonitor.data.BleManager
import com.example.stressmonitor.data.BleManagerImpl
import com.example.stressmonitor.data.FitManager
import com.example.stressmonitor.data.TfliteModelInterpreter
import com.example.stressmonitor.data.StressRepository
import com.example.stressmonitor.domain.usecase.GetStressLevelUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides @Singleton
    fun provideBleManager(impl: BleManagerImpl): BleManager = impl

    @Provides @Singleton
    fun provideFitManager(app: Context): FitManager = FitManager(app)

    @Provides @Singleton
    fun provideModelInterpreter(app: Context): TfliteModelInterpreter =
        TfliteModelInterpreter(app)

    @Provides @Singleton
    fun provideStressRepository(
        ble: BleManager,
        fit: FitManager,
        model: TfliteModelInterpreter
    ): StressRepository =
        // Здесь подставьте реальное устройство или заглушку
        StressRepository(ble, fit, model, /* device= */ TODO())

    @Provides @Singleton
    fun provideGetStressLevelUseCase(repo: StressRepository): GetStressLevelUseCase =
        GetStressLevelUseCase(repo)
}
