package com.example.balancify.presentation.home.component.expense

import com.example.balancify.domain.model.ExpenseModel
import com.example.balancify.domain.model.UserModel
import com.google.firebase.firestore.DocumentSnapshot

data class ExpenseState(
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val canLoadMore: Boolean = true,
    val localUser: UserModel? = null,
    val expenses: List<ExpenseModel> = emptyList(),
    val lastDoc: DocumentSnapshot? = null,
)
