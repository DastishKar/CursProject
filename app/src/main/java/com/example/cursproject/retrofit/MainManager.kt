//package com.example.cursproject.retrofit
//
//import com.example.cursproject.data.list.UserData
//import retrofit2.Call
//import retrofit2.Callback
//import retrofit2.Response
//import retrofit2.Retrofit
//import retrofit2.converter.gson.GsonConverterFactory
//
//class MainManager {
//    private val retrofit: Retrofit = Retrofit.Builder()
//        .baseUrl("https://fair-canto-402302-default-rtdb.firebaseio.co/")
//        .addConverterFactory(GsonConverterFactory.create())
//        .build()
//
//
//    fun getStatistics(callback: (MutableList<UserData>?) -> Unit) {
//        val service = retrofit.create(MainApi::class.java)
//        val call = service.getAllUsers()
//        call.enqueue(object : Callback<MutableList<UserData>> {
//            override fun onResponse(
//                call: Call<MutableList<Statistics>>,
//                response: Response<MutableList<Statistics>>
//            ) {
//                if (response.isSuccessful){
//                    callback(response.body())
//                } else {
//                    callback(null)
//                }
//            }
//
//            override fun onFailure(call: Call<MutableList<Statistics>>, t: Throwable) {
//                callback(null)
//            }
//        })
//
//    }
//
//}