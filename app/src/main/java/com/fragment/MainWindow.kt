package com.fragment

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.activities.SignInActivity
import com.adapter.ViewPagerAdapter
import com.example.connectionlesson_4_chat_app.R
import com.example.connectionlesson_4_chat_app.databinding.FragmentMainWindowBinding
import com.example.connectionlesson_4_chat_app.databinding.ItemTabLayoutBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.models.User
import com.utils.SharedPreference

class MainWindow : Fragment(R.layout.fragment_main_window) {
    private val binding: FragmentMainWindowBinding by viewBinding()
    lateinit var googleSignInClient: GoogleSignInClient
    lateinit var tabLayoutList: ArrayList<String>
    lateinit var user: User

    lateinit var viewPagerAdapter: ViewPagerAdapter
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        binding.logout.setOnClickListener {
            logOut()
        }
        var fragments = ArrayList<Fragment>()
        fragments = arrayListOf(Chats(), Groups())
        viewPagerAdapter = ViewPagerAdapter(fragments, requireActivity())
        binding.chatVP.adapter = viewPagerAdapter
        TabLayoutMediator(binding.tabLayout, binding.chatVP) { _, _ ->
        }.attach()
        tabLayoutList = ArrayList()
        tabLayoutList.add("Chats")
        tabLayoutList.add("Groups")
        setTab()
    }

    @SuppressLint("UseRequireInsteadOfGet")
    private fun logOut() {
        SharedPreference.init(requireContext())
        SharedPreference.signUp = false
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.your_web_client_id))
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(requireContext(), gso)
        googleSignInClient.signOut()
        val intent = Intent(requireContext(), SignInActivity::class.java)
        startActivity(intent)
        activity!!.onBackPressed()
    }

    fun setTab() {
        val tabCount = binding.tabLayout.tabCount
        for (i in 0 until tabCount) {
            val itemTabLayoutBinding =
                ItemTabLayoutBinding.inflate(LayoutInflater.from(requireContext()), null, false)
            val tabAt = binding.tabLayout.getTabAt(i)
            tabAt!!.customView = itemTabLayoutBinding.root
            itemTabLayoutBinding.btn.text = tabLayoutList[i]

        }
    }
}