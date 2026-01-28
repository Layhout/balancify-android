package com.example.balancify.presentation.home.component.account

import com.example.balancify.domain.model.UserModel

data class AccountState(
    val user: UserModel? = null,
    val isLogoutBottomSheetVisible: Boolean = false,
)
