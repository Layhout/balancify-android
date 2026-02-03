package com.example.balancify.core.ext

fun String.getInitials(): String {
    return this.trim()
        .split("\\s+".toRegex())
        .filter { it.isNotEmpty() }
        .map { it.first().uppercaseChar() }
        .joinToString(separator = "")
}

fun String.getTrigram(): List<String> {
    val lCase = this.lowercase()

    if (lCase.length < 3) return listOf(lCase)

    var result = listOf<String>()

    for (i in 0..lCase.length - 3) {
        result = result + lCase.substring(i, i + 3)
    }

    return result
}