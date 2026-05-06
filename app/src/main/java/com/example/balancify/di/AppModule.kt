package com.example.balancify.di

import com.example.balancify.MainViewModel
import com.example.balancify.core.manager.GlobalAppStateManager
import com.example.balancify.data.data_source.dashboard.DashboardRemoteDataSource
import com.example.balancify.data.data_source.dashboard.DashboardRemoteDataSourceImp
import com.example.balancify.data.data_source.expense.ExpenseRemoteDataSource
import com.example.balancify.data.data_source.expense.ExpenseRemoteDataSourceImp
import com.example.balancify.data.data_source.friend.FriendRemoteDataSource
import com.example.balancify.data.data_source.friend.FriendRemoteDataSourceImp
import com.example.balancify.data.data_source.group.GroupRemoteDataSource
import com.example.balancify.data.data_source.group.GroupRemoteDataSourceImp
import com.example.balancify.data.data_source.notification.NotificationRemoteDataSource
import com.example.balancify.data.data_source.notification.NotificationRemoteDataSourceImp
import com.example.balancify.data.data_source.user.UserLocalDataSource
import com.example.balancify.data.data_source.user.UserLocalDataSourceImp
import com.example.balancify.data.data_source.user.UserRemoteDataSource
import com.example.balancify.data.data_source.user.UserRemoteDataSourceImp
import com.example.balancify.data.repository.DashboardRepositoryImp
import com.example.balancify.data.repository.ExpenseRepositoryImp
import com.example.balancify.data.repository.FriendRepositoryImp
import com.example.balancify.data.repository.GroupRepositoryImp
import com.example.balancify.data.repository.NotificationRepositoryImp
import com.example.balancify.data.repository.UserRepositoryImp
import com.example.balancify.domain.repository.DashboardRepository
import com.example.balancify.domain.repository.ExpenseRepository
import com.example.balancify.domain.repository.FriendRepository
import com.example.balancify.domain.repository.GroupRepository
import com.example.balancify.domain.repository.NotificationRepository
import com.example.balancify.domain.repository.UserRepository
import com.example.balancify.domain.service.FriendEnricher
import com.example.balancify.domain.use_case.dashboard.DashboardUseCases
import com.example.balancify.domain.use_case.dashboard.GetDashboardData
import com.example.balancify.domain.use_case.expense.CreateExpense
import com.example.balancify.domain.use_case.expense.DeleteExpense
import com.example.balancify.domain.use_case.expense.ExpenseUseCases
import com.example.balancify.domain.use_case.expense.GetExpenseDetail
import com.example.balancify.domain.use_case.expense.GetExpenses
import com.example.balancify.domain.use_case.expense.GetExpensesForGroup
import com.example.balancify.domain.use_case.expense.SettleExpense
import com.example.balancify.domain.use_case.expense.UpdateExpense
import com.example.balancify.domain.use_case.friend.AcceptFriend
import com.example.balancify.domain.use_case.friend.AddFriendByEmail
import com.example.balancify.domain.use_case.friend.FriendUseCases
import com.example.balancify.domain.use_case.friend.GetFriends
import com.example.balancify.domain.use_case.friend.RejectFriend
import com.example.balancify.domain.use_case.friend.Unfriend
import com.example.balancify.domain.use_case.group.CreateGroup
import com.example.balancify.domain.use_case.group.DeleteGroup
import com.example.balancify.domain.use_case.group.GetGroupDetail
import com.example.balancify.domain.use_case.group.GetGroups
import com.example.balancify.domain.use_case.group.GroupUseCases
import com.example.balancify.domain.use_case.group.LeaveGroup
import com.example.balancify.domain.use_case.group.UpdateGroup
import com.example.balancify.domain.use_case.notification.CheckUnreadNotification
import com.example.balancify.domain.use_case.notification.GetNotifications
import com.example.balancify.domain.use_case.notification.NotificationUseCases
import com.example.balancify.domain.use_case.notification.ReadNotification
import com.example.balancify.domain.use_case.search.FindFriends
import com.example.balancify.domain.use_case.search.FindGroups
import com.example.balancify.domain.use_case.search.SearchUseCases
import com.example.balancify.domain.use_case.user.AddLocalUser
import com.example.balancify.domain.use_case.user.AddUser
import com.example.balancify.domain.use_case.user.GetLocalUser
import com.example.balancify.domain.use_case.user.GetUser
import com.example.balancify.domain.use_case.user.UserUseCases
import com.example.balancify.presentation.expense_detail.ExpenseDetailViewModel
import com.example.balancify.presentation.expense_form.ExpenseFormViewModel
import com.example.balancify.presentation.friend.FriendViewModel
import com.example.balancify.presentation.group_detail.GroupDetailViewModel
import com.example.balancify.presentation.group_form.GroupFormViewModel
import com.example.balancify.presentation.home.HomeViewModel
import com.example.balancify.presentation.home.component.account.AccountViewModel
import com.example.balancify.presentation.home.component.dashboard.DashboardViewModel
import com.example.balancify.presentation.home.component.expense.ExpenseViewModel
import com.example.balancify.presentation.home.component.group.GroupViewModel
import com.example.balancify.presentation.login.LoginViewModel
import com.example.balancify.presentation.notification.NotificationViewModel
import com.example.balancify.presentation.search.SearchViewModel
import com.example.balancify.service.AuthService
import com.example.balancify.service.DatabaseService
import com.example.balancify.service.LocalDatabaseService
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module

val appModule = module {
    /* Managers */
    singleOf(::GlobalAppStateManager)

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
    singleOf(::ExpenseRemoteDataSourceImp) bind ExpenseRemoteDataSource::class
    singleOf(::ExpenseRepositoryImp) bind ExpenseRepository::class
    singleOf(::NotificationRemoteDataSourceImp) bind NotificationRemoteDataSource::class
    singleOf(::NotificationRepositoryImp) bind NotificationRepository::class
    singleOf(::DashboardRemoteDataSourceImp) bind DashboardRemoteDataSource::class
    singleOf(::DashboardRepositoryImp) bind DashboardRepository::class

    /* Use Case Services */
    singleOf(::FriendEnricher)

    /* Use Cases */
    single {
        UserUseCases(
            getUser = GetUser(get()),
            addUser = AddUser(get()),
            getLocalUser = GetLocalUser(get()),
            addLocalUser = AddLocalUser(get()),
        )
    }
    single {
        FriendUseCases(
            getFriends = GetFriends(get(), get()),
            unfriend = Unfriend(get()),
            acceptFriend = AcceptFriend(get()),
            rejectFriend = RejectFriend(get()),
            addFriendByEmail = AddFriendByEmail(
                get(),
                get(),
                get()
            ),
        )
    }
    single {
        SearchUseCases(
            findFriends = FindFriends(get(), get()),
            findGroups = FindGroups(get(), get()),
        )
    }
    single {
        GroupUseCases(
            createGroup = CreateGroup(
                get(),
                get(),
                get()
            ),
            getGroups = GetGroups(get(), get()),
            getGroupDetail = GetGroupDetail(get(), get()),
            leaveGroup = LeaveGroup(get(), get()),
            deleteGroup = DeleteGroup(get()),
            updateGroup = UpdateGroup(get(), get()),
        )
    }
    single {
        ExpenseUseCases(
            getExpenses = GetExpenses(get(), get()),
            getExpenseDetail = GetExpenseDetail(get(), get()),
            getExpensesForGroup = GetExpensesForGroup(get()),
            deleteExpense = DeleteExpense(get()),
            settleExpense = SettleExpense(get(), get()),
            createExpense = CreateExpense(
                get(),
                get(),
                get()
            ),
            updateExpense = UpdateExpense(get(), get()),
        )
    }
    single {
        NotificationUseCases(
            getNotifications = GetNotifications(get()),
            checkUnreadNotification = CheckUnreadNotification(get()),
            readNotification = ReadNotification(get())
        )
    }
    single {
        DashboardUseCases(
            getDashboardData = GetDashboardData(get())
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
    viewModelOf(::ExpenseViewModel)
    viewModelOf(::ExpenseDetailViewModel)
    viewModelOf(::ExpenseFormViewModel)
    viewModelOf(::NotificationViewModel)
    viewModelOf(::DashboardViewModel)
}