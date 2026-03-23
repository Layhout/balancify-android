package com.example.balancify.domain.use_case.expense

import com.example.balancify.domain.repository.ExpenseRepository

class DeleteExpense(private val repository: ExpenseRepository) {
    suspend operator fun invoke(id: String) = repository.deleteExpense(id)
}