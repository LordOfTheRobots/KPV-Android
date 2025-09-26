package com.kpv.lesson

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.content.IntentCompat

class ThirdActivity: ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Column{
                Spacer(modifier = Modifier.padding(30.dp))
                MessageShow(intent.getStringExtra("message"), stringResource(R.string.third_activity))
                ActivityTrans(stringResource(R.string.first_activity),
                    this@ThirdActivity,
                    intent.getStringExtra("message"),
                    MainActivity::class.java,
                    ::IntentConfigFromThirdToFirstScreen
                )
            }

        }
    }
}

fun IntentConfigFromThirdToFirstScreen(intent: Intent){
    intent.apply { flags = Intent.FLAG_ACTIVITY_REORDER_TO_FRONT }
}