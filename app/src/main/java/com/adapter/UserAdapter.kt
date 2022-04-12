package com.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.connectionlesson_4_chat_app.databinding.ItemUserBinding
import com.models.User
import com.squareup.picasso.Picasso

class UserAdapter(var userList: ArrayList<User>) :
    RecyclerView.Adapter<UserAdapter.UserVH>() {
    lateinit var currentItem: OnItemClickListener

    fun interface OnItemClickListener {
        fun onClick(user: User)
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        currentItem = listener
    }


    inner class UserVH(var binding: ItemUserBinding) : RecyclerView.ViewHolder(binding.root) {

        fun onBind(user: User) {
            binding.userName.text = user.displayName
            binding.lastMessage.text = user.lastMessage
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