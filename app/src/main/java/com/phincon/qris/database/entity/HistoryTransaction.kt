package com.phincon.qris.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class HistoryTransaction(
    @PrimaryKey val uid: Int?,
    @ColumnInfo(name = "user") val user : String?,
    @ColumnInfo(name = "bankName") val bankName : String?,
    @ColumnInfo(name = "idTransaction") val idTransaction: String?,
    @ColumnInfo(name = "merchantName") val merchantName: String?,
    @ColumnInfo(name = "nominal") val nominal: Int?,
    @ColumnInfo(name = "date") val date: String?
)
