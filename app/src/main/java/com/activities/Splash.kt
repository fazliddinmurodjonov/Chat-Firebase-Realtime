package com.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import com.adapter.UserAdapter
import com.example.connectionlesson_4_chat_app.MainActivity
import com.example.connectionlesson_4_chat_app.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.models.User
import com.utils.SharedPreference

class Splash : AppCompatActivity(R.layout.activity_splash) {
    lateinit var reference: DatabaseReference
    lateinit var firebaseDatabase: FirebaseDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        SharedPreference.init(this)
        var containUser = false
        var userList = ArrayList<String>()
        firebaseDatabase = FirebaseDatabase.getInstance()
        reference = firebaseDatabase.getReference("Users")
        reference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val users = snapshot.children
                userList.clear()
                for (user in users) {
                    val userValue = user.getValue(User::class.java)
                    if (userValue != null) {
                        userList.add(userValue.email!!)
                    }
                }
                containUser = userList.contains(SharedPreference.userGmail)
            }

            override fun onCancelled(error: DatabaseError) {
            }

        })


        val signUp = SharedPreference.signUp
        val handler = Handler(Looper.getMainLooper())
        handler.postDelayed({
            if (signUp!! && containUser) {
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