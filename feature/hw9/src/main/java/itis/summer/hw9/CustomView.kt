package itis.summer.hw9

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.min
import kotlin.math.sin
import kotlin.math.sqrt

@Composable
fun InteractiveDiagramScreen() {
    var sectorCount by remember { mutableStateOf(4) }

    val colors = listOf(
        Color(0xFFE57373), Color(0xFF64B5F6), Color(0xFFFFD54F),
        Color(0xFF81C784), Color(0xFFBA68C8), Color(0xFF4DB6AC),
        Color(0xFFFF8A65), Color(0xFFA1887F), Color(0xFF7986CB),
        Color(0xFF4FC3F7), Color(0xFF26A69A), Color(0xFFD4E157)
    )

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Circular Diagram",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        Card(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Sectors: $sectorCount",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Medium
                )
                Spacer(modifier = Modifier.height(12.dp))
                Slider(
                    value = sectorCount.toFloat(),
                    onValueChange = { sectorCount = it.toInt() },
                    valueRange = 2f..12f,
                    steps = 9,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        CircularProgressDiagram(
            sectorCount = sectorCount,
            colors = colors,
            modifier = Modifier.size(320.dp),
            onSectorClick = { println("Clicked: $it") }
        )
    }
}

@Composable
fun CircularProgressDiagram(
    sectorCount: Int,
    colors: List<Color>,
    modifier: Modifier = Modifier,
    onSectorClick: ((Int) -> Unit)? = null
) {
    require(sectorCount in 2..12) { "Sectors must be 2-12" }
    require(colors.size >= 12) { "Provide at least 12 colors" }

    var activeSector by remember { mutableStateOf(-1) }

    val sectorAngle = 360f / sectorCount
    val drawSweep = sectorAngle * 1.25f
    val startOffset = 180f

    val scope = rememberCoroutineScope()
    val animatedValues = remember(sectorCount) {
        List(sectorCount) { Animatable(0.9f) }
    }

    LaunchedEffect(sectorCount) {
        animatedValues.forEach { it.animateTo(0.9f, tween(250)) }
    }

    val density = LocalDensity.current
    val centerTextPx = with(density) { 44.sp.toPx() }
    val labelPx = with(density) { 14.sp.toPx() }

    Canvas(
        modifier = modifier.pointerInput(sectorCount, sectorAngle, drawSweep, startOffset, activeSector) {
            detectTapGestures(
                onTap = { offset ->
                    val canvasSize = Size(size.width.toFloat(), size.height.toFloat())
                    val sector = getSectorFromOffset(
                        offset, canvasSize, sectorCount, sectorAngle, drawSweep, startOffset
                    )
                    if (sector != -1) {
                        onSectorClick?.invoke(sector)
                        activeSector = sector
                    } else {
                        activeSector = -1
                    }
                }
            )
        }
    ) {
        val centerX = size.width / 2
        val centerY = size.height / 2
        val radius = min(centerX, centerY) * 0.9f
        val strokeWidth = radius * (0.6f / (1f + sectorCount * 0.09f))

        for (i in 0 until sectorCount) {
            val startAngle = startOffset + (i * sectorAngle)
            val sweep = drawSweep * animatedValues[i].value.coerceIn(0f, 1f)
            val color = if (i == activeSector) lightenColor(colors[i % colors.size], 0.3f) else colors[i % colors.size]

            if (sweep > 0.05f) {
                drawArc(
                    color = color,
                    startAngle = startAngle,
                    sweepAngle = sweep,
                    useCenter = false,
                    style = Stroke(width = strokeWidth, cap = StrokeCap.Butt),
                    topLeft = Offset(centerX - radius, centerY - radius),
                    size = Size(radius * 2, radius * 2)
                )
            }
        }

        for (i in 0 until sectorCount) {
            val startAngle = startOffset + (i * sectorAngle)
            val sweep = drawSweep * animatedValues[i].value.coerceIn(0f, 1f)
            val endAngleDeg = startAngle + sweep
            val endAngleRad = Math.toRadians(endAngleDeg.toDouble())

            val endX = centerX + radius * cos(endAngleRad)
            val endY = centerY + radius * sin(endAngleRad)

            val color = if (i == activeSector) lightenColor(colors[i % colors.size], 0.3f) else colors[i % colors.size]

            drawCircle(
                color = color,
                radius = strokeWidth / 2f,
                center = Offset(endX.toFloat(), endY.toFloat())
            )
        }

        drawContext.canvas.nativeCanvas.apply {
            val countPaint = android.graphics.Paint().apply {
                color = android.graphics.Color.WHITE
                textSize = centerTextPx
                textAlign = android.graphics.Paint.Align.CENTER
                isFakeBoldText = true
                typeface = android.graphics.Typeface.DEFAULT_BOLD
                setShadowLayer(4f, 0f, 0f, android.graphics.Color.BLACK)
            }

            drawText(sectorCount.toString(), centerX, centerY - 6f, countPaint)

            val labelPaint = android.graphics.Paint().apply {
                color = android.graphics.Color.argb(220, 255, 255, 255)
                textSize = labelPx
                textAlign = android.graphics.Paint.Align.CENTER
                setShadowLayer(2f, 0f, 0f, android.graphics.Color.BLACK)
            }
            drawText("Steps", centerX, centerY + 26f, labelPaint)
        }
    }
}

private fun getSectorFromOffset(
    offset: Offset,
    canvasSize: Size,
    count: Int,
    sectorAngle: Float,
    drawSweep: Float,
    startOffset: Float
): Int {
    val centerX = canvasSize.width / 2
    val centerY = canvasSize.height / 2
    val radius = min(centerX, centerY) * 0.9f
    val strokeWidth = radius * (0.6f / (1f + count * 0.09f))

    val dx = offset.x - centerX
    val dy = offset.y - centerY
    val distance = sqrt(dx * dx + dy * dy)

    val innerR = radius - strokeWidth / 2 - 5f
    val outerR = radius + strokeWidth / 2 + 15f
    if (distance < innerR || distance > outerR) return -1

    var angleDeg = Math.toDegrees(atan2(dy, dx).toDouble()).toFloat()
    angleDeg += 180f
    if (angleDeg < 0) angleDeg += 360f
    val normalizedAngle = angleDeg % 360f

    for (i in 0 until count) {
        val start = (startOffset + i * sectorAngle) % 360f
        val end = (start + drawSweep) % 360f

        val inRange = if (start < end) {
            normalizedAngle in start..end
        } else {
            normalizedAngle >= start || normalizedAngle <= end
        }
        if (inRange) return i
    }
    return -1
}

private fun lightenColor(color: Color, factor: Float): Color {
    val hsl = FloatArray(3)
    android.graphics.Color.colorToHSV(
        android.graphics.Color.argb(
            (color.alpha * 255).toInt(),
            (color.red * 255).toInt(),
            (color.green * 255).toInt(),
            (color.blue * 255).toInt()
        ),
        hsl
    )
    hsl[2] = (hsl[2] + factor * (1 - hsl[2])).coerceIn(0f, 1f)
    return Color(android.graphics.Color.HSVToColor(hsl))
}