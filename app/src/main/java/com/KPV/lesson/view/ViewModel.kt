package com.kpv.lesson.view

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kpv.lesson.BuildConfig
import com.kpv.lesson.api.OpenWeatherApi
import com.kpv.lesson.ui.state.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException

class WeatherViewModel : ViewModel() {
    private val _uiState = MutableStateFlow<UiState>(UiState.Idle)
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    private val API_KEY = BuildConfig.API_KEY

    private val retrofit = Retrofit.Builder()
        .baseUrl("https://api.openweathermap.org/data/2.5/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val api = retrofit.create(OpenWeatherApi::class.java)

    fun searchWeather(city: String) {
        if (city.isBlank()) return
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            try {
                val response = api.getWeather(city, API_KEY)
                _uiState.value = UiState.Success(response)
            } catch (e: Exception) {
                val errorMsg = when (e) {
                    is retrofit2.HttpException -> {
                        if (e.code() == 404) "Город не найден"
                        else "Ошибка сервера (${e.code()})"
                    }
                    is IOException -> "Нет соединения с интернетом"
                    else -> "Произошла ошибка: ${e.message}"
                }
                _uiState.value = UiState.Error(errorMsg)
            }
        }
    }
}