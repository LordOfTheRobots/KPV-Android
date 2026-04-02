package com.kpv.lesson.dto

data class WeatherResponse(
    val name: String,
    val sys: Sys,
    val main: Main,
    val wind: Wind,
    val weather: List<Weather>
) {
    data class Sys(val country: String)
    data class Main(val temp: Float, val feels_like: Float, val humidity: Int)
    data class Wind(val speed: Float)
    data class Weather(val description: String, val icon: String)
}