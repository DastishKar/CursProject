package com.example.cursproject.view

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.Button
import android.widget.CheckBox
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.cursproject.R
import com.example.cursproject.databinding.ActivityServiceBinding
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging

class ServicesActivity : AppCompatActivity() {
    private val TAG = " Token"
    private val checkBoxMap = mutableMapOf<String, CheckBox>()
    private lateinit var binding: ActivityServiceBinding
//    private lateinit var mainApi: MainApi
//    private lateinit var userSubscribeManager: UserSubscribeManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityServiceBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        initRetrofit()
//        userSubs()

        binding.T102.setOnClickListener {
            val intent = Intent(this@ServicesActivity, MainActivity::class.java)
            startActivity(intent)
            Toast.makeText(this@ServicesActivity, "Вы перешли к скорой", Toast.LENGTH_SHORT).show()
        }
        binding.T103.setOnClickListener {
            val intent = Intent(this@ServicesActivity, MainActivity::class.java)
            startActivity(intent)
            Toast.makeText(this@ServicesActivity, "Вы перешли к пожарным", Toast.LENGTH_SHORT)
                .show()
        }
        binding.T112.setOnClickListener {
            val intent = Intent(this@ServicesActivity, MainActivity::class.java)
            startActivity(intent)
            Toast.makeText(this@ServicesActivity, "Вы перешли к полиции", Toast.LENGTH_SHORT).show()
        }

        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w(TAG, "Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            }
            val token = task.result
            val msg = getString(R.string.msg_token_fmt, token)
            Log.d(TAG, msg)
            Toast.makeText(baseContext, msg, Toast.LENGTH_SHORT).show()
        })


        binding.notificationButton.setOnClickListener {
            val TAG = "Topics notification"
            val dialogLayout = LayoutInflater.from(this@ServicesActivity)
                .inflate(R.layout.subc_dialog, null)

            checkBoxMap["Chicago"] = dialogLayout.findViewById(R.id.topicCheckbox1)
            checkBoxMap["New-York"] = dialogLayout.findViewById(R.id.topicCheckbox2)
            checkBoxMap["Astana"] = dialogLayout.findViewById(R.id.topicCheckbox3)

            val builder = AlertDialog.Builder(this@ServicesActivity)
            builder.setView(dialogLayout)


            val dialog = builder.create()

            val subscriptionYesButton = dialogLayout.findViewById<Button>(R.id.yesButton)
            val subscriptionNoButton = dialogLayout.findViewById<Button>(R.id.noButton)

            subscriptionYesButton.setOnClickListener {
                val topics = listOf("Chicago", "New-York", "Astana")

                for (topic in topics) {
                    if (isTopicSelected(topic)) {
                        FirebaseMessaging.getInstance().subscribeToTopic(topic)
                            .addOnCompleteListener { task ->
                                val msg = if (task.isSuccessful) {
                                    "Subscribed to $topic"
                                } else {
                                    "Subscribe to $topic failed"
                                }
                                Log.d(TAG, msg)
                                Toast.makeText(baseContext, msg, Toast.LENGTH_SHORT).show()
                            }
                    }
                }
                dialog.dismiss()
            }

            subscriptionNoButton.setOnClickListener {
                val topics = listOf("Chicago", "New-York", "Astana")

                for (topic in topics) {
                    if (isTopicSelected(topic)) {
                        FirebaseMessaging.getInstance().unsubscribeFromTopic(topic)
                            .addOnCompleteListener { task ->
                                val msg = if (task.isSuccessful) {
                                    "Unsubscribed from $topic"
                                } else {
                                    "Unsubscribe from $topic failed"
                                }
                                Log.d(TAG, msg)
                                Toast.makeText(baseContext, msg, Toast.LENGTH_SHORT).show()
                            }
                    }
                }
                dialog.dismiss()
            }
            dialog.show()
        }
    }




    private fun isTopicSelected(topic: String): Boolean {
        val checkBox = checkBoxMap[topic]
        return checkBox?.isChecked ?: false
    }
}


