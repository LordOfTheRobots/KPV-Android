package com.kpv.lesson.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.kpv.lesson.R

enum class NavigationIds(
    val titleProvider: @Composable () -> String,
    val route: String
) {
    SETTINGS({ stringResource(R.string.settings) }, "SETTINGS"),
    EDIT({ stringResource(R.string.edit) }, "EDIT"),
    MESSAGES({ stringResource(R.string.messages) }, "MESSAGES");

    @Composable
    fun getTitle(): String = titleProvider()
}