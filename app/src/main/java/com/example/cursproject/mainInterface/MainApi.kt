package com.example.cursproject.mainInterface

import com.example.cursproject.data.AuthRequest
import com.example.cursproject.data.UserData
import com.example.cursproject.data.Users
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface MainApi {

    @POST("auth/login")
    suspend fun auth(@Body authRequest: AuthRequest): UserData

    @GET("users")
    suspend fun getAllUsers(): Users

    @GET("auth/users/add")
    suspend fun getAddUsers(): UserData
}