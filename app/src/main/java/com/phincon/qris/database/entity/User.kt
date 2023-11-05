package com.phincon.qris.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class User(
    @PrimaryKey val uid: Int?,
    @ColumnInfo(name = "name") val name: String?,
    @ColumnInfo(name = "balance") val balance: Int?,
    @ColumnInfo(name = "createdDate") val createdDate: String?
)
