package com.example.balancify.domain.use_case.user

import com.example.balancify.domain.repository.UserRepository

class GetUser(
    private val repository: UserRepository
) {
    suspend operator fun invoke(id: String) = repository.getUser(id)
}