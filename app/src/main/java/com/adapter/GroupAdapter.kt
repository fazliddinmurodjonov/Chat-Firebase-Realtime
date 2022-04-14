package com.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.connectionlesson_4_chat_app.databinding.ItemGroupBinding
import com.models.Group

class GroupAdapter(var groupList: ArrayList<Group>) : RecyclerView.Adapter<GroupAdapter.GroupVH>() {
    lateinit var selectedGroup: OnGroupClickListener

    fun interface OnGroupClickListener {
        fun onClick(groupUnique: String)
    }

    fun setOnGroupClickListener(listener: OnGroupClickListener) {
        selectedGroup = listener
    }

    inner class GroupVH(var binding: ItemGroupBinding) : RecyclerView.ViewHolder(binding.root) {
        fun onBind(group: Group) {
            binding.groupName.text = group.groupName
            binding.root.setOnClickListener {
                selectedGroup.onClick(group.groupUnique!!)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GroupVH {
        return GroupVH(ItemGroupBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: GroupVH, position: Int) {
        val group = groupList[position]
        holder.onBind(group)
    }

    override fun getItemCount(): Int = groupList.size
}