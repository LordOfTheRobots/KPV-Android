package com.kpv.lesson.ui.state

import com.kpv.lesson.dto.WeatherResponse

sealed class UiState {
    object Idle : UiState()
    object Loading : UiState()
    data class Success(val data: WeatherResponse) : UiState()
    data class Error(val message: String) : UiState()
}