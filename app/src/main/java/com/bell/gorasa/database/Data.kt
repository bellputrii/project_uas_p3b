package com.bell.gorasa.database

import androidx.room.PrimaryKey
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName

data class Data(
    @PrimaryKey(autoGenerate = true)
    val id: String,
    @SerializedName("food_name")
    val foodname: String,
    @SerializedName("price")
    val price: String,
    @SerializedName("desc")
    val description: String
)
{
    fun toJson(): String {
        return Gson().toJson(this)
    }
}