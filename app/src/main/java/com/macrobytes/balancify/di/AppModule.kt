package com.macrobytes.balancify.di

import com.macrobytes.balancify.MainViewModel
import com.macrobytes.balancify.core.manager.GlobalAppStateManager
import com.macrobytes.balancify.data.data_source.dashboard.DashboardRemoteDataSource
import com.macrobytes.balancify.data.data_source.dashboard.DashboardRemoteDataSourceImp
import com.macrobytes.balancify.data.data_source.expense.ExpenseRemoteDataSource
import com.macrobytes.balancify.data.data_source.expense.ExpenseRemoteDataSourceImp
import com.macrobytes.balancify.data.data_source.friend.FriendRemoteDataSource
import com.macrobytes.balancify.data.data_source.friend.FriendRemoteDataSourceImp
import com.macrobytes.balancify.data.data_source.group.GroupRemoteDataSource
import com.macrobytes.balancify.data.data_source.group.GroupRemoteDataSourceImp
import com.macrobytes.balancify.data.data_source.notification.NotificationRemoteDataSource
import com.macrobytes.balancify.data.data_source.notification.NotificationRemoteDataSourceImp
import com.macrobytes.balancify.data.data_source.user.UserLocalDataSource
import com.macrobytes.balancify.data.data_source.user.UserLocalDataSourceImp
import com.macrobytes.balancify.data.data_source.user.UserRemoteDataSource
import com.macrobytes.balancify.data.data_source.user.UserRemoteDataSourceImp
import com.macrobytes.balancify.data.repository.DashboardRepositoryImp
import com.macrobytes.balancify.data.repository.ExpenseRepositoryImp
import com.macrobytes.balancify.data.repository.FriendRepositoryImp
import com.macrobytes.balancify.data.repository.GroupRepositoryImp
import com.macrobytes.balancify.data.repository.NotificationRepositoryImp
import com.macrobytes.balancify.data.repository.UserRepositoryImp
import com.macrobytes.balancify.domain.repository.DashboardRepository
import com.macrobytes.balancify.domain.repository.ExpenseRepository
import com.macrobytes.balancify.domain.repository.FriendRepository
import com.macrobytes.balancify.domain.repository.GroupRepository
import com.macrobytes.balancify.domain.repository.NotificationRepository
import com.macrobytes.balancify.domain.repository.UserRepository
import com.macrobytes.balancify.domain.service.FriendEnricher
import com.macrobytes.balancify.domain.use_case.dashboard.DashboardUseCases
import com.macrobytes.balancify.domain.use_case.dashboard.GetDashboardData
import com.macrobytes.balancify.domain.use_case.expense.CreateExpense
import com.macrobytes.balancify.domain.use_case.expense.DeleteExpense
import com.macrobytes.balancify.domain.use_case.expense.ExpenseUseCases
import com.macrobytes.balancify.domain.use_case.expense.GetExpenseDetail
import com.macrobytes.balancify.domain.use_case.expense.GetExpenses
import com.macrobytes.balancify.domain.use_case.expense.GetExpensesForGroup
import com.macrobytes.balancify.domain.use_case.expense.SettleExpense
import com.macrobytes.balancify.domain.use_case.expense.UpdateExpense
import com.macrobytes.balancify.domain.use_case.friend.AcceptFriend
import com.macrobytes.balancify.domain.use_case.friend.AddFriendByEmail
import com.macrobytes.balancify.domain.use_case.friend.FriendUseCases
import com.macrobytes.balancify.domain.use_case.friend.GetFriends
import com.macrobytes.balancify.domain.use_case.friend.RejectFriend
import com.macrobytes.balancify.domain.use_case.friend.Unfriend
import com.macrobytes.balancify.domain.use_case.group.CreateGroup
import com.macrobytes.balancify.domain.use_case.group.DeleteGroup
import com.macrobytes.balancify.domain.use_case.group.GetGroupDetail
import com.macrobytes.balancify.domain.use_case.group.GetGroups
import com.macrobytes.balancify.domain.use_case.group.GroupUseCases
import com.macrobytes.balancify.domain.use_case.group.LeaveGroup
import com.macrobytes.balancify.domain.use_case.group.UpdateGroup
import com.macrobytes.balancify.domain.use_case.notification.CheckUnreadNotification
import com.macrobytes.balancify.domain.use_case.notification.GetNotifications
import com.macrobytes.balancify.domain.use_case.notification.NotificationUseCases
import com.macrobytes.balancify.domain.use_case.notification.ReadNotification
import com.macrobytes.balancify.domain.use_case.search.FindFriends
import com.macrobytes.balancify.domain.use_case.search.FindGroups
import com.macrobytes.balancify.domain.use_case.search.SearchUseCases
import com.macrobytes.balancify.domain.use_case.user.AddLocalUser
import com.macrobytes.balancify.domain.use_case.user.AddUser
import com.macrobytes.balancify.domain.use_case.user.GetLocalUser
import com.macrobytes.balancify.domain.use_case.user.GetUser
import com.macrobytes.balancify.domain.use_case.user.UserUseCases
import com.macrobytes.balancify.presentation.expense_detail.ExpenseDetailViewModel
import com.macrobytes.balancify.presentation.expense_form.ExpenseFormViewModel
import com.macrobytes.balancify.presentation.friend.FriendViewModel
import com.macrobytes.balancify.presentation.group_detail.GroupDetailViewModel
import com.macrobytes.balancify.presentation.group_form.GroupFormViewModel
import com.macrobytes.balancify.presentation.home.HomeViewModel
import com.macrobytes.balancify.presentation.home.component.account.AccountViewModel
import com.macrobytes.balancify.presentation.home.component.dashboard.DashboardViewModel
import com.macrobytes.balancify.presentation.home.component.expense.ExpenseViewModel
import com.macrobytes.balancify.presentation.home.component.group.GroupViewModel
import com.macrobytes.balancify.presentation.login.LoginViewModel
import com.macrobytes.balancify.presentation.notification.NotificationViewModel
import com.macrobytes.balancify.presentation.search.SearchViewModel
import com.macrobytes.balancify.service.AuthService
import com.macrobytes.balancify.service.DatabaseService
import com.macrobytes.balancify.service.LocalDatabaseService
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