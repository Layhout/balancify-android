package com.macrobytes.balancify.domain.model

import com.macrobytes.balancify.core.util.DateAsLongSerializer
import com.google.firebase.firestore.IgnoreExtraProperties
import com.google.firebase.firestore.ServerTimestamp
import kotlinx.serialization.Serializable
import java.util.Date
import kotlin.math.roundToInt

@IgnoreExtraProperties
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
    val group: ExpenseGroupModel? = null,
    val member: Map<String, ExpenseMemberModel> = emptyMap(),
    val memberIds: List<String> = emptyList(),
    val createdBy: UserModel = UserModel(),
    val paidBy: UserModel = UserModel(),
    val timelines: List<TimelineModel> = emptyList(),
) {
    fun getPayerName(localUserId: String? = ""): String {
        return if (paidBy.id == localUserId) "You"
        else paidBy.name
    }

    fun getSettlePercentage(): Int {
        var result: Int

        val total = member.values.sumOf { it.settledAmount }
        if (total == 0.0) return 0
        result = ((total / amount) * 100).roundToInt()

        return result
    }

    fun getSettlementStatus(): String {
        return if (getSettlePercentage() == 100) "Settled"
        else if (getSettlePercentage() > 100) "Overpaid"
        else "Paid"
    }

    fun getLocalUserOweAmount(id: String): Double {
        return member[id]?.amount ?: 0.0
    }

    fun getMembers(): List<ExpenseMemberModel> {
        return member.values.toList()
    }
}