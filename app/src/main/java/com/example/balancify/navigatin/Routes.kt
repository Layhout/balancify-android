package com.example.balancify.navigatin

import kotlinx.serialization.Serializable

sealed interface Routes {
    @Serializable
    data object Intro : Routes

    @Serializable
    data object Home : Routes
}