package com.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.connectionlesson_4_chat_app.databinding.ItemUserBinding
import com.google.firebase.database.*
import com.models.Messages
import com.models.User
import com.squareup.picasso.Picasso

class UserAdapter(var userList: ArrayList<User>, var uid: String) :
    RecyclerView.Adapter<UserAdapter.UserVH>() {
    lateinit var currentItem: OnItemClickListener
    lateinit var referenceMessage: DatabaseReference
    lateinit var firebaseDatabase: FirebaseDatabase

    fun interface OnItemClickListener {
        fun onClick(user: User)
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        currentItem = listener
    }


    inner class UserVH(var binding: ItemUserBinding) : RecyclerView.ViewHolder(binding.root) {

        fun onBind(user: User) {
            firebaseDatabase = FirebaseDatabase.getInstance()
            referenceMessage = firebaseDatabase.getReference("Messages")
            var messageList = ArrayList<String>()
            referenceMessage.child("$uid/${user.uid}")
                .addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val messages = snapshot.children
                        messageList.clear()
                        for (message in messages) {
                            val m = message.getValue(Messages::class.java)
                            messageList.add(m?.message!!)
                            if (messageList.size == 0) {
                                binding.lastMessage.text = ""
                            } else {
                                binding.lastMessage.text = messageList[messageList.size - 1]
                            }
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {

                    }

                })
            binding.userName.text = user.displayName
            binding.lastSeen.text = user.lastTime
            if (user.userState != "Online") {
                binding.onlineSign.visibility = View.INVISIBLE
            }
            Picasso.get().load(user.photoUrl).into(binding.imageProfile)
            binding.root.setOnClickListener {
                currentItem.onClick(user)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserVH {
        return UserVH(ItemUserBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: UserVH, position: Int) {
        val user = userList[position]
        holder.onBind(user)
    }

    override fun getItemCount(): Int = userList.size
}