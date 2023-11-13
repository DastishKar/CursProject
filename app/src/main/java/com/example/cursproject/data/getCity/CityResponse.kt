package com.example.cursproject.data.getCity

data class CityResponse(
    val users: List<UserData>
)

data class UserData(
    val id: Int,
    val firstName: String,
    val lastName: String,
    val maidenName: String,
    val address: Address
)

data class Address(
    val address: String,
    val city: String,
)

