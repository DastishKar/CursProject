package com.example.cursproject.data

import com.example.cursproject.data.arrayData.Address

data class UserData(
    val id: Int,
    val firstName: String,
    val lastName: String,
    val maidenName: String,
    val age: Int,
    val email: String,
    val username: String,
    val password: String,
    val address: Address,
    val birthDay: String,
    val token: String?
)
