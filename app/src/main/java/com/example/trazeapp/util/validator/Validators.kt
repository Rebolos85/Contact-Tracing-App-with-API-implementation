package com.example.trazeapp.util.validator

import java.util.regex.Matcher
import java.util.regex.Pattern

// static once naka object singleton ni siya
object Validators {
    val PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#\$%!\\-_?&])(?=\\S+\$).{8,}"
    private const val NAME_PATTERN = "^[A-z][\\sA-z]*$"
    private val patternPassword: Pattern = Pattern.compile(PASSWORD_PATTERN)
    private val namePattern : Pattern =  Pattern.compile(NAME_PATTERN)
    fun isValidName(nameInput: String): Boolean {
        val matchName = namePattern.matcher(nameInput)
        return matchName.matches()
    }

    fun isValidPassword(password: String): Boolean {
        val matchPassword = patternPassword.matcher(password)
        return matchPassword.matches()
    }

    fun isValidPhoneNumber(phoneNumber: String): Boolean {
        return phoneNumber.matches("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#\$%^&+=])(?=\\\\S+\$).{4,}\$".toRegex())
    }


}