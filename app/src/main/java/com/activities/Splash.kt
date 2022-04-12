package com.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.example.connectionlesson_4_chat_app.MainActivity
import com.example.connectionlesson_4_chat_app.R
import com.utils.SharedPreference

class Splash : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        SharedPreference.init(this)
        val signUp = SharedPreference.signUp
        val handler = Handler(Looper.getMainLooper())
        handler.postDelayed({
            if (signUp!!) {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                val intent = Intent(this, SignInActivity::class.java)
                startActivity(intent)
                finish()
            }
        }, 2000)

    }
}