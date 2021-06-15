package com.ziperp.dms.main.visitcustomer.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.ziperp.dms.R
import com.ziperp.dms.extensions.getString
import com.ziperp.dms.main.visitcustomer.model.PresenterModel
import com.ziperp.dms.main.visitcustomer.view.AllVideoFragment
import com.ziperp.dms.main.visitcustomer.view.LocationFragment

class PresenterPagerAdapter(fragmentManager: FragmentManager, val dataAll: MutableList<PresenterModel>,val dataBookMarks: MutableList<PresenterModel>,val dataRecently: MutableList<PresenterModel>) :
    FragmentPagerAdapter(fragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    override fun getItem(position: Int): Fragment =
        when(position){
            0 -> {
                AllVideoFragment(dataAll)
            }
            1 -> {
                LocationFragment.newInstance()
            }
            else -> LocationFragment.newInstance()
        }

    override fun getCount(): Int = 3

    override fun getPageTitle(position: Int): CharSequence =
        when(position) {
            0 -> R.string.str_all_video.getString() + " (" + dataAll.size + ")"
            1 -> R.string.str_bookmarks.getString() + " (" + dataBookMarks.size + ")"
            else -> R.string.str_recently_played.getString() + " (" + dataRecently.size + ")"
        }

}