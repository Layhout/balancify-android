package com.example.balancify.domain.use_case.friend

data class FriendUseCases(
    val getFriends: GetFriends,
    val unfriend: Unfriend,
    val acceptFriend: AcceptFriend,
    val rejectFriend: RejectFriend,
    val addFriendByEmail: AddFriendByEmail
)
