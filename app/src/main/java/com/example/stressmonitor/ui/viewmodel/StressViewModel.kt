// app/src/main/java/com/example/stressmonitor/ui/viewmodel/StressViewModel.kt
package com.example.stressmonitor.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.stressmonitor.domain.usecase.GetStressLevelUseCase
import com.example.stressmonitor.model.StressLevel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StressViewModel @Inject constructor(
    private val getStressLevelUseCase: GetStressLevelUseCase
): ViewModel(){
    private val _stressLevel = MutableStateFlow<StressLevel?>(null)
    val stressLevel: StateFlow<StressLevel?> = _stressLevel.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    fun fetchStressLevel() {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                _stressLevel.value = getStressLevelUseCase()
            } catch (e: Exception) {
                _error.value = e.message ?: "Ошибка"
            } finally {
                _isLoading.value = false
            }
        }
    }
}
