package com.kpv.lesson.utils

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.kpv.lesson.R

class Validator {
    companion object{
        private val UPPERCASE_REGEX: Regex = Regex(".*[A-Z].*")
        private val LOWERCASE_REGEX: Regex = Regex(".*[a-z].*")
        private val NUMBER_REGEX: Regex = Regex(".*\\d.*")
        private val SPECIAL_CHAR_REGEX: Regex =
            Regex(".*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?].*")
        private val ALL_REQ_FOR_PASSWORD: Regex =
            Regex("^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?]).+$")
        private val EMAIL_REQ: Regex =
            Regex("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")
        fun validatePassword(
            password: String
        ): Boolean {
            return ALL_REQ_FOR_PASSWORD.matches(password)
        }

        fun validateEmail(
            email: String
        ): Boolean {
            return EMAIL_REQ.matches(email)
        }
        @Composable
        fun validatePasswordWithDetails(
            password: String
        ): String{
            val passwordInvalids:ArrayList<String> = ArrayList()
            if (password.isEmpty()){
                passwordInvalids.add(stringResource(R.string.no_letters))
            }
            else{
                if (!UPPERCASE_REGEX
                        .matches(password)){
                    passwordInvalids.add(stringResource(R.string.no_uppercase))
                }
                if (password.length < 8){
                    passwordInvalids.add(stringResource(R.string.not_enough_letters))
                }
                if (!LOWERCASE_REGEX
                        .matches(password)){
                    passwordInvalids.add(stringResource(R.string.no_lowercase))
                }
                if (!NUMBER_REGEX
                        .matches(password)){
                    passwordInvalids.add(stringResource(R.string.no_numbers))
                }
                if (!SPECIAL_CHAR_REGEX
                        .matches(password)){
                    passwordInvalids.add(stringResource(R.string.no_spec_chars))
                }
            }
            return passwordInvalids.joinToString(", ")
    }

    }
}