package com.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class ViewPagerAdapter(
    private val categoryList: ArrayList<Fragment>,
    fragmentActivity: FragmentActivity,
) :
    FragmentStateAdapter(fragmentActivity) {
    override fun getItemCount(): Int = categoryList.size

    override fun createFragment(position: Int): Fragment {
        return categoryList[position]
    }
}