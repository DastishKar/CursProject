package com.example.cursproject.view

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.cursproject.R
import com.example.cursproject.data.auth.Auth
import com.example.cursproject.data.auth.AuthRequest
import com.example.cursproject.databinding.ActivityAuthBinding
import com.example.cursproject.retrofit.MainApi
import com.example.cursproject.viewModel.AuthViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.json.JSONObject
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class AuthActivity : AppCompatActivity() {
    private lateinit var mainApi: MainApi
    private lateinit var binding: ActivityAuthBinding
    private lateinit var viewModel: AuthViewModel
    private var currentUser: Auth? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAuthBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initRetrofit()
        viewModel = ViewModelProvider(this@AuthActivity)[AuthViewModel::class.java]
        viewModel.token.observe(this@AuthActivity, Observer { token ->

        })


        binding.apply {
            buttonlog.setOnClickListener {
                auth(
                    AuthRequest(
                        username.text.toString(),
                        password.text.toString()
                    )
                )
            }
            selectButton.setOnClickListener {
                val intent = Intent(this@AuthActivity, ServicesActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
    }


    private fun initRetrofit() {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY

        val client = OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl("https://dummyjson.com/").client(client)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create()).build()
        mainApi = retrofit.create(MainApi::class.java)
    }

    private fun auth(authRequest: AuthRequest) {
        CoroutineScope(Dispatchers.IO).launch {
            val response = mainApi.auth(authRequest)
            val message = response.errorBody()?.string()?.let {
                JSONObject(it).getString("message")
            }
            runOnUiThread {
                binding.error.text = message
                val user = response.body()
                if (user != null) {
                    currentUser = user
                    Toast.makeText(
                        this@AuthActivity,
                        "Welcome!",
                        Toast.LENGTH_SHORT)
                        .show()
                    binding.selectButton.visibility = View.VISIBLE
                    viewModel.token.value = user.token

                    CoroutineScope(Dispatchers.IO).launch {
                        try {
                          val intent = Intent(this@AuthActivity, ServicesActivity::class.java)
                            intent.putExtra("userToken", user.token)
                            intent.putExtra("userId", user.id)
                            startActivity(intent)

                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }
                }
            }
        }
    }





}