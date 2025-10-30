package com.kpv.lesson.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp

object DesignConstants {
    val PaddingForEdge = 25.dp

    val DistanceBetweenElementsNotes = 16.dp

    val DistanceBetweenPasswordAndButtonShow = 8.dp

    val FontSizeEmptyText: @Composable () -> TextUnit = { (LocalConfiguration.current.screenWidthDp * 0.01f).toSp() }
    val FontSizeNotEmptyText: @Composable () -> TextUnit = { (LocalConfiguration.current.screenWidthDp * 0.057f).toSp() }

    val FontColumnName: @Composable () -> TextUnit = {(LocalConfiguration.current.screenWidthDp * 0.065f).toSp()}

    @Composable
    private fun Float.toSp(): TextUnit {
        return with(LocalDensity.current) { this@toSp.dp.toSp() }
    }
}