package com.phincon.qris.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.phincon.qris.database.dao.HistoryTransactionDao
import com.phincon.qris.database.dao.UserDao
import com.phincon.qris.database.entity.HistoryTransaction
import com.phincon.qris.database.entity.User

@Database(entities = [User::class, HistoryTransaction::class], version = 2)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun historyTransactionDao(): HistoryTransactionDao
}