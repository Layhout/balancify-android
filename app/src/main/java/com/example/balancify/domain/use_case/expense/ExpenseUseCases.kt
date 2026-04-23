package com.example.balancify.domain.use_case.expense

data class ExpenseUseCases(
    val getExpenses: GetExpenses,
    val getExpenseDetail: GetExpenseDetail,
    val getExpensesForGroup: GetExpensesForGroup,
    val deleteExpense: DeleteExpense,
    val settleExpense: SettleExpense,
    val createExpense: CreateExpense,
)
