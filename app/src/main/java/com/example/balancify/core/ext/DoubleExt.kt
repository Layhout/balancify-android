package com.example.balancify.core.ext

import java.text.NumberFormat
import java.util.Locale

fun Double.getCurrencyFormatted(locale: Locale = Locale.US): String {
    val formatter = NumberFormat.getCurrencyInstance(locale)
    return formatter.format(this)
}