package com.example.balancify.domain.model

import com.example.balancify.R

enum class ExpenseIcon(
    val value: String,
    val id: Int,
) {
    SUIT_CASE("suit-case", R.drawable.icon_suit_case),
    COFFEE("coffee", R.drawable.icon_coffee),
    FOOD("food", R.drawable.icon_food),
    STAR("star", R.drawable.icon_star),
    GIFT("gift", R.drawable.icon_gift),
    HEART("heart", R.drawable.icon_heart),
    BOOKMARK("bookmark", R.drawable.icon_bookmark);

    companion object {
        fun getIconIdFromValue(value: String): Int {
            return entries.find { it.value == value }?.id ?: STAR.id
        }
    }
}