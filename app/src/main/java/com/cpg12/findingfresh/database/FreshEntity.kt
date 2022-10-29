package com.cpg12.findingfresh.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "my_table")
data class FreshEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    @ColumnInfo(name = "name") val name: String?,
    @ColumnInfo(name = "address") val address: String?,
    @ColumnInfo(name = "phone") val phone: String?,
    @ColumnInfo(name = "category") val category: String?,
    @ColumnInfo(name = "image") val image: String?,
)