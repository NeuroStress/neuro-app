package com.example.stressmonitor.domain.usecase

import com.example.stressmonitor.data.StressRepository
import com.example.stressmonitor.domain.model.StressLevel

class GetStressLevelUseCase(private val repo: StressRepository) {
    suspend operator fun invoke(): StressLevel =
        repo.analyze()
}
