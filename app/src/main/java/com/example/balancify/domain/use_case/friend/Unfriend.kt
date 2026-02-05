package com.example.balancify.domain.use_case.friend

import com.example.balancify.domain.repository.FriendRepository

class Unfriend(private val repository: FriendRepository) {
    suspend operator fun invoke(id: String) = repository.unfriend(id)
}