package com.macrobytes.balancify.domain.use_case.user

import com.macrobytes.balancify.domain.repository.UserRepository

class GetUser(
    private val repository: UserRepository
) {
    suspend operator fun invoke(id: String) = repository.getUser(id)
}