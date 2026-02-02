package com.example.balancify.core.constant

/**
 * Accepted = 'accepted',
 *   Pending = 'pending',
 *   Rejected = 'rejected',
 *   Unfriend = 'unfriend',
 *   Requesting = 'requesting',
 */

enum class FriendStatus(
    val value: String
) {
    PENDING("pending"),
    ACCEPTED("accepted"),
    REJECTED("rejected"),
    UNFRIEND("unfriend"),
    REQUESTING("requesting"),
}


