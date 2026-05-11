package com.macrobytes.balancify.presentation.search

import com.macrobytes.balancify.core.constant.SearchType
import com.macrobytes.balancify.domain.model.FoundItemModel
import com.google.firebase.firestore.DocumentSnapshot

data class SearchState(
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val canLoadMore: Boolean = true,
    val lastDoc: DocumentSnapshot? = null,
    val foundItems: List<FoundItemModel> = emptyList(),
    val searchType: SearchType = SearchType.FRIEND,
    val searchTerm: String = "",
)