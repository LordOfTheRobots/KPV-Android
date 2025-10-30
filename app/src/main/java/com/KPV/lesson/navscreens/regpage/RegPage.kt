package com.kpv.lesson.navscreens.regpage

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.navigation.NavHostController
import com.kpv.lesson.utils.CreateURL
import com.kpv.lesson.utils.Input
import com.kpv.lesson.navigation.Keys
import com.kpv.lesson.R
import com.kpv.lesson.entities.UserData
import com.kpv.lesson.utils.Validator
import com.kpv.lesson.navigation.NavigationIds
import com.kpv.lesson.ui.theme.DesignConstants

@Composable
fun RegPage(
    navController: NavHostController,
    email: MutableState<String>
){
    val password = remember { mutableStateOf("") }
    val showPassword = remember { mutableStateOf(true) }
    val emailInvalid: MutableState<String> = remember { mutableStateOf("")}
    val passwordInvalid: MutableState<String> = remember { mutableStateOf("")}
    Surface {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .windowInsetsPadding(WindowInsets.systemBars),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Spacer(modifier = Modifier.padding(DesignConstants.PaddingForEdge))
                Input(email, R.string.enter_email, emailInvalid, KeyboardType.Email)
                InvalidsText(emailInvalid.value)
                InputPassword(password, passwordInvalid, showPassword)
                InvalidsText(passwordInvalid.value)
            }
            Confirm(email,
                password,
                passwordInvalid,
                emailInvalid,
                navController)
        }
    }
}

@Composable
private fun InvalidsText(
    message: String
){
    Text(
        text = message,
        fontSize = if (message.isNotEmpty()) DesignConstants.FontSizeNotEmptyText()
        else DesignConstants.FontSizeEmptyText()
    )
}


@Composable
private fun InputPassword(
    password: MutableState<String>,
    exceptionString: MutableState<String>,
    showPassword: MutableState<Boolean>
){
    Row{
        TextField(
            value = password.value,
            onValueChange = {
                password.value = it
                exceptionString.value = ""},
            placeholder = { Text(text = stringResource(R.string.enter_password)) },
            visualTransformation = if (showPassword.value) PasswordVisualTransformation() else VisualTransformation.None,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            modifier = Modifier.focusRequester(FocusRequester())
        )
        Spacer(modifier = Modifier.padding(DesignConstants.DistanceBetweenPasswordAndButtonShow))
        Button(
            onClick = { if (showPassword.value) showPassword.value = false else showPassword.value = true },
            content = {
                Text(
                text = stringResource(R.string.show_password),
                textAlign = TextAlign.Center)
            }
        )
    }
}

@Composable
private fun Confirm(
    email: MutableState<String>,
    password: MutableState<String>,
    exceptionPassword: MutableState<String>,
    exceptionEmail: MutableState<String>,
    navController: NavHostController
){
    val notEmailText = stringResource(R.string.not_email)
    val passwordInvalids = Validator.Companion.validatePasswordWithDetails(password.value)
    Button(
        modifier = Modifier.fillMaxWidth(),
        onClick = {
            try{
                UserData(email.value, password.value)
                navController.navigate(CreateURL(
                    NavigationIds.NOTES_LIST.name,
                    ArrayList(listOf(
                        "${Keys.NotesListArgs.email}=${email.value}"
                    ))
                ))
            }
            catch (e: Exception) {
                if (!Validator.Companion.validateEmail(email.value)){
                    exceptionEmail.value = notEmailText
                }
                exceptionPassword.value = passwordInvalids
                email.value = ""
                password.value = ""
            }
                  },
        content = {Text(stringResource(R.string.confirm))},
    )
}