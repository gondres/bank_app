package com.phincon.qris.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.phincon.qris.database.entity.HistoryTransaction
import com.phincon.qris.database.entity.User
import kotlinx.coroutines.flow.Flow

@Dao
interface HistoryTransactionDao {
    @Query("SELECT * FROM historytransaction where user= (:name)")
    fun getAll(name: String): Flow<List<HistoryTransaction>>

    @Insert
    suspend fun insertAll(vararg history: HistoryTransaction)

    @Delete
    fun delete(history: HistoryTransaction)
}