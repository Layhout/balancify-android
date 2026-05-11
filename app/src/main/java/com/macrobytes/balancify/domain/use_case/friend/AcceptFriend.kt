package com.macrobytes.balancify.domain.use_case.friend

import com.macrobytes.balancify.domain.repository.FriendRepository

class AcceptFriend(private val repository: FriendRepository) {
    suspend operator fun invoke(id: String) = repository.acceptFriend(id)
}