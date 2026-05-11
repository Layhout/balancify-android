package com.macrobytes.balancify.domain.use_case.user

import com.macrobytes.balancify.domain.repository.UserRepository

class GetLocalUser(
    private val repository: UserRepository
) {
    suspend operator fun invoke() = repository.getLocalUser()

}