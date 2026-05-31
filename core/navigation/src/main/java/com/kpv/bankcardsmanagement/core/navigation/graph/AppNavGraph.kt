package com.kpv.bankcardsmanagement.core.navigation.graph

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.kpv.bankcardsmanagement.core.navigation.AuthRoute
import com.kpv.bankcardsmanagement.core.navigation.CardsRoute
import com.kpv.bankcardsmanagement.core.navigation.MapRoute
import com.kpv.bankcardsmanagement.core.navigation.SettingsRoute
import com.kpv.bankcardsmanagement.core.navigation.TransactionsRoute
import com.kpv.bankcardsmanagement.core.navigation.TransfersRoute

@Composable
fun AppNavGraph(
    navController: NavHostController,
    onAuthScreen: @Composable () -> Unit,
    onCardScreen: @Composable () -> Unit,
    onTransactionScreen: @Composable () -> Unit,
    onTransferScreen: @Composable () ->Unit,
    onSettingsRoute:@Composable () -> Unit,
    onMapsRoute: @Composable () -> Unit,
    startDestination: Any = AuthRoute
) {
    NavHost(navController = navController, startDestination = startDestination) {
        composable<AuthRoute> { onAuthScreen() }
        composable<CardsRoute> { onCardScreen() }
        composable<TransactionsRoute> {onTransactionScreen()}
        composable<TransfersRoute> { onTransferScreen()}
        composable<SettingsRoute> { onSettingsRoute() }
        composable<MapRoute>{onMapsRoute()}
    }
}