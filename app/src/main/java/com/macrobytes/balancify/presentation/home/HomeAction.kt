package com.macrobytes.balancify.presentation.home

sealed interface HomeAction {
    data object OnToggleFabClick : HomeAction
    data object OnCollectFlag : HomeAction
}