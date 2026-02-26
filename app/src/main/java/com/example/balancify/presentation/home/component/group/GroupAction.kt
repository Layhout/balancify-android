package com.example.balancify.presentation.home.component.group

sealed interface GroupAction {
    data object OnRefresh : GroupAction
    data object OnLoadMore : GroupAction
    data object OnGroupClick : GroupAction
}