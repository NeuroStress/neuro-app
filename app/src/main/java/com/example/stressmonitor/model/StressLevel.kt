package com.example.stressmonitor.domain.model

enum class StressLevel {
    CALM,
    LIGHT_STRESS,
    HIGH_STRESS,
    FATIGUE,
    EXCITEMENT;

    companion object {
        fun fromProbabilities(probs: FloatArray): StressLevel {
            val idx = probs.withIndex().maxByOrNull { it.value }!!.index
            return values()[idx]
        }
    }
}
