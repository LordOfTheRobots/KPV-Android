package com.kpv.lesson.utils

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType

@Composable
fun Input(
    message: MutableState<String>,
    stringId: Int,
    exceptionString: MutableState<String>,
    keyboardType: KeyboardType
) {
    TextField(
        value = message.value,
        onValueChange = {
            message.value = it
            exceptionString.value = ""
        },
        placeholder = { Text(text = stringResource(stringId)) },
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        modifier = Modifier.focusRequester(FocusRequester())
    )
}