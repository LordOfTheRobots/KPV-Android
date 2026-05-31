package com.kpv.bankcardsmanagement.feature.core.bars

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.kpv.bankcardsmanagement.core.ui.theme.StringsObject
import com.kpv.bankcardsmanagement.core.ui.theme.UiText

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(
    modifier: Modifier = Modifier,
    title: UiText = StringsObject.appName
) {
    TopAppBar(
        modifier = modifier,
        title = {
            Text(
                text = title.asString(),
                color = Color.White,
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold,
                    letterSpacing = MaterialTheme.typography.titleLarge.letterSpacing
                )
            )
        },
        navigationIcon = {
            CardIcon(modifier = Modifier.size(32.dp))
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.Transparent,
            scrolledContainerColor = Color(0xFF0A2A5E).copy(alpha = 0.85f),
            titleContentColor = Color.White,
            navigationIconContentColor = Color.White,
            actionIconContentColor = Color.White
        )
    )
}

@Composable
private fun CardIcon(modifier: Modifier = Modifier) {
    val density = LocalDensity.current
    val dpToPx = { dp: Float -> with(density) { dp.dp.toPx() } }

    Canvas(modifier = modifier.padding(6.dp)) {
        val w = size.width
        val h = size.height
        val corner = dpToPx(3f)

        val cardPath = Path().apply {
            moveTo(corner, 0f)
            lineTo(w - corner, 0f)
            quadraticTo(w, 0f, w, corner)
            lineTo(w, h - corner)
            quadraticTo(w, h, w - corner, h)
            lineTo(corner, h)
            quadraticTo(0f, h, 0f, h - corner)
            lineTo(0f, corner)
            quadraticTo(0f, 0f, corner, 0f)
            close()
        }

        drawPath(cardPath, color = Color.White, style = Fill)
        drawPath(cardPath, color = Color(0xFF0D47A1), style = Stroke(width = dpToPx(1.5f)))

        drawRect(
            color = Color(0xFF0D47A1),
            topLeft = androidx.compose.ui.geometry.Offset(0f, h * 0.3f),
            size = androidx.compose.ui.geometry.Size(w, dpToPx(3f))
        )

        drawRect(
            color = Color(0xFFFFD54F),
            topLeft = androidx.compose.ui.geometry.Offset(dpToPx(3f), h * 0.55f),
            size = androidx.compose.ui.geometry.Size(dpToPx(6f), dpToPx(4f))
        )
    }
}