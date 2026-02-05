package com.example.balancify.domain.use_case.friend

import com.example.balancify.domain.repository.FriendRepository

class RejectFriend(private val repository: FriendRepository) {
    suspend operator fun invoke(id: String) = repository.rejectFriend(id)
}