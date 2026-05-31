package com.kpv.bankcardsmanagement

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.kpv.bankcardsmanagement.data.repository.AuthRepositoryImpl
import com.kpv.bankcardsmanagement.di.ActivityComponent
import com.kpv.bankcardsmanagement.di.ViewModelFactory
import com.kpv.bankcardsmanagement.ui.AppRoot
import com.kpv.bankcardsmanagement.ui.theme.BankCardsManagementTheme
import javax.inject.Inject

class MainActivity : ComponentActivity() {
    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    @Inject
    lateinit var authRepository: AuthRepositoryImpl

    override fun onCreate(savedInstanceState: Bundle?) {
        val appComponent = (applicationContext as AppApplication).appComponent
        val activityComponent: ActivityComponent = appComponent.activityComponentFactory().create()
        activityComponent.inject(this)

        super.onCreate(savedInstanceState)
        val isAuthenticated = authRepository.isAuthorized()

        setContent {
            AppRoot(viewModelFactory = viewModelFactory, isAuthenticated = isAuthenticated)
        }
    }
}