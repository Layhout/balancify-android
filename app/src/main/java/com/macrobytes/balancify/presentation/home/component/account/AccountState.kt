package com.macrobytes.balancify.presentation.home.component.account

import com.macrobytes.balancify.domain.model.UserModel

data class AccountState(
    val user: UserModel? = null,
    val isLogoutBottomSheetVisible: Boolean = false,
    val isDeleteAccountBottomSheetVisible: Boolean = false,
)
