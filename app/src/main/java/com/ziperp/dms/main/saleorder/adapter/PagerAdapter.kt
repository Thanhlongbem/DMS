package com.ziperp.dms.main.saleorder.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

class PagerAdapter(fragmentManager: FragmentManager,val listFragments: List<Fragment>) :
    FragmentPagerAdapter(fragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    var listTitles = listOf<String>()

    override fun getItem(position: Int): Fragment = listFragments[position]

    override fun getCount(): Int = listFragments.size

    override fun getPageTitle(position: Int): CharSequence = listTitles[position]

}