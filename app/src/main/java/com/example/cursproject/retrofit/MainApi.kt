package com.example.cursproject.retrofit

import com.example.cursproject.data.CityResponse
import com.example.cursproject.data.auth.Auth
import com.example.cursproject.data.auth.AuthRequest
import com.example.cursproject.data.list.UserData
import com.example.cursproject.data.list.Users
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface MainApi {

    @Headers("Content-Type: application/json")
    @POST("auth")
    suspend fun auth(@Body authRequest: AuthRequest): Response<Auth>

    @Headers("Content-Type: application/json")
    @GET("auth")
    suspend  fun getAllUsers(): Response<Users>
    @Headers("Content-Type: application/json")
    @POST("auth")
    suspend fun getAddUsers(@Body userData: UserData, @Header("Authorization") token: String?): Response<UserData>

    @PUT("auth{id}")
    suspend fun updateUsers(@Path("id") id: String, @Body userData: UserData, @Header("Authorization") token: String?): Response<UserData>

    @POST("subscribe")
    fun subscribeToCityNotifications(@Query("id") cityId: Int): Call<Void>

}