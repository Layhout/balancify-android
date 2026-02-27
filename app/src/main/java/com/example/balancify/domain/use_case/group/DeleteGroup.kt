package com.example.balancify.domain.use_case.group

import com.example.balancify.domain.repository.GroupRepository

class DeleteGroup(
    private val repository: GroupRepository,
) {
    suspend operator fun invoke(id: String) = repository.deleteGroup(id)
}