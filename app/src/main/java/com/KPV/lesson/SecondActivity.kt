package com.kpv.lesson

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp


class SecondActivity: ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Column{
                Spacer(modifier = Modifier.padding(30.dp))
                MessageShow(intent.getStringExtra("message"), stringResource(R.string.second_activity))
                ActivityTrans(stringResource(R.string.third_activity),
                    this@SecondActivity,
                    intent.getStringExtra("message"),
                    ThirdActivity::class.java,
                    ::IntentConfigDefault
                )
                ActivityTrans(stringResource(R.string.first_activity),
                    this@SecondActivity,
                    intent.getStringExtra("message"),
                    MainActivity::class.java,
                    ::IntentConfigDefault
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

