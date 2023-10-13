package com.example.cursproject.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class MainViewModel: ViewModel() {
    private val cities = MutableLiveData<List<String>>()
    val city: LiveData<List<String>> get() = cities


    private val selectedCities = MutableLiveData<String>()
    val selectedCity: LiveData<String> get() = selectedCities

    private val selectedDates = MutableLiveData<String>()
    val selectedDate: LiveData<String> get() = selectedDates



    init {
        cities.value = mutableListOf(
            "Chicago", "New York",
            "Los Angeles", "Chicago",
            "Miami", "Houston",
            "Philadelphia", "Phoenix",
            "San Francisco", "Washington")
    }

    fun onCitySelected(city: String){
        selectedCities.value = city
    }

    fun onDateSelected(year: Int, month: Int, day: Int) {
        val selectedDate = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
            .format(Calendar.getInstance().apply {
                set(Calendar.YEAR, year)
                set(Calendar.MONTH, month)
                set(Calendar.DAY_OF_MONTH, day)
            }.time)
        selectedDates.value = selectedDate
    }


}