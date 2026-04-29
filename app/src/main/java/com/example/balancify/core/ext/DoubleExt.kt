package com.example.balancify.core.ext

import java.text.NumberFormat
import java.util.Locale

fun Double.getCurrencyFormatted(locale: Locale = Locale.US): String {
    val formatter = NumberFormat.getCurrencyInstance(locale)
    return formatter.format(this)
}

fun Double.toCleanString(): String {
    if (this == 0.0) return ""
    return if (this % 1.0 == 0.0) toInt().toString() else toString()
}