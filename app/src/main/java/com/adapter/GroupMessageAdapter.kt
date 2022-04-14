package com.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.connectionlesson_4_chat_app.databinding.ItemMessageFromUserBinding
import com.example.connectionlesson_4_chat_app.databinding.ItemMessageToGroupBinding
import com.example.connectionlesson_4_chat_app.databinding.ItemMessageToUserBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.models.GroupMessages
import com.models.User
import com.squareup.picasso.Picasso

class GroupMessageAdapter(var groupMessagesList: ArrayList<GroupMessages>, var uid: String) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    lateinit var firebaseDatabase: FirebaseDatabase
    lateinit var reference: DatabaseReference

    inner class FromVH(var binding: ItemMessageFromUserBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun onBind(message: GroupMessages) {
            binding.messageTv.text = message.message
            binding.dateTv.text = message.date
        }
    }

    inner class ToVH(var binding: ItemMessageToGroupBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun onBind(message: GroupMessages) {
            var u = User()
            firebaseDatabase = FirebaseDatabase.getInstance()
            reference = firebaseDatabase.getReference("Users").child(message.fromUser!!)
            reference.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val user = snapshot.getValue(User::class.java)
                    u = user!!
                    binding.userName.text = user?.displayName
                    Picasso.get().load(user?.photoUrl).into(binding.profileImg)

                }

                override fun onCancelled(error: DatabaseError) {
                }

            })
            reference.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val user = snapshot.getValue(User::class.java)
                    binding.messageTv.text = message.message
                    binding.dateTv.text = message.date
                    if (user?.userState == "Offline") {
                        binding.userState.visibility = View.INVISIBLE
                    } else {
                        binding.userState.visibility = View.VISIBLE
                    }
                }

                override fun onCancelled(error: DatabaseError) {

                }

            })
            val connectedRef = Firebase.database.getReference(".info/connected")
            connectedRef.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val connected = snapshot.getValue(Boolean::class.java) ?: false
                    if (connected) {
                        reference.child(u.uid!!).child("userState").setValue("Online")
                    } else {
                        reference.child(u.uid!!).child("userState").onDisconnect()
                            .setValue("Offline")
                    }
                }

                override fun onCancelled(error: DatabaseError) {

                }
            })
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == 1) {
            return FromVH(ItemMessageFromUserBinding.inflate(LayoutInflater.from(parent.context),
                parent,
                false))
        } else {
            return ToVH(ItemMessageToGroupBinding.inflate(LayoutInflater.from(parent.context),
                parent,
                false))
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (getItemViewType(position) == 1) {
            val fromVH = holder as FromVH
            fromVH.onBind(groupMessagesList[position])
        } else {
            val toVH = holder as ToVH
            toVH.onBind(groupMessagesList[position])
        }
    }

    override fun getItemViewType(position: Int): Int {
        if (groupMessagesList[position].fromUser == uid) {
            return 1
        } else {
            return 2
        }
        return super.getItemViewType(position)
    }

    override fun getItemCount(): Int = groupMessagesList.size
}
