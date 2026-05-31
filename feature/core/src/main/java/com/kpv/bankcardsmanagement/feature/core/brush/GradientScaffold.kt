package com.kpv.bankcardsmanagement.feature.core.brush


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import com.kpv.bankcardsmanagement.feature.core.bars.TopBar


@Composable
fun <T : ViewModel> GradientScaffold(
    viewModel: T,
    bottomBar: @Composable () -> Unit,
    content: @Composable (T) -> Unit,
) {
    val gradientBrush = Brush.verticalGradient(
        colors = listOf(
            Color(0xFF0A2A5E),
            Color(0xFF0D47A1),
            Color(0xFF1565C0),
            Color(0xFF1976D2),
            Color(0xFF1E88E5),
            Color(0xFF2196F3),
            Color(0xFF42A5F5),
            Color(0xFF4FC3F7)
        )
    )

    Scaffold(
        containerColor = Color.Transparent,
        topBar = { TopBar() },
        bottomBar = {bottomBar()}
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(gradientBrush)
                .padding(padding)
        ) {
            content(viewModel)
        }
    }
}