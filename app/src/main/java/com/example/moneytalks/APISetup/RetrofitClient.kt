package com.example.moneytalks.APISetup

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    private val retrofit by lazy{
        Retrofit.Builder()
            .baseUrl("http://10.0.2.2:3000/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val api: MoneyTalksInterface by lazy {
        retrofit.create(MoneyTalksInterface::class.java)
    }
}