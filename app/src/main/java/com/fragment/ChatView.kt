package com.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.adapter.MessageAdapter
import com.adapter.UserAdapter
import com.example.connectionlesson_4_chat_app.R
import com.example.connectionlesson_4_chat_app.databinding.FragmentChatViewBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.models.Message
import com.models.User
import com.squareup.picasso.Picasso
import com.utils.Empty
import java.text.SimpleDateFormat
import java.util.*

class ChatView : Fragment(R.layout.fragment_chat_view) {
    val binding: FragmentChatViewBinding by viewBinding()
    lateinit var firebaseAuth: FirebaseAuth
    lateinit var firebaseDatabase: FirebaseDatabase
    lateinit var reference: DatabaseReference
    lateinit var referenceMessage: DatabaseReference
    lateinit var currentUser: FirebaseUser
    var onFragment = true
    lateinit var simpleDateFormat: SimpleDateFormat
    lateinit var date: String
    lateinit var messageList: ArrayList<Message>
    lateinit var userUID: String
    lateinit var messageAdapter: MessageAdapter
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        userUID = arguments?.getString("userUID")!!
        messageList = ArrayList()
        firebaseAuth = FirebaseAuth.getInstance()
        currentUser = firebaseAuth.currentUser!!
        firebaseDatabase = FirebaseDatabase.getInstance()
        reference = firebaseDatabase.getReference("Users").child(userUID!!)

        reference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val user = snapshot.getValue(User::class.java)
                binding.userState.text = user?.userState
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
        reference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val user = snapshot.getValue(User::class.java)
                Picasso.get().load(user?.photoUrl).into(binding.profileImg)
                binding.userName.text = user?.displayName
                binding.userState.text = user?.userState
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
        referenceMessage = firebaseDatabase.getReference("Messages")

        referenceMessage.child("${firebaseAuth.currentUser!!.uid}/$userUID")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val messages = snapshot.children
                    messageList.clear()

                    for (message in messages) {
                        val m = message.getValue(Message::class.java)
                        messageList.add(m!!)
                    }
                    var lastMessage = ""
                    if (messageList.size != 0) {
                        lastMessage = messageList[messageList.size - 1].message!!
                    }
                    val referenceUser = firebaseDatabase.getReference("Users")
                    referenceUser.child("${firebaseAuth.currentUser!!.uid}/lastMessage")
                        .setValue(lastMessage)
                    referenceUser.child("$userUID/lastMessage").setValue(lastMessage)

                    messageAdapter = MessageAdapter(messageList, firebaseAuth.currentUser!!.uid)
                    if (onFragment) {
                        binding.messageRV.adapter = messageAdapter
                    }
                }

                override fun onCancelled(error: DatabaseError) {

                }

            })
        binding.sendMessageBtn.setOnClickListener {
            val m = binding.sendMessageEt.text.toString()
            val messageEmpty = Empty.empty(m)
            val messageSpace = Empty.space(m)

            if (messageEmpty && messageSpace && m.isNotEmpty()) {
                val simpleDateFormat = SimpleDateFormat("dd.MM.yyyy HH:mm")
                val date = simpleDateFormat.format(Date())
                val message = Message(firebaseAuth.currentUser!!.uid, userUID, m, date)
                val key = referenceMessage.push().key
                referenceMessage.child("${firebaseAuth.currentUser!!.uid}/${userUID}/$key")
                    .setValue(message)
                referenceMessage.child("${userUID}/${firebaseAuth.currentUser!!.uid}/$key")
                    .setValue(message)
                binding.sendMessageEt.text.clear()

            }
        }

        binding.backBtn.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        onFragment = false
    }

}