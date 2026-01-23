package com.example.balancify.di

import com.example.balancify.data.data_source.user.UserRemoteDataSource
import com.example.balancify.data.data_source.user.UserRemoteDataSourceImp
import com.example.balancify.data.repository.UserRepositoryImp
import com.example.balancify.domain.repository.UserRepository
import com.example.balancify.domain.use_case.user.AddUser
import com.example.balancify.domain.use_case.user.GetUser
import com.example.balancify.domain.use_case.user.UserUseCases
import com.example.balancify.presentation.login.LoginViewModel
import com.example.balancify.service.AuthService
import com.example.balancify.service.DatabaseService
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module

val appModule = module {
    singleOf(::AuthService)
    singleOf(::DatabaseService)

    singleOf(::UserRemoteDataSourceImp) bind UserRemoteDataSource::class
    singleOf(::UserRepositoryImp) bind UserRepository::class
    single {
        UserUseCases(
            getUser = GetUser(get()),
            addUser = AddUser(get()),
        )
    }

    viewModelOf(::LoginViewModel)
}