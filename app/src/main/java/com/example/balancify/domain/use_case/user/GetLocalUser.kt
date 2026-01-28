package com.example.balancify.domain.use_case.user

import com.example.balancify.domain.repository.UserRepository

class GetLocalUser(
    private val repository: UserRepository
) {
    suspend operator fun invoke() = repository.getLocalUser()

}