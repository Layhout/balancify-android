package com.example.balancify.core.ext

import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue

fun formatAmountTextFieldValue(amount: String): TextFieldValue {
    return if (amount.isEmpty()) {
        TextFieldValue("")
    } else {
        val display = "$$amount"
        TextFieldValue(
            text = display,
            selection = TextRange(display.length)
        )
    }
}

fun TextFieldValue.formatAmountValueChange(): String? {
    val stripped = text.removePrefix("$")

    if (stripped.matches(Regex("^-?\\d*(\\.\\d{0,2})?$"))) {
        return if (stripped == ".") "0."
        else stripped
    }

    return null
}