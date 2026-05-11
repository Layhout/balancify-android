package com.macrobytes.balancify.domain.use_case.user

data class UserUseCases(
    val getUser: GetUser,
    val addUser: AddUser,
    val getLocalUser: GetLocalUser,
    val addLocalUser: AddLocalUser
)
