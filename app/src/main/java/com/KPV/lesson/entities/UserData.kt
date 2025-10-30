package com.kpv.lesson.entities

import com.kpv.lesson.utils.Validator

data class UserData(
    val email: String,
    val password: String
){
    init {
        if (!(Validator.Companion.validateEmail(email) && Validator.Companion.validatePassword(password))){
            throw Exception("Password or email is not right")
        }
    }
}