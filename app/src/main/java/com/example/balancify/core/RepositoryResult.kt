package com.example.balancify.core

sealed interface RepositoryResult<out T> {
    data class Success<T>(val data: T) : RepositoryResult<T>
    data class Error(val throwable: Throwable) : RepositoryResult<Nothing>
}