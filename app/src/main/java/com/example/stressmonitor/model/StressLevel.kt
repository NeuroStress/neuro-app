package com.example.stressmonitor.model

/**
 * Перечисление уровней стресса.
 * Используется для классификации состояния пользователя.
 *
 * @property value Числовое значение уровня стресса (0-100).
 */
enum class StressLevel(val value: Int) {
    /**
     * Низкий уровень стресса (0-25).
     * Характеризуется спокойным состоянием, нормальными физиологическими показателями.
     */
    LOW(25),

    /**
     * Умеренный уровень стресса (26-50).
     * Небольшое напряжение, но в пределах нормы.
     */
    MODERATE(50),

    /**
     * Высокий уровень стресса (51-75).
     * Значительное напряжение, требует внимания.
     */
    HIGH(75),

    /**
     * Критический уровень стресса (76-100).
     * Сильное напряжение, требует немедленного вмешательства.
     */
    CRITICAL(100);

    /**
     * Возвращает текстовое описание уровня стресса.
     * @return Описание уровня стресса на русском языке.
     */
    fun getDescription(): String {
        return when (this) {
            LOW -> "Низкий уровень стресса"
            MODERATE -> "Умеренный уровень стресса"
            HIGH -> "Высокий уровень стресса"
            CRITICAL -> "Критический уровень стресса"
        }
    }
}