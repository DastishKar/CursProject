package com.example.cursproject.retrofit

import com.example.cursproject.data.getCity.CityResponse
import com.example.cursproject.data.auth.Auth
import com.example.cursproject.data.auth.AuthRequest
import com.example.cursproject.data.list.UserData
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface MainApi {
    @Headers("Content-Type: application/json")
    @POST("auth/login")
    suspend fun auth(@Body authRequest: AuthRequest): Response<Auth>

    @Headers("Content-Type: application/json")
    @GET("auth")
    suspend  fun getAllUsers(): Response<List<UserData>>

    @Headers("Content-Type: application/json")
    @POST("auth")
    suspend fun getAddUsers(@Body userData: UserData, @Header("Authorization") token: String?): Response<UserData>

    @PUT("auth{id}")
    suspend fun updateUsers(@Path("id") id: String, @Body userData: UserData, @Header("Authorization") token: String?): Response<UserData>
    //Логика сервера не позволяет обновлять данные через PUT апрос
    @Headers("Content-Type: application/json")
    @GET("users")
    suspend fun getUserData(@Header("Authorization") token: String): Response<CityResponse>

    @Headers("Content-Type: application/json")
    @GET("users")
    suspend fun getCityForUserWithToken(@Header("Authorization") token: String): Response<CityResponse>

    @Headers("Content-Type: application/json")
    @POST("users")
    suspend fun subscribeToCityTopic(@Query("city") city: String) : Response<CityResponse>

}