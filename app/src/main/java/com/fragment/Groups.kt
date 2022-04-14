package com.fragment

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.adapter.GroupAdapter
import com.example.connectionlesson_4_chat_app.R
import com.example.connectionlesson_4_chat_app.databinding.CustomAddGroupDialogBinding
import com.example.connectionlesson_4_chat_app.databinding.FragmentGroupsBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.models.Group
import com.utils.Empty

class Groups : Fragment(R.layout.fragment_groups) {
    private val binding: FragmentGroupsBinding by viewBinding()
    lateinit var groupAdapter: GroupAdapter
    lateinit var groupList: ArrayList<Group>
    lateinit var firebaseAuth: FirebaseAuth
    lateinit var currentUser: FirebaseUser
    lateinit var firebaseDatabase: FirebaseDatabase
    lateinit var reference: DatabaseReference
    var onFragment = true

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        firebaseAuth = FirebaseAuth.getInstance()
        currentUser = firebaseAuth.currentUser!!
        firebaseDatabase = FirebaseDatabase.getInstance()
        groupList = ArrayList()
        reference = firebaseDatabase.getReference("Groups")
        reference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val groups = snapshot.children
                groupList.clear()
                for (group in groups) {
                    val groupValue = group.getValue(Group::class.java)
                    if (groupValue != null) {
                        groupList.add(groupValue)
                    }
                }
                groupAdapter = GroupAdapter(groupList)
                if (onFragment) {
                    binding.groupsRV.adapter = groupAdapter
                }
                groupAdapter.setOnGroupClickListener {
                    val bundleOf = bundleOf("groupUnique" to it)
                    findNavController().navigate(R.id.groupView, bundleOf)
                }

            }

            override fun onCancelled(error: DatabaseError) {

            }

        })


        binding.addGroupBtn.setOnClickListener {
            val dialog = Dialog(requireContext())
            val dialogView =
                CustomAddGroupDialogBinding.inflate(LayoutInflater.from(requireContext()),
                    null,
                    false)
            dialog.setContentView(dialogView.root)
            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialogView.cancelButton.setOnClickListener {
                dialog.dismiss()
            }

            dialogView.saveButton.setOnClickListener {
                val groupUnique = reference.push().key
                val groupName = dialogView.groupName.text.toString()
                val empty = Empty.empty(groupName)
                val space = Empty.space(groupName)
                val group = Group(groupUnique, groupName)
                if (empty && space) {
                    var groupNameList = ArrayList<String>()

                    reference.addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {
                            groupNameList.clear()
                            val groups = snapshot.children
                            for (group in groups) {
                                val groupValue = group.getValue(Group::class.java)
                                groupNameList.add(groupValue?.groupName!!)
                            }
                            val contains = groupNameList.contains(groupName)
                            if (!contains) {
                                reference.child(groupUnique!!).setValue(group)
                                dialog.dismiss()
                            }
                            if (groupNameList.size == 0) {
                                reference.child(groupUnique!!).setValue(group)
                                dialog.dismiss()
                            }
                        }

                        override fun onCancelled(error: DatabaseError) {

                        }

                    })


                }

            }
            dialog.show()
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