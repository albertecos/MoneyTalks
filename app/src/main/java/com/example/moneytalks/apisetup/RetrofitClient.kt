package com.example.moneytalks.apisetup

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    const val BASE_URL = "http://10.0.2.2:3000/"
    private val retrofit by lazy{
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val api: MoneyTalksInterface by lazy {
        retrofit.create(MoneyTalksInterface::class.java)
    }
}