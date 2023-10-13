package com.example.cursproject.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.cursproject.data.AuthRequest
import com.example.cursproject.databinding.ActivityAuthBinding
import com.example.cursproject.mainInterface.MainApi
import com.example.cursproject.viewModel.AuthViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.json.JSONObject
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class AuthActivity : AppCompatActivity() {
    private lateinit var mainApi: MainApi
    private lateinit var binding: ActivityAuthBinding
    private lateinit var viewModel: AuthViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAuthBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initRetrofit()
        viewModel = ViewModelProvider(this@AuthActivity)[AuthViewModel::class.java]

        viewModel.token.observe(this@AuthActivity, Observer { token ->
            initRetrofit()
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
                val intent = Intent(this@AuthActivity, MainActivity::class.java)
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
                binding.error.text = message.toString()
                val user = response.body()
                if (user != null) {
                    binding.selectButton.visibility = View.VISIBLE
                    viewModel.token.value = user.token
                }
            }
        }
    }
}