package com.ziperp.dms.main.itemmaster.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class SQPagerAdapter(fragmentActivity: FragmentActivity, val listFragments: List<Fragment>) :
    FragmentStateAdapter(fragmentActivity) {

    override fun getItemCount(): Int = listFragments.size

    override fun createFragment(position: Int): Fragment {
        return listFragments[position]
    }



}