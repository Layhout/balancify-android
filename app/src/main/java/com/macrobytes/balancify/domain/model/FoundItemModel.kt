package com.macrobytes.balancify.domain.model

data class FoundItemModel(
    val id: String = "",
    val imageUrl: String = "",
    val name: String = "",
    val data: FoundItemData? = null,
)