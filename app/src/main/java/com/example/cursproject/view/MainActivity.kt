package com.example.cursproject.view
import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.DatePicker
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cursproject.adapter.MainAdapter
import com.example.cursproject.data.list.UserData
import com.example.cursproject.databinding.ActivityMainBinding
import com.example.cursproject.retrofit.MainApi
import com.example.cursproject.viewModel.MainViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale


class MainActivity : AppCompatActivity() {
    private val TAG = "Token ->"
    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: MainAdapter
    private lateinit var mainApi: MainApi
    private lateinit var datePickerDialog: DatePickerDialog
    private lateinit var viewModel: MainViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this@MainActivity)[MainViewModel::class.java]

        initRetrofit()
        initRecycler()
        viewModelFun()

        binding.apply {
            categorySpinner.onItemSelectedListener =
                object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(
                        parent: AdapterView<*>?,
                        view: View?,
                        position: Int,
                        id: Long
                    ) {
                        val selectedCategory = parent?.getItemAtPosition(position).toString()
                        viewModel.onCitySelected(selectedCategory)
                    }

                    override fun onNothingSelected(parent: AdapterView<*>?) {
                        TODO("Not yet implemented")
                    }
                }
        }

        fun showDatePicker() {
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            datePickerDialog = DatePickerDialog(
                this@MainActivity,
                { _: DatePicker, selectedYear: Int, selectedMonth: Int, selectedDay: Int ->
                    val selectedDate = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
                        .format(Calendar.getInstance().apply {
                            set(Calendar.YEAR, selectedYear)
                            set(Calendar.MONTH, selectedMonth)
                            set(Calendar.DAY_OF_MONTH, selectedDay)
                        }.time)
                    selectedDate.also {
                        binding.apply {
                            dateTextView.text = it
                            dateTextView.textSize = 20f
                        }
                    }
                },
                year, month, day
            )
            datePickerDialog.show()
        }
        getAllUsers()

        binding.apply {

            calendarButton.setOnClickListener {
                showDatePicker()
            }

            saveButton.setOnClickListener {
                val selectedCity = categorySpinner.selectedItem.toString()
                val selectedDate = dateTextView.text.toString()
                val quantityText = quantityEditText.text.toString()

                val userData = UserData("", "", "", "", quantityText.toDouble(), selectedDate, selectedCity)
                addNewUser(userData)
            }
        }
    }

    private fun viewModelFun() {
        viewModel.city.observe(this@MainActivity, Observer { cities ->
            val adapter = ArrayAdapter(
                this@MainActivity,
                android.R.layout.simple_spinner_item,
                cities
            )
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.categorySpinner.adapter = adapter
        })

        viewModel.selectedDate.observe(this@MainActivity, Observer { selectedDate ->
            "Выбранная дата: ${selectedDate.also { binding.dateTextView.text = it }}"
        })
    }


    private fun initRecycler() {

        binding.apply {
            adapter = MainAdapter()
            recyclerView.layoutManager = LinearLayoutManager(this@MainActivity)
            recyclerView.adapter = adapter

        }
    }

    private fun initRetrofit() {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY

        val client = OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl("https://65324f5bd80bd20280f54f5c.mockapi.io/karbayevd/api/users/")
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        mainApi = retrofit.create(MainApi::class.java)
    }


    private fun getAllUsers() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = mainApi.getAllUsers()
                if (response.isSuccessful) {
                    val userList = response.body()
                    userList?.let {
                        runOnUiThread {
                            adapter.submitList(userList)
                        }
                    }
                } else {
                    Log.e("DebugTag", "Error while fetching data: ${response.message()}")
                }
            } catch (e: Exception) {
                Log.e("DebugTag", "Error while fetching data: ${e.message}")
            }
        }
    }
    private fun addNewUser(userData: UserData) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val token = viewModel.token.value
                val response = mainApi.getAddUsers(userData, token)
                if (response.isSuccessful) {
                    val newUser: UserData? = response.body()
                    newUser?.let {
                        runOnUiThread {
                            adapter.addItem(newUser)
                            val position = adapter.currentList.indexOf(newUser)
                            adapter.notifyItemChanged(position)
                            Log.d("DebugTag", "Added new user: $userData")
                        }
                    }
                } else {
                    Log.e("DebugTag", "Error while adding user: ${response.message()}")
                }
            } catch (e: Exception) {
                Log.e("DebugTag", "Error while adding user: ${e.message}")
            }
        }
    }
}