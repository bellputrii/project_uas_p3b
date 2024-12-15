package com.bell.gorasa.database

import com.google.gson.annotations.SerializedName

data class Data(
    @SerializedName("_id")
    val id: String,
    @SerializedName("food_name")
    val foodname: String,
    @SerializedName("price")
    val price: String,
    @SerializedName("desc")
    val description: String
)