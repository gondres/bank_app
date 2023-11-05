package com.phincon.qris.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.phincon.qris.database.entity.User
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Query("SELECT * FROM user")
    fun getAll(): List<User>

    @Query("SELECT * FROM user WHERE uid IN (:userIds)")
    fun loadAllByIds(userIds: IntArray): List<User>

    @Insert
    suspend fun insertAll(vararg users: User)

    @Query("SELECT * FROM user WHERE name = (:name)")
    fun getUserData(name: String): Flow<User?>

    @Query("UPDATE User SET balance = balance - :amount WHERE name = :name")
    suspend fun subtractBalance(name: String, amount: Int)

    @Delete
    fun delete(user: User)
}