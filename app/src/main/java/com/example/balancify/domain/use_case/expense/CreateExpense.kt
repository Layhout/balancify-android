package com.example.balancify.domain.use_case.expense

import com.example.balancify.core.ext.getTrigram
import com.example.balancify.domain.model.ExpenseMemberModel
import com.example.balancify.domain.model.ExpenseMetadataModel
import com.example.balancify.domain.model.ExpenseModel
import com.example.balancify.domain.model.TimelineModel
import com.example.balancify.domain.repository.ExpenseRepository
import com.example.balancify.domain.repository.UserRepository
import java.util.UUID

class CreateExpense(
    private val repository: ExpenseRepository,
    private val userRepository: UserRepository,
) {
    suspend operator fun invoke(
        expenseParam: ExpenseModel,
        members: List<ExpenseMemberModel>,
    ): Result<Unit> {
        val userResult = userRepository.getLocalUser()

        if (userResult.isFailure) return Result.failure(
            userResult.exceptionOrNull()!!
        )

        val expenseId = UUID.randomUUID().toString()
        val timelineCreateTime = System.currentTimeMillis()
        val timelines = listOf(
            TimelineModel(
                createdAt = timelineCreateTime,
                createdBy = expenseParam.paidBy,
                events = "Paid for this expense"
            ),
            TimelineModel(
                createdAt = timelineCreateTime,
                createdBy = userResult.getOrNull()!!,
                events = "Created expense"
            )
        )
        val newMemberMap = members.associateBy { it.id }.toMutableMap()
        newMemberMap[expenseParam.paidBy.id] = newMemberMap[expenseParam.paidBy.id]!!.copy(
            settledAmount = newMemberMap[expenseParam.paidBy.id]!!.amount
        )

        val expense = expenseParam.copy(
            id = expenseId,
            member = newMemberMap,
            memberIds = members.map { it.id },
            createdBy = userResult.getOrNull()!!,
            timelines = timelines,
        )

        val expenseMetadata = ExpenseMetadataModel(
            expenseId = expenseId,
            nameTrigrams = expense.name.getTrigram(),
            membersFlag = members.associate { it.id to true }
        )

        return repository.createExpense(expense, expenseMetadata)
    }
}