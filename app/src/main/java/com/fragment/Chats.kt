package com.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.adapter.UserAdapter
import com.example.connectionlesson_4_chat_app.R
import com.example.connectionlesson_4_chat_app.databinding.FragmentChatsBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.models.User

class Chats : Fragment(R.layout.fragment_chats) {
    private val binding: FragmentChatsBinding by viewBinding()
    lateinit var userAdapter: UserAdapter
    lateinit var userList: ArrayList<User>
    lateinit var firebaseAuth: FirebaseAuth
    lateinit var firebaseDatabase: FirebaseDatabase
    lateinit var reference: DatabaseReference
    lateinit var currentUser: FirebaseUser
    lateinit var messageList: ArrayList<String>
    lateinit var referenceMessage: DatabaseReference
    var connect = true
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        firebaseAuth = FirebaseAuth.getInstance()
        currentUser = firebaseAuth.currentUser!!
        firebaseDatabase = FirebaseDatabase.getInstance()
        userList = ArrayList()
        messageList = ArrayList()
        reference = firebaseDatabase.getReference("Users")
        reference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val users = snapshot.children
                userList.clear()
                messageList.clear()
                for (user in users) {
                    val userValue = user.getValue(User::class.java)
                    if (userValue != null && currentUser.uid != userValue.uid) {
                        userList.add(userValue)
                    }
                }

                userAdapter = UserAdapter(userList,currentUser.uid)
                if (connect) {
                    binding.profilesRV.adapter = userAdapter
                }

                userAdapter.setOnItemClickListener {
                    val bundle = bundleOf("userUID" to it.uid)
                    findNavController().navigate(R.id.chatView, bundle)
                }
            }

            override fun onCancelled(error: DatabaseError) {
            }

        })

    }

    override fun onResume() {
        super.onResume()
        connect = true
    }

    override fun onPause() {
        super.onPause()
        connect = false
    }

}