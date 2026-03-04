package com.example.balancify.domain.model

import com.example.balancify.core.util.DateAsLongSerializer
import com.google.firebase.firestore.ServerTimestamp
import kotlinx.serialization.Serializable
import java.util.Date

/**
 * id: string
 *   name: string
 *   createdAt: FieldValue
 *   amount: number
 *   icon: string
 *   iconBgColor: string
 *   memberOption: MemberOption
 *   splitOption: SplitOption
 *   group: { id: string; name: string } | null
 *   member: Record<string, ExpenseMember>
 *   memberIds: string[]
 *   createdBy: User
 *   paidBy: User
 *   timelines: Timeline[]
 */

@Serializable
data class ExpenseModel(
    val id: String = "",
    val name: String = "",
    @Serializable(with = DateAsLongSerializer::class)
    @ServerTimestamp val createdAt: Date? = null,
    val amount: Double = 0.0,
    val icon: String = "",
    val iconBgColor: String = "",
    val memberOption: MemberOption = MemberOption.FRIEND,
    val splitOption: SplitOption = SplitOption.SPLIT_EQUALLY,
    val group: ExpenseGroupModel = ExpenseGroupModel(),
    val member: Map<String, ExpenseMemberModel> = emptyMap(),
    val memberIds: List<String> = emptyList(),
    val createdBy: UserModel = UserModel(),
    val paidBy: UserModel = UserModel(),
    val timelines: List<TimelineModel> = emptyList(),
)