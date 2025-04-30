package com.example.stressmonitor.data

import android.content.Context
import com.example.stressmonitor.domain.model.StressLevel
import org.tensorflow.lite.Interpreter
import org.tensorflow.lite.support.common.FileUtil
import java.io.IOException
import java.nio.ByteBuffer

/**
 * Класс-обёртка для работы с TensorFlow Lite моделью.
 * Обеспечивает загрузку модели и выполнение предсказаний уровня стресса.
 *
 * @property context Контекст приложения.
 */
class TfliteModelInterpreter(private val context: Context) {
    private var interpreter: Interpreter? = null
    private val modelPath = "model.tflite"

    init {
        loadModel()
    }

    /**
     * Загружает модель TensorFlow Lite из assets.
     * @throws RuntimeException если не удалось загрузить модель.
     */
    private fun loadModel() {
        try {
            val modelBuffer = FileUtil.loadMappedFile(context, modelPath)
            val options = Interpreter.Options().apply {
                setNumThreads(4)
            }
            interpreter = Interpreter(modelBuffer, options)
        } catch (e: IOException) {
            throw RuntimeException("Error loading model: ${e.message}")
        }
    }

    /**
     * Выполняет предсказание уровня стресса по входным данным.
     * @param inputArray Массив из 5 float-значений: [heartRate, hrv, gsr, skinTemp, accelMagnitude]
     * @return Уровень стресса (StressLevel)
     * @throws IllegalStateException если модель не загружена.
     */
    fun predict(inputArray: FloatArray): StressLevel {
        val interpreter = interpreter ?: throw IllegalStateException("Model not loaded")
        
        // Подготовка входных и выходных тензоров
        val inputShape = interpreter.getInputTensor(0).shape()
        val outputShape = interpreter.getOutputTensor(0).shape()
        
        val inputBuffer = ByteBuffer.allocateDirect(inputShape[1] * Float.SIZE_BYTES)
        val outputBuffer = ByteBuffer.allocateDirect(outputShape[1] * Float.SIZE_BYTES)
        
        // Заполнение входного буфера
        inputBuffer.rewind()
        for (value in inputArray) {
            inputBuffer.putFloat(value)
        }
        
        // Выполнение вывода
        interpreter.run(inputBuffer, outputBuffer)
        
        // Чтение результатов
        outputBuffer.rewind()
        val output = FloatArray(outputShape[1])
        for (i in output.indices) {
            output[i] = outputBuffer.float
        }
        
        return StressLevel.fromProbabilities(output)
    }

    /**
     * Освобождает ресурсы, связанные с моделью.
     * Должен вызываться при завершении работы с интерпретатором.
     */
    fun close() {
        interpreter?.close()
        interpreter = null
    }
}
