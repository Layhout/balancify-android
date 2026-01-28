package com.example.balancify.core.ext

fun String.getInitials(): String {
    return this.trim()
        .split("\\s+".toRegex())
        .filter { it.isNotEmpty() }
        .map { it.first().uppercaseChar() }
        .joinToString(separator = "")
}