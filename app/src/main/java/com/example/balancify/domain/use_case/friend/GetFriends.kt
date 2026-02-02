package com.example.balancify.domain.use_case.friend

import com.example.balancify.domain.repository.FriendRepository
import com.google.firebase.firestore.DocumentSnapshot

class GetFriends(private val repository: FriendRepository) {
    suspend operator fun invoke(lastDoc: DocumentSnapshot?) = repository.getFriends(lastDoc)
}