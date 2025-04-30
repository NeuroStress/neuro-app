package com.example.stressmonitor.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.stressmonitor.domain.model.StressLevel
import com.example.stressmonitor.domain.usecase.GetStressLevelUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class StressViewModel(
    private val getStressLevelUseCase: GetStressLevelUseCase
) : ViewModel() {
    private val _stressLevel = MutableStateFlow<StressLevel?>(null)
    val stressLevel: StateFlow<StressLevel?> = _stressLevel.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    fun fetchStressLevel() {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                _error.value = null
                _stressLevel.value = getStressLevelUseCase()
            } catch (e: Exception) {
                _error.value = e.message ?: "Произошла ошибка"
            } finally {
                _isLoading.value = false
            }
        }
    }
} 