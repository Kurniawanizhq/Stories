package com.eone.submission2_bpai.utils

import android.util.Patterns
import java.util.regex.Pattern

object Helper {
    fun String.isValidEmail(): Boolean {
        val pattern: Pattern = Patterns.EMAIL_ADDRESS
        return pattern.matcher(this).matches()
    }
}