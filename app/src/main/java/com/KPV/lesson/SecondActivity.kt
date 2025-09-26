package com.kpv.lesson

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource


class SecondActivity: ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Column{
                MessageShow(intent.getStringExtra("message"), stringResource(R.string.second_activity))
                ActivityTrans(stringResource(R.string.first_activity),
                    this@SecondActivity,
                    intent.getStringExtra("message"),
                    ThirdActivity::class.java,
                    ::IntentConfigDefault
                )
                ActivityTrans(stringResource(R.string.first_activity),
                    this@SecondActivity,
                    intent.getStringExtra("message"),
                    MainActivity::class.java,
                    ::IntentConfigFromSecondToFirstScreen
                )
            }

        }
    }
}

@Composable
fun MessageShow(
    str: String?,
    screenWord: String
){
    str?.ifEmpty { "$screenWord screen" }?.let { Text( text= it) }
}

fun IntentConfigFromSecondToFirstScreen(
    intent: Intent
){
    intent.apply{
        flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
    }
}
