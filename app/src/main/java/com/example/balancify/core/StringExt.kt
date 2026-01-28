package com.example.balancify.core

fun String.getInitials(): String {
    return this.trim()
        .split("\\s+".toRegex())
        .filter { it.isNotEmpty() }
        .map { it.first().uppercaseChar() }
        .joinToString(separator = "")
}
