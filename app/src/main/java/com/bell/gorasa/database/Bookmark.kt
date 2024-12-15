package com.bell.gorasa.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "bookmarked_items")
data class Bookmark(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val foodname: String,
    val price: String,
    val description: String
)

