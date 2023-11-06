package com.phincon.qris.service.repository

sealed class MainStateEvent<out T> {
    data class Loading(val isLoading: Boolean?) : MainStateEvent<Nothing>()
    data class Success<out T>(val data: T) : MainStateEvent<T>()
    data class Error(val statusCode: String, val message: String) : MainStateEvent<Nothing>()
    data class Exception(val errorMessage: String) : MainStateEvent<Nothing>()
}
