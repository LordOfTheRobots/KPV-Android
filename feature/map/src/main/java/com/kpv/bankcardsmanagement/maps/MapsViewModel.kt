package com.kpv.bankcardsmanagement.maps

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kpv.bankcardsmanagement.core.ui.theme.StringsObject
import com.kpv.bankcardsmanagement.core.ui.theme.UiText
import com.kpv.bankcardsmanagement.domain.core.model.GeocodeResult
import com.kpv.bankcardsmanagement.domain.usecases.maps.GeocodeUseCase
import com.kpv.bankcardsmanagement.domain.usecases.maps.SearchBanksUseCase
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.CameraPosition
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.collections.firstOrNull

data class MapUiState(
    val searchQuery: String = "",
    val results: List<GeocodeResult> = emptyList(),
    val nearbyBanks: List<GeocodeResult> = emptyList(),
    val selectedPlace: GeocodeResult? = null,
    val isLoading: Boolean = false,
    val error: UiText? = null,
    val cameraPosition: CameraPosition = CameraPosition(
        Point(55.751244, 37.618423),
        10f,
        0f,
        0f
    )
)

class MapViewModel @Inject constructor(
    private val geocodeUseCase: GeocodeUseCase,
    private val searchBanksUseCase: SearchBanksUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(MapUiState())
    val state: StateFlow<MapUiState> = _state.asStateFlow()

    fun updateSearchQuery(query: String) = _state.update { currentState -> currentState.copy(searchQuery = query, error = null) }

    fun moveToLocation(lat: Double, lon: Double) {
        val currentZoom = _state.value.cameraPosition.zoom
        val newPos = CameraPosition(Point(lat, lon), currentZoom, 0f, 0f)
        _state.update { currentState -> currentState.copy(cameraPosition = newPos) }
    }

    fun updateZoom(zoom: Double) {
        val currentPos = _state.value.cameraPosition.target
        val newPos = CameraPosition(currentPos, zoom.toFloat(), 0f, 0f)
        _state.update { currentState -> currentState.copy(cameraPosition = newPos) }
    }

    fun search() {
        viewModelScope.launch {
            val query = _state.value.searchQuery.trim()
            if (query.isBlank()) return@launch

            _state.update { currentState -> currentState.copy(isLoading = true, error = null) }

            geocodeUseCase(query)
                .onSuccess { resultList: List<GeocodeResult> ->
                    _state.update { currentState ->
                        currentState.copy(results = resultList, isLoading = false)
                    }
                    resultList.firstOrNull()?.let { place ->
                        val newPos = CameraPosition(
                            Point(place.latitude, place.longitude),
                            _state.value.cameraPosition.zoom,
                            0f, 0f
                        )
                        _state.update { currentState ->
                            currentState.copy(cameraPosition = newPos)
                        }
                    }
                }
                .onFailure { exception ->
                    _state.update { currentState ->
                        currentState.copy(isLoading = false, error = mapError(exception))
                    }
                }
        }
    }

    fun loadNearbyBanks(lat: Double, lon: Double) {
        viewModelScope.launch {
            searchBanksUseCase(lat, lon)
                .onSuccess { bankList -> _state.update { currentState -> currentState.copy(nearbyBanks = bankList) } }
                .onFailure { }
        }
    }

    fun selectPlace(place: GeocodeResult) = _state.update { currentState -> currentState.copy(selectedPlace = place) }
    fun clearSelection() = _state.update { currentState -> currentState.copy(selectedPlace = null) }
    fun updateCamera(pos: CameraPosition) = _state.update { currentState -> currentState.copy(cameraPosition = pos) }
    fun dismissError() = _state.update { currentState -> currentState.copy(error = null) }

    private fun mapError(e: Throwable): UiText = StringsObject.errorLoadingMap
}