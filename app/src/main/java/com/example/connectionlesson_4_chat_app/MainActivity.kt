package com.example.connectionlesson_4_chat_app

import android.annotation.SuppressLint
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.adapter.UserAdapter
import com.example.connectionlesson_4_chat_app.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.models.User
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity(R.layout.activity_main) {
    private val binding: ActivityMainBinding by viewBinding()
    lateinit var firebaseAuth: FirebaseAuth
    lateinit var firebaseDatabase: FirebaseDatabase
    lateinit var currentUser: FirebaseUser
    lateinit var date: String
    lateinit var simpleDateFormat: SimpleDateFormat
    lateinit var reference: DatabaseReference

    @SuppressLint("SimpleDateFormat")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        firebaseAuth = FirebaseAuth.getInstance()
        firebaseDatabase = FirebaseDatabase.getInstance()
        currentUser = firebaseAuth.currentUser!!
        simpleDateFormat = SimpleDateFormat("HH:mm")
        date = simpleDateFormat.format(Date())

        reference = firebaseDatabase.getReference("Users")

        val connectedRef = Firebase.database.getReference(".info/connected")
        connectedRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val connected = snapshot.getValue(Boolean::class.java) ?: false
                if (connected) {
                    reference.child(currentUser.uid).child("userState").setValue("Online")
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }


    }

    override fun onDestroy() {
        super.onDestroy()
        reference.child(currentUser.uid).child("userState").setValue("Offline")
        reference.child(currentUser.uid).child("lastTime").setValue("$date")
    }

    override fun onPause() {
        reference.child(currentUser.uid).child("userState").setValue("Offline")
        reference.child(currentUser.uid).child("lastTime").setValue("$date")
        super.onPause()
    }

    override fun onResume() {
        reference.child(currentUser.uid).child("userState").setValue("Online")
        super.onResume()
    }

    override fun onSupportNavigateUp(): Boolean {
        return findNavController(R.id.my_nav_host).navigateUp()
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }
}