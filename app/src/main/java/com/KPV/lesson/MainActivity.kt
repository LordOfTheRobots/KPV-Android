package com.kpv.lesson

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.res.stringResource
import java.lang.Class

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val message =  remember{ mutableStateOf("") }
            Column { Input(message)
                ActivityTrans(
                    stringResource(R.string.second_activity),
                    this@MainActivity,
                    message.value,
                    SecondActivity::class.java,
                    ::IntentConfigDefault)
                ActivityTrans(
                    stringResource(R.string.third_activity),
                    this@MainActivity,
                    message.value,
                    ThirdActivity::class.java,
                    ::IntentConfigDefault) }
        }
    }

}


@Composable
fun ActivityTrans(
    text: String,
    packageContext: ComponentActivity,
    message: String?,
    clazz : Class<*>,
    intentConfig:(input:Intent)->Unit
){
    val str: String = stringResource(R.string.screen_trans)
    Button(
        onClick = {
            val intent = Intent(packageContext, clazz)
            intentConfig(intent)
            intent.putExtra("message", message)
            packageContext.startActivity(intent)
                  },
        content = { Row{Text(text = str + text)} }
    )
}

@Composable
fun Input(
    message: MutableState<String>
){
    TextField(
        value = message.value,
        onValueChange = {message.value=it},
        placeholder = {Text(text = stringResource(R.string.enter_smth))}
    )
}

fun IntentConfigDefault(intent:Intent){
}

