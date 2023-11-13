package com.example.cursproject.view

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.cursproject.R
import com.example.cursproject.databinding.ActivityServiceBinding
import com.example.cursproject.viewModel.ServicesViewModel
import com.google.firebase.messaging.FirebaseMessaging

class ServicesActivity : AppCompatActivity() {
    private val TAG = "Token"
    private lateinit var viewModel: ServicesViewModel
    private lateinit var binding: ActivityServiceBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityServiceBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this).get(ServicesViewModel::class.java)

        binding.T102.setOnClickListener {
            navigateToMainActivityWithToast("скорой")
        }
        binding.T103.setOnClickListener {
            navigateToMainActivityWithToast("пожарным")
        }
        binding.T112.setOnClickListener {
            navigateToMainActivityWithToast("полиции")
        }

        val userToken = intent.getStringExtra("userToken")
        val userId= intent.getStringExtra("userId")

        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val token = task.result
                val msg = getString(R.string.msg_token_fmt, token)
                Log.d(TAG, msg)
            } else {
                Log.w(TAG, "Fetching FCM registration token failed", task.exception)
            }
        }

        binding.notificationButton.setOnClickListener {
            viewModel.fetchUserCity(userToken.toString(), userId).observe(this) { city ->
                if (city.isNotEmpty()) {
                    showSubscriptionDialog(city)
                } else {
                    showToast("Failed to get user city")
                }
            }
        }
    }

    private fun navigateToMainActivityWithToast(destination: String) {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        showToast("Вы перешли к $destination")
    }

    private fun showSubscriptionDialog(userCity: String) {
        val dialogLayout = LayoutInflater.from(this).inflate(R.layout.subc_dialog, null)
        val cityTextView = dialogLayout.findViewById<TextView>(R.id.dialogCity)
        cityTextView.text = userCity

        val builder = AlertDialog.Builder(this)
        builder.setView(dialogLayout)

        val dialog = builder.create()

        val subscriptionYesButton = dialogLayout.findViewById<Button>(R.id.yesButton)
        val subscriptionNoButton = dialogLayout.findViewById<Button>(R.id.noButton)

        subscriptionYesButton.setOnClickListener {
            subscribeToCityTopic(userCity)
            showToast("Вы подписались на уведомления по городу $userCity")
            dialog.dismiss()
        }

        subscriptionNoButton.setOnClickListener {
            unsubscribeFromCityTopic(userCity)
            showToast("Вы отписались на уведомления по городу $userCity")
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun subscribeToCityTopic(city: String) {
        viewModel.subscribeToCityTopic(city)
    }

    private fun unsubscribeFromCityTopic(city: String) {
        viewModel.unsubscribeFromCityTopic(city)
    }
}