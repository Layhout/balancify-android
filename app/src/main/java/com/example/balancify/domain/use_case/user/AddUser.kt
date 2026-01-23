package com.example.balancify.domain.use_case.user

import com.example.balancify.domain.model.UserModel
import com.example.balancify.domain.repository.UserRepository

class AddUser(
    private val repository: UserRepository
) {
    suspend operator fun invoke(user: UserModel) = repository.addUser(user)
}