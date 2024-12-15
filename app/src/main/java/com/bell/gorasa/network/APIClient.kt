package com.bell.gorasa.network

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object APIClient {
    fun getInstance(): APIService {
        // Membuat interceptor untuk logging HTTP request dan response
        val mHttpLoggingInterceptor = HttpLoggingInterceptor()
            .setLevel(HttpLoggingInterceptor.Level.BODY)

        // Membuat OkHttpClient dengan interceptor
        val mOkHttpClient = OkHttpClient
            .Builder()
            .addInterceptor(mHttpLoggingInterceptor)
            .build()

        // Membuat Retrofit instance
        val builder = Retrofit.Builder()
            .baseUrl("https://ppbo-api.vercel.app/fyPrG/") // URL dasar API Anda
            .addConverterFactory(GsonConverterFactory.create()) // Konverter untuk JSON
            .client(mOkHttpClient) // Menggunakan OkHttpClient yang telah dibuat
            .build()

        // Mengembalikan instance APIService
        return builder.create(APIService::class.java)
    }
}
