package com.example.cursproject.retrofit

import com.example.cursproject.data.auth.Auth
import com.example.cursproject.data.auth.AuthRequest
import com.example.cursproject.data.list.UserData
import com.example.cursproject.data.list.Users
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface MainApi {

    @POST("auth")
    suspend fun auth(@Body authRequest: AuthRequest): Response<Auth>

    @Headers("Content-Type: application/json")
    @GET("user")
    suspend  fun getAllUsers(): Response<MutableList<UserData>>
    @Headers("Content-Type: application/json")

    @POST("user")
    suspend fun getAddUsers(@Body userData: UserData, @Header("Authorization") token: String?): Response<UserData>

    @PUT("user{id}")
    suspend fun updateUsers(@Path("id") id: String, @Body userData: UserData, @Header("Authorization") token: String?): Response<UserData>

}