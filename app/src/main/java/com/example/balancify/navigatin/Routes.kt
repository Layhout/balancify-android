package com.example.balancify.navigatin

import com.example.balancify.core.constant.SearchType
import com.example.balancify.domain.model.GroupModel
import kotlinx.serialization.Serializable

sealed interface Routes {
    @Serializable
    data object Login : Routes

    @Serializable
    data object Home : Routes

    @Serializable
    data object Friend : Routes

    @Serializable
    data class GroupFrom(
        val group: GroupModel? = null,
    ) : Routes

    @Serializable
    data class Search(
        val type: SearchType,
    ) : Routes

    @Serializable
    data class GroupDetail(
        val group: GroupModel,
    ) : Routes
}