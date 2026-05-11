package com.macrobytes.balancify.domain.use_case.user

import com.macrobytes.balancify.domain.model.UserModel
import com.macrobytes.balancify.domain.repository.UserRepository

class AddUser(
    private val repository: UserRepository
) {
    suspend operator fun invoke(user: UserModel) = repository.addUser(user)
}