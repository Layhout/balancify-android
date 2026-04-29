package com.example.balancify.domain.use_case.expense

import com.example.balancify.core.ext.getTrigram
import com.example.balancify.domain.model.ExpenseMemberModel
import com.example.balancify.domain.model.ExpenseMetadataModel
import com.example.balancify.domain.model.ExpenseModel
import com.example.balancify.domain.model.TimelineModel
import com.example.balancify.domain.model.User
import com.example.balancify.domain.repository.ExpenseRepository
import com.example.balancify.domain.repository.UserRepository

class UpdateExpense(
    private val repository: ExpenseRepository,
    private val userRepository: UserRepository,
) {
    suspend operator fun invoke(
        id: String,
        expenseParam: ExpenseModel,
        members: List<ExpenseMemberModel>,
        previousPayer: User? = null,
    ): Result<Unit> {
        val userResult = userRepository.getLocalUser()

        if (userResult.isFailure) return Result.failure(
            userResult.exceptionOrNull()!!
        )

        val timelineCreateTime = System.currentTimeMillis()
        val timelines = listOf(
            TimelineModel(
                createdAt = timelineCreateTime,
                createdBy = expenseParam.paidBy,
                events = "Edited the expense"
            )
        ) + expenseParam.timelines

        var expense = expenseParam.copy(
            member = members.associateBy { it.id },
            memberIds = members.map { it.id },
            createdBy = userResult.getOrNull()!!,
            timelines = timelines,
        )

        val expenseMetadata = ExpenseMetadataModel(
            nameTrigrams = expense.name.getTrigram(),
            membersFlag = members.associate { it.id to true }
        )

        if (previousPayer?.id != expense.paidBy.id) {
            val newMemberMap = expense.member.toMutableMap()
            newMemberMap[expense.paidBy.id] = newMemberMap[expense.paidBy.id]!!.copy(
                settledAmount =
                    newMemberMap[expense.paidBy.id]!!.settledAmount +
                            newMemberMap[expense.paidBy.id]!!.amount
            )

            val prevPayerMember = newMemberMap[previousPayer!!.id]
            if (prevPayerMember != null) {
                newMemberMap[prevPayerMember.id] = newMemberMap[prevPayerMember.id]!!.copy(
                    settledAmount =
                        newMemberMap[prevPayerMember.id]!!.settledAmount -
                                newMemberMap[prevPayerMember.id]!!.amount
                )
            }

            expense = expense.copy(
                member = newMemberMap,
            )
        }

        return repository.updateExpense(
            id, expense, expenseMetadata
        )
    }
}