package com.phincon.qris.database

sealed class DatabaseStateEvent {
    object Loading : DatabaseStateEvent()
    data class Success(val data: Any) : DatabaseStateEvent()
    data class Error(val message: String) : DatabaseStateEvent()
}
