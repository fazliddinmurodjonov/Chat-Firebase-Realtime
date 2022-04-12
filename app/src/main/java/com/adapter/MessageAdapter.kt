package com.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.connectionlesson_4_chat_app.databinding.ItemMessageFromUserBinding
import com.example.connectionlesson_4_chat_app.databinding.ItemMessageToUserBinding
import com.models.Message

class MessageAdapter(var messageList: ArrayList<Message>, var uid: String) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    inner class FromVH(var binding: ItemMessageFromUserBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun onBind(message: Message) {
            binding.messageTv.text = message.message
            binding.dateTv.text = message.date
        }
    }

    inner class ToVH(var binding: ItemMessageToUserBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun onBind(message: Message) {
            binding.messageTv.text = message.message
            binding.dateTv.text = message.date
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == 1) {
            return FromVH(ItemMessageFromUserBinding.inflate(LayoutInflater.from(parent.context),
                parent,
                false))
        } else {
            return ToVH(ItemMessageToUserBinding.inflate(LayoutInflater.from(parent.context),
                parent,
                false))
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (getItemViewType(position) == 1) {
            val fromVH = holder as FromVH
            fromVH.onBind(messageList[position])
        } else {
            val toVH = holder as ToVH
            toVH.onBind(messageList[position])
        }
    }

    override fun getItemViewType(position: Int): Int {
        if (messageList[position].fromUser == uid) {
            return 1
        } else {
            return 2
        }
        return super.getItemViewType(position)
    }

    override fun getItemCount(): Int = messageList.size
}
