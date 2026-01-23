package com.example.balancify.domain.model

import com.google.firebase.firestore.DocumentId

data class UserModel(
    @DocumentId val documentId: String = "",
    val id: String = "",
    val email: String = "",
    val imageUrl: String = "",
    val name: String = "",
    val notiToken: String = "",
    val profileBgColor: String = "",
    val referralCode: String = "",
)
