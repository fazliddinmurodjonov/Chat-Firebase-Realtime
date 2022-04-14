package com.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.adapter.GroupAdapter
import com.adapter.GroupMessageAdapter
import com.example.connectionlesson_4_chat_app.R
import com.example.connectionlesson_4_chat_app.databinding.FragmentGroupViewBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.models.Group
import com.models.GroupMessages
import com.models.Messages
import com.utils.Empty
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class GroupView : Fragment(R.layout.fragment_group_view) {
    private val binding: FragmentGroupViewBinding by viewBinding()
    lateinit var groupMessageAdapter: GroupMessageAdapter
    lateinit var groupMessagesList: ArrayList<GroupMessages>
    lateinit var firebaseAuth: FirebaseAuth
    lateinit var currentUser: FirebaseUser
    lateinit var firebaseDatabase: FirebaseDatabase
    lateinit var reference: DatabaseReference
    lateinit var groupUnique: String
    lateinit var referenceGroup: DatabaseReference
    var onFragment = true
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        firebaseAuth = FirebaseAuth.getInstance()
        firebaseDatabase = FirebaseDatabase.getInstance()
        groupMessagesList = ArrayList()
        currentUser = firebaseAuth.currentUser!!

        groupUnique = arguments?.getString("groupUnique")!!
        reference = firebaseDatabase.getReference("GroupMessages").child(groupUnique)
        referenceGroup = firebaseDatabase.getReference("Groups").child(groupUnique)
        referenceGroup.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val group = snapshot.getValue(Group::class.java)
                binding.groupName.text = group?.groupName
            }

            override fun onCancelled(error: DatabaseError) {
            }

        })

        reference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                groupMessagesList.clear()
                val messages = snapshot.children
                for (message in messages) {
                    val messageValue = message.getValue(GroupMessages::class.java)
                    groupMessagesList.add(messageValue!!)
                }
                groupMessageAdapter = GroupMessageAdapter(groupMessagesList, currentUser.uid)
                if (onFragment) {
                    binding.GroupMessagesRV.adapter = groupMessageAdapter
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
                val referenceUser =
                    firebaseDatabase.getReference("Users").child("${currentUser.uid}")
                val groupMessage = GroupMessages(currentUser.uid, groupUnique, m, date)
                val key = referenceUser.push().key
                reference.child(key!!).setValue(groupMessage)
                binding.sendMessageEt.text.clear()
            }
        }
        binding.backBtn.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    override fun onResume() {
        super.onResume()
        onFragment = true
    }

    override fun onPause() {
        super.onPause()
        onFragment = false
    }
}