package com.phincon.qris.database.repository

import android.util.Log
import com.phincon.qris.database.AppDatabase
import com.phincon.qris.database.DatabaseStateEvent
import com.phincon.qris.database.entity.HistoryTransaction
import com.phincon.qris.database.entity.User
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class HistoryRepository @Inject constructor(private val appDatabase: AppDatabase) {

    suspend fun insertHistory(history: HistoryTransaction): DatabaseStateEvent {
        try {
            appDatabase.historyTransactionDao().insertAll(history)
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

    fun getHistoryList(name: String): Flow<List<HistoryTransaction>?> {
        return appDatabase.historyTransactionDao().getAll(name)
    }
}