package com.rizqanmr.learningstory.util

import android.util.Patterns

object CommonFunction {
    fun isEmailValid(email: String): Boolean {
        val pattern = Patterns.EMAIL_ADDRESS
        return pattern.matcher(email).matches()
    }

}