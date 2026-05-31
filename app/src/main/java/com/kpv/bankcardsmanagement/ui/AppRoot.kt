package com.kpv.bankcardsmanagement.ui

import com.kpv.bankcardsmanagement.auth.view.AuthScreen
import com.kpv.bankcardsmanagement.core.navigation.graph.AppNavGraph
import com.kpv.bankcardsmanagement.feature.core.brush.GradientScaffold

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.kpv.bankcardsmanagement.auth.viewmodel.AuthViewModel
import com.kpv.bankcardsmanagement.core.navigation.AuthRoute
import androidx.lifecycle.ViewModelProvider
import com.kpv.bankcardsmanagement.cards.ui.CardsScreen
import com.kpv.bankcardsmanagement.core.navigation.CardsRoute
import com.kpv.bankcardsmanagement.feature.cards.viewmodel.CardsViewModel
import com.kpv.bankcardsmanagement.feature.core.bars.BottomNavigationBar
import com.kpv.bankcardsmanagement.maps.MapViewModel
import com.kpv.bankcardsmanagement.maps.ui.MapScreen
import com.kpv.settings.SettingsViewModel
import com.kpv.settings.ui.SettingsScreen
import com.kpv.transactions.TransactionsViewModel
import com.kpv.transactions.ui.TransactionsScreen
import com.kpv.transfer.TransfersViewModel
import com.kpv.transfer.ui.TransfersScreen

@Composable
fun AppRoot(
    isAuthenticated: Boolean,
    viewModelFactory: ViewModelProvider.Factory
) {
    val navController = rememberNavController()

    AppNavGraph(

        navController = navController,
        startDestination = if (isAuthenticated) CardsRoute else AuthRoute,
        onAuthScreen = {
            val authViewModel: AuthViewModel = viewModel(factory = viewModelFactory)
            GradientScaffold(authViewModel, { BottomNavigationBar(navController) }) {
                AuthScreen(
                    viewModel = authViewModel,
                    onNavigateToMain = {
                        navController.navigate(CardsRoute) {
                            popUpTo(AuthRoute) { inclusive = true }
                        }
                    }
                )
            }

        },
        onCardScreen = {
            val cardViewModel: CardsViewModel = viewModel(factory = viewModelFactory)
            GradientScaffold(cardViewModel, { BottomNavigationBar(navController) }) {
                CardsScreen(
                    viewModel = cardViewModel,
                )
            }
        },
        onTransactionScreen = {
            val transactionsViewModel: TransactionsViewModel = viewModel(factory = viewModelFactory)
            GradientScaffold(transactionsViewModel, { BottomNavigationBar(navController) }) {
                TransactionsScreen(transactionsViewModel)
            }
        },
        onTransferScreen = @Composable {
            val transfersViewModel: TransfersViewModel = viewModel(factory = viewModelFactory)
            GradientScaffold(viewModel = transfersViewModel, { BottomNavigationBar(navController) }) {
                TransfersScreen(viewModel = transfersViewModel)
            }
        },
        onSettingsRoute = {val settingsViewModel: SettingsViewModel = viewModel(factory = viewModelFactory)
            GradientScaffold(settingsViewModel, { BottomNavigationBar(navController) }) {
                SettingsScreen(viewModel = settingsViewModel,
                                onLogout = {navController.navigate(AuthRoute)})
            }
        },
        onMapsRoute = {val mapsViewModel: MapViewModel = viewModel(factory = viewModelFactory)
            GradientScaffold(mapsViewModel, { BottomNavigationBar(navController) }) {
                MapScreen(viewModel = mapsViewModel)
            }
        }
    )

}