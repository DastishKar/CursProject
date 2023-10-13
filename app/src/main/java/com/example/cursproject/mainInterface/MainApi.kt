package com.example.cursproject.mainInterface

import com.example.cursproject.data.AuthRequest
import com.example.cursproject.data.UserData
import com.example.cursproject.data.Users
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST

interface MainApi {

    @POST("auth/login")
    suspend fun auth(@Body authRequest: AuthRequest): Response<UserData>

    @Headers("Content-Type: application/json")
    @GET("users")
    suspend fun getAllUsers(@Header("Authorization") token: String): Users

    @Headers("Content-Type: application/json")
    @GET("auth/users/add")
    suspend fun getAddUsers(@Header("Authorization") token: String,
                            @Body data: Map<String, String>): Response<Users>
}