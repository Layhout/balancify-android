package com.example.balancify.domain.model

data class GroupMetadataModel(
    val groupId: String = "",
    val nameTrigrams: List<String> = emptyList(),
    val membersFlag: Map<String, Boolean> = emptyMap(),
)
