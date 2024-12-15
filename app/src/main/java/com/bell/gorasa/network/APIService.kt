package com.bell.gorasa.network

import com.bell.gorasa.database.Data
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface APIService {

    @GET("gorasa") // Sesuaikan endpoint API yang digunakan
    fun getAllMenu(): Call<List<Data>>

    @GET("gorasa/{id}")
    fun getMenuById(@Path("id") id: String): Call<Data>  // Mengambil data berdasarkan ID

    @POST("gorasa") // Sesuaikan endpoint API yang digunakan
    fun addMenu(@Body data: Data): Call<Data>  // Mengirimkan objek Data

    @POST("gorasa/{id}")
    fun updateMenu(@Path("id") id: String, @Body data: Data): Call<Data>  // Mengirimkan objek Data

    @DELETE("gorasa/{id}")
    fun deleteMenu(@Path("id") id: String): Call<Data>  // Menghapus data berdasarkan ID
}
