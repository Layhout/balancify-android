package com.example.balancify.domain.use_case.group

data class GroupUseCases(
    val createGroup: CreateGroup,
    val getGroups: GetGroups,
    val getGroupDetail: GetGroupDetail,
)