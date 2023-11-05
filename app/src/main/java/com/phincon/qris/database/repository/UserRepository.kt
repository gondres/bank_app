package com.phincon.qris.database.repository

import android.util.Log
import com.phincon.qris.database.AppDatabase
import com.phincon.qris.database.DatabaseStateEvent
import com.phincon.qris.database.entity.User
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class UserRepository @Inject constructor(private val appDatabase: AppDatabase) {

    suspend fun insertUser(user: User): DatabaseStateEvent {
        try {
            appDatabase.userDao().insertAll(user)
            Log.d("User Repo", "Sukses")
            return DatabaseStateEvent.Success("")
        } catch (e: Exception) {
            Log.d("User Repo", e.localizedMessage)
            return DatabaseStateEvent.Error(e.localizedMessage)
        }
    }

    suspend fun substractBalance(name: String, amount: Int): DatabaseStateEvent {
        try {
            appDatabase.userDao().subtractBalance(name, amount)
            Log.d("User Repo", "Sukses")
            return DatabaseStateEvent.Success("")
        } catch (e: Exception) {
            Log.d("User Repo", e.localizedMessage)
            return DatabaseStateEvent.Error(e.localizedMessage)
        }
    }

    fun getUserData(name: String): Flow<User?> {
        return appDatabase.userDao().getUserData(name)
    }
}