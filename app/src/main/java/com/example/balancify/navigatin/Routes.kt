package com.example.balancify.navigatin

import com.example.balancify.core.constant.SearchType
import kotlinx.serialization.Serializable

sealed interface Routes {
    @Serializable
    data object Login : Routes

    @Serializable
    data object Home : Routes

    @Serializable
    data object Friend : Routes

    @Serializable
    data object GroupFrom : Routes

    @Serializable
    data class Search(
        val type: SearchType,
    ) : Routes

}