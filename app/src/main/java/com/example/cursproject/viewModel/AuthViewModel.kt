package com.example.cursproject.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.cursproject.data.auth.Auth

class AuthViewModel: ViewModel() {
    val token = MutableLiveData<String>()
}