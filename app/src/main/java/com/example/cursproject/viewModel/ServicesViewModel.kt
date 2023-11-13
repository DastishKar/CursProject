package com.example.cursproject.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.cursproject.retrofit.MainApi
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ServicesViewModel : ViewModel() {
  private val mainApi: MainApi = createMainApi()

  private val _userCity = MutableLiveData<String>()
  val userCity: LiveData<String>
    get() = _userCity

  // Метод для получения города пользователя из API
  fun fetchUserCity(userToken: String, userId: String?): LiveData<String> {
    val result = MutableLiveData<String>()
    CoroutineScope(Dispatchers.IO).launch {
      val city = getUserCityFromApi(userToken, userId)
      result.postValue(city)
    }
    return result
  }


  private suspend fun getUserCityFromApi(userToken: String, userId: String?): String {
    return try {
      val response = mainApi.getCityForUserWithToken(userToken)
      response.body()?.users?.find { it.id == userId?.toInt() }?.address?.city ?: "DefaultCity"
    } catch (e: Exception) {
      // Обработка ошибок
      ""
    }
  }

  fun createMainApi(): MainApi {
    val interceptor = HttpLoggingInterceptor()
    interceptor.level = HttpLoggingInterceptor.Level.BODY

    val client = OkHttpClient.Builder()
      .addInterceptor(interceptor)
      .build()

    val retrofit = Retrofit.Builder()
      .baseUrl("https://dummyjson.com/") // здесь ваш базовый URL
      .client(client)
      .addConverterFactory(GsonConverterFactory.create())
      .build()

    return retrofit.create(MainApi::class.java)
  }

  val logMessage = MutableLiveData<String>() // LiveData для логирования сообщений

  fun subscribeToCityTopic(city: String) {
    FirebaseMessaging.getInstance().subscribeToTopic(city)
      .addOnCompleteListener { task ->
        val msg = if (task.isSuccessful) {
          "Подписка на $city"
        } else {
          "Subscribe to $city failed"
        }
        logMessage.postValue(msg)
        // showToastMessage.postValue(msg) // Лучше не использовать Toast в ViewModel
      }

    CoroutineScope(Dispatchers.IO).launch {
      try {
        val subscribeResponse = mainApi.subscribeToCityTopic(city)
      } catch (e: Exception) {
        e.printStackTrace()
      }
    }
  }

  fun unsubscribeFromCityTopic(city: String) {
    FirebaseMessaging.getInstance().unsubscribeFromTopic(city)
      .addOnCompleteListener { task ->
        val msg = if (task.isSuccessful) {
          "Отписка по $city"
        } else {
          "Unsubscribe from $city failed"
        }
        logMessage.postValue(msg)
        // showToastMessage.postValue(msg) // Аналогично, не стоит использовать Toast в ViewModel
      }
  }
}

