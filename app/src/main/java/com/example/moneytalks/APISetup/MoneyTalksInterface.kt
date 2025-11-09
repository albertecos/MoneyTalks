package com.example.moneytalks.APISetup

import com.example.moneytalks.DataClasses.Group
import retrofit2.http.GET
import retrofit2.http.Query
import java.lang.reflect.Member

interface MoneyTalksInterface {
    @GET("groups")
    suspend fun getGroups(@Query("memberId") memberId: String): List<Group>
}