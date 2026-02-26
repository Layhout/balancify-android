package com.example.balancify.di

import com.example.balancify.MainViewModel
import com.example.balancify.data.data_source.friend.FriendRemoteDataSource
import com.example.balancify.data.data_source.friend.FriendRemoteDataSourceImp
import com.example.balancify.data.data_source.group.GroupRemoteDataSource
import com.example.balancify.data.data_source.group.GroupRemoteDataSourceImp
import com.example.balancify.data.data_source.user.UserLocalDataSource
import com.example.balancify.data.data_source.user.UserLocalDataSourceImp
import com.example.balancify.data.data_source.user.UserRemoteDataSource
import com.example.balancify.data.data_source.user.UserRemoteDataSourceImp
import com.example.balancify.data.repository.FriendRepositoryImp
import com.example.balancify.data.repository.GroupRepositoryImp
import com.example.balancify.data.repository.UserRepositoryImp
import com.example.balancify.domain.repository.FriendRepository
import com.example.balancify.domain.repository.GroupRepository
import com.example.balancify.domain.repository.UserRepository
import com.example.balancify.domain.service.FriendEnricher
import com.example.balancify.domain.use_case.friend.AcceptFriend
import com.example.balancify.domain.use_case.friend.AddFriendByEmail
import com.example.balancify.domain.use_case.friend.FriendUseCases
import com.example.balancify.domain.use_case.friend.GetFriends
import com.example.balancify.domain.use_case.friend.RejectFriend
import com.example.balancify.domain.use_case.friend.Unfriend
import com.example.balancify.domain.use_case.group.CreateGroup
import com.example.balancify.domain.use_case.group.GetGroupDetail
import com.example.balancify.domain.use_case.group.GetGroups
import com.example.balancify.domain.use_case.group.GroupUseCases
import com.example.balancify.domain.use_case.search.FindFriends
import com.example.balancify.domain.use_case.search.SearchUseCases
import com.example.balancify.domain.use_case.user.AddLocalUser
import com.example.balancify.domain.use_case.user.AddUser
import com.example.balancify.domain.use_case.user.GetLocalUser
import com.example.balancify.domain.use_case.user.GetUser
import com.example.balancify.domain.use_case.user.UserUseCases
import com.example.balancify.presentation.friend.FriendViewModel
import com.example.balancify.presentation.group_detail.GroupDetailViewModel
import com.example.balancify.presentation.group_form.GroupFormViewModel
import com.example.balancify.presentation.home.HomeViewModel
import com.example.balancify.presentation.home.component.account.AccountViewModel
import com.example.balancify.presentation.home.component.group.GroupViewModel
import com.example.balancify.presentation.login.LoginViewModel
import com.example.balancify.presentation.search.SearchViewModel
import com.example.balancify.service.AuthService
import com.example.balancify.service.DatabaseService
import com.example.balancify.service.LocalDatabaseService
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module

val appModule = module {
    /* Services */
    singleOf(::AuthService)
    singleOf(::DatabaseService)
    singleOf(::LocalDatabaseService)

    /* Data */
    singleOf(::UserRemoteDataSourceImp) bind UserRemoteDataSource::class
    singleOf(::UserLocalDataSourceImp) bind UserLocalDataSource::class
    singleOf(::UserRepositoryImp) bind UserRepository::class
    singleOf(::FriendRemoteDataSourceImp) bind FriendRemoteDataSource::class
    singleOf(::FriendRepositoryImp) bind FriendRepository::class
    singleOf(::GroupRemoteDataSourceImp) bind GroupRemoteDataSource::class
    singleOf(::GroupRepositoryImp) bind GroupRepository::class


    /* Use Case Services */
    singleOf(::FriendEnricher)

    /* Use Cases */
    single {
        UserUseCases(
            getUser = GetUser(get()),
            addUser = AddUser(get()),
            getLocalUser = GetLocalUser(get()),
            addLocalUser = AddLocalUser(get())
        )
    }
    single {
        FriendUseCases(
            getFriends = GetFriends(get(), get()),
            unfriend = Unfriend(get()),
            acceptFriend = AcceptFriend(get()),
            rejectFriend = RejectFriend(get()),
            addFriendByEmail = AddFriendByEmail(get(), get())
        )
    }
    single {
        SearchUseCases(
            findFriends = FindFriends(get(), get())
        )
    }
    single {
        GroupUseCases(
            createGroup = CreateGroup(get(), get()),
            getGroups = GetGroups(get(), get()),
            getGroupDetail = GetGroupDetail(get(), get())
        )
    }

    /* View Models */
    viewModelOf(::MainViewModel)
    viewModelOf(::LoginViewModel)
    viewModelOf(::HomeViewModel)
    viewModelOf(::AccountViewModel)
    viewModelOf(::FriendViewModel)
    viewModelOf(::SearchViewModel)
    viewModelOf(::GroupFormViewModel)
    viewModelOf(::GroupViewModel)
    viewModelOf(::GroupDetailViewModel)
}