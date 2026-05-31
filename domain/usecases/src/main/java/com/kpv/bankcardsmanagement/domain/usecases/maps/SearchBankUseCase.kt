package com.kpv.bankcardsmanagement.domain.usecases.maps

import com.kpv.bankcardsmanagement.domain.core.model.GeocodeResult
import com.kpv.bankcardsmanagement.domain.repository.MapsRepository
import javax.inject.Inject

class SearchBanksUseCase @Inject constructor(
    private val repository: MapsRepository
) {
    suspend operator fun invoke(lat: Double, lon: Double): Result<List<GeocodeResult>> =
        repository.searchNearbyBanks(lat, lon)
}