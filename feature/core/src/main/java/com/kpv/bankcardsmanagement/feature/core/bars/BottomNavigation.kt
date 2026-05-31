package com.kpv.bankcardsmanagement.feature.core.bars

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ReceiptLong
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.Place
import androidx.compose.runtime.Composable

import androidx.compose.material3.*
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.kpv.bankcardsmanagement.core.navigation.*
import com.kpv.bankcardsmanagement.core.ui.theme.StringsObject
import com.kpv.bankcardsmanagement.core.ui.theme.UiText
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.SwapHoriz

sealed class BottomTab(
    val route: Any,
    val icon: ImageVector,
    val title: UiText
) {
    object Cards : BottomTab(CardsRoute, Icons.Default.CreditCard, StringsObject.navCards)
    object Transactions : BottomTab(TransactionsRoute,
        Icons.AutoMirrored.Filled.ReceiptLong, StringsObject.navTransactions)
    object Transfers : BottomTab(TransfersRoute, Icons.Default.SwapHoriz, StringsObject.transferTitle)
    object Map : BottomTab(MapRoute, Icons.Default.Place, StringsObject.navMap)
    object Settings : BottomTab(SettingsRoute, Icons.Default.Settings, StringsObject.navSettings)
}

val bottomTabs = listOf(BottomTab.Cards, BottomTab.Transactions, BottomTab.Transfers, BottomTab.Map, BottomTab.Settings)

@Composable
fun BottomNavigationBar(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    NavigationBar(
        modifier = modifier,
        containerColor = MaterialTheme.colorScheme.surface,
        contentColor = MaterialTheme.colorScheme.onSurface
    ) {
        bottomTabs.forEach { tab ->
            val isSelected = currentDestination?.hierarchy?.any {
                it.route == tab.route::class.qualifiedName
            } == true

            NavigationBarItem(
                selected = isSelected,
                onClick = {
                    navController.navigate(tab.route) {
                        popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                icon = { Icon(tab.icon, contentDescription = null) },
                label = { Text(tab.title.asString()) },
                alwaysShowLabel = false
            )
        }
    }
}