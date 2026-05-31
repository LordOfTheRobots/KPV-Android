package com.kpv.bankcardsmanagement.maps.ui

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Navigation
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Snackbar
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kpv.bankcardsmanagement.core.maps.BuildConfig
import com.kpv.bankcardsmanagement.core.maps.R
import com.kpv.bankcardsmanagement.core.ui.theme.StringsObject
import com.kpv.bankcardsmanagement.domain.core.model.GeocodeResult
import com.kpv.bankcardsmanagement.maps.MapViewModel
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.CameraPosition
import com.yandex.mapkit.map.MapObjectCollection
import com.yandex.mapkit.mapview.MapView
import com.yandex.runtime.image.ImageProvider


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MapScreen(viewModel: MapViewModel, modifier: Modifier = Modifier) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val sheetState = rememberModalBottomSheetState()

    val launchNavigation: () -> Unit = {
        state.selectedPlace?.let { place ->
            val uri = Uri.parse("geo:${place.latitude},${place.longitude}?q=${place.latitude},${place.longitude}")
            val intent = Intent(Intent.ACTION_VIEW, uri)
            if (intent.resolveActivity(context.packageManager) != null) {
                context.startActivity(intent)
            }
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF0A2A5E),
                        Color(0xFF0D47A1),
                        Color(0xFF1565C0),
                        Color(0xFF1976D2)
                    )
                )
            )
    ) {
        GlassSearchBar(
            query = state.searchQuery,
            onQueryChange = viewModel::updateSearchQuery,
            onSearch = viewModel::search,
            isLoading = state.isLoading,
            onClear = { viewModel.updateSearchQuery("") }
        )

        Box(modifier = Modifier.weight(1f)) {
            YandexMapView(
                context = context,
                cameraPosition = state.cameraPosition,
                markers = state.results + state.nearbyBanks,
                onMarkerClick = { place -> viewModel.selectPlace(place) },
                onCameraMove = { newPos ->
                    viewModel.updateCamera(newPos)
                    viewModel.loadNearbyBanks(newPos.target.latitude, newPos.target.longitude)
                },
                modifier = Modifier.fillMaxSize()
            )

            state.error?.let { err ->
                Box(
                    modifier = Modifier
                        .padding(12.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0xFF000000).copy(alpha = 0.3f))
                        .border(1.dp, Color(0xFFFFEB3B).copy(alpha = 0.5f), RoundedCornerShape(12.dp))
                        .padding(12.dp)
                ) {
                    Row(horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = err.asString(),
                            color = Color(0xFFFFEB3B),
                            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium)
                        )
                        TextButton(onClick = { viewModel.dismissError() }) {
                            Text("OK", color = Color.White, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }

        if (state.results.isNotEmpty() || state.nearbyBanks.isNotEmpty()) {
            GlassResultsList(
                results = state.results,
                banks = state.nearbyBanks,
                onResultClick = { place ->
                    viewModel.moveToLocation(place.latitude, place.longitude)
                    viewModel.selectPlace(place)
                }
            )
        }
    }

    if (state.selectedPlace != null) {
        ModalBottomSheet(
            onDismissRequest = { viewModel.clearSelection() },
            sheetState = sheetState,
            containerColor = Color(0xFF0D47A1).copy(alpha = 0.95f),
            tonalElevation = 0.dp
        ) {
            GlassBottomSheetContent(
                place = state.selectedPlace!!,
                onClose = { viewModel.clearSelection() },
                onNavigate = launchNavigation
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun GlassSearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    onSearch: () -> Unit,
    isLoading: Boolean,
    onClear: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(Color.White.copy(alpha = 0.12f))
            .border(1.dp, Color.White.copy(alpha = 0.25f), RoundedCornerShape(16.dp))
            .padding(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        OutlinedTextField(
            value = query,
            onValueChange = onQueryChange,
            label = { Text(StringsObject.searchHint.asString(), color = Color(0xFFB3E5FC)) },
            modifier = Modifier.weight(1f),
            singleLine = true,
            enabled = !isLoading,
            colors = glassTextFieldColors(),
            trailingIcon = {
                if (query.isNotBlank()) {
                    IconButton(onClick = onClear) {
                        Icon(Icons.Default.Clear, contentDescription = null, tint = Color.White)
                    }
                }
            }
        )
        Button(
            onClick = onSearch,
            enabled = !isLoading,
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.White,
                contentColor = Color(0xFF0D47A1),
                disabledContainerColor = Color.White.copy(alpha = 0.3f)
            ),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.height(48.dp)
        ) {
            if (isLoading) {
                CircularProgressIndicator(modifier = Modifier.size(20.dp), strokeWidth = 2.dp, color = Color(0xFF0D47A1))
            } else {
                Text(StringsObject.searchButton.asString(), fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
            }
        }
    }
}

@Composable
private fun GlassResultsList(
    results: List<GeocodeResult>,
    banks: List<GeocodeResult>,
    onResultClick: (GeocodeResult) -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(max = 220.dp),
        color = Color(0xFF0D47A1).copy(alpha = 0.85f),
        shadowElevation = 8.dp,
        shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)
    ) {
        LazyColumn(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            if (results.isNotEmpty()) {
                item {
                    Text(
                        text = StringsObject.searchResults.asString(),
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                        color = Color(0xFFB3E5FC),
                        modifier = Modifier.padding(horizontal = 4.dp, vertical = 4.dp)
                    )
                }
                items(results) { place ->
                    GlassSearchResultItem(place = place, onClick = { onResultClick(place) })
                }
            }
            if (banks.isNotEmpty()) {
                item {
                    Spacer(Modifier.height(4.dp))
                    Text(
                        text = StringsObject.nearbyBanks.asString(),
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                        color = Color(0xFFB3E5FC),
                        modifier = Modifier.padding(horizontal = 4.dp, vertical = 4.dp)
                    )
                }
                items(banks) { bank ->
                    GlassSearchResultItem(place = bank, onClick = { onResultClick(bank) }, isBank = true)
                }
            }
        }
    }
}
@Composable
private fun GlassSearchResultItem(place: GeocodeResult, onClick: () -> Unit, isBank: Boolean = false) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(Color.White.copy(alpha = 0.08f))
            .border(1.dp, Color.White.copy(alpha = 0.15f), RoundedCornerShape(12.dp))
            .clickable(onClick = onClick)
            .padding(12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(
                        if (isBank) Color(0xFF4CAF50).copy(alpha = 0.2f) else Color(0xFF1976D2).copy(alpha = 0.2f)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = if (isBank) Icons.Default.AccountBalance else Icons.Default.Place,
                    contentDescription = null,
                    tint = if (isBank) Color(0xFF4CAF50) else Color(0xFF1976D2),
                    modifier = Modifier.size(18.dp)
                )
            }
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = place.name,
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium),
                    color = Color.White,
                    maxLines = 1
                )
                Text(
                    text = place.description,
                    style = MaterialTheme.typography.bodySmall,
                    color = Color(0xFFB3E5FC).copy(alpha = 0.8f),
                    maxLines = 1
                )
            }
            Icon(
                imageVector = Icons.Default.Navigation,
                contentDescription = null,
                tint = Color(0xFFB3E5FC),
                modifier = Modifier.size(20.dp)
            )
        }
    }
}

