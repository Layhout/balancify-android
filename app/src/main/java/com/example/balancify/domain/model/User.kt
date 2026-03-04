package com.example.balancify.domain.model

sealed interface User {
    val id: String
    val email: String
    val imageUrl: String
    val name: String
    val notiToken: String
    val profileBgColor: String
    val referralCode: String
    val subNoti: Boolean
}