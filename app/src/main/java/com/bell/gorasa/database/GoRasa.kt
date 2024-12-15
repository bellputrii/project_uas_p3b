package com.bell.gorasa.database

import androidx.room.Entity
import com.google.gson.annotations.SerializedName

@Entity(tableName = "gorasa_table") // Nama tabel bisa disesuaikan
data class GoRasa(
    @SerializedName("data")
    val data: List<Data>
)