@Composable
private fun GlassBottomSheetContent(place: GeocodeResult, onClose: () -> Unit, onNavigate: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = place.name,
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = Color.White
            )
            IconButton(onClick = onClose) {
                Icon(Icons.Default.Close, contentDescription = StringsObject.close.asString(), tint = Color.White)
            }
        }
        Spacer(Modifier.height(8.dp))
        Text(
            text = place.description,
            style = MaterialTheme.typography.bodyMedium,
            color = Color(0xFFB3E5FC)
        )
        Spacer(Modifier.height(20.dp))
        Button(
            onClick = onNavigate,
            modifier = Modifier.fillMaxWidth().height(48.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.White,
                contentColor = Color(0xFF0D47A1)
            ),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text(StringsObject.navigateTo.asString(), fontWeight = FontWeight.SemiBold)
        }
    }
}

@Composable
fun YandexMapView(
    context: Context,
    cameraPosition: CameraPosition,
    markers: List<GeocodeResult>,
    onMarkerClick: (GeocodeResult) -> Unit,
    onCameraMove: (CameraPosition) -> Unit,
    modifier: Modifier = Modifier
) {
    val mapView = remember { MapView(context) }

    LaunchedEffect(cameraPosition) {
        mapView.mapWindow.map.move(cameraPosition)
    }

    LaunchedEffect(markers) {
        val map = mapView.mapWindow.map
        map.mapObjects.clear()
        val collection = map.mapObjects.addCollection()
        markers.forEach { place ->
            addPlacemark(collection, place, context) { onMarkerClick(place) }
        }
    }

    LaunchedEffect(mapView) {
        mapView.mapWindow.map.addCameraListener { _, newCameraPosition, _, finished ->
            if (finished) onCameraMove(newCameraPosition)
        }
    }

    AndroidView(
        factory = { mapView },
        modifier = modifier,
        update = { }
    )
}

private fun addPlacemark(
    collection: MapObjectCollection,
    place: GeocodeResult,
    context: Context,
    onClick: () -> Unit
) {
    collection.addPlacemark().apply {
        geometry = Point(place.latitude, place.longitude)
        setIcon(ImageProvider.fromResource(context, R.drawable.ic_map_pin))
        addTapListener { _, _ ->
            onClick()
            true
        }
    }
}

@Composable
private fun glassTextFieldColors() = OutlinedTextFieldDefaults.colors(
    focusedContainerColor = Color.White.copy(alpha = 0.12f),
    unfocusedContainerColor = Color.White.copy(alpha = 0.08f),
    disabledContainerColor = Color.White.copy(alpha = 0.05f),
    focusedTextColor = Color.White,
    unfocusedTextColor = Color.White,
    disabledTextColor = Color.White.copy(alpha = 0.4f),
    focusedBorderColor = Color.White.copy(alpha = 0.5f),
    unfocusedBorderColor = Color.White.copy(alpha = 0.25f),
    disabledBorderColor = Color.White.copy(alpha = 0.1f),
    cursorColor = Color.White,
    focusedLabelColor = Color(0xFFB3E5FC),
    unfocusedLabelColor = Color(0xFFB3E5FC).copy(alpha = 0.7f),
    disabledLabelColor = Color.White.copy(alpha = 0.3f)
)