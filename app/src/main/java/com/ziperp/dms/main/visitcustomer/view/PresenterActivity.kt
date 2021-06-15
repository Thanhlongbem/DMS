package com.ziperp.dms.main.visitcustomer.view

import com.ziperp.dms.R
import com.ziperp.dms.base.BaseActivity
import com.ziperp.dms.main.visitcustomer.adapter.PresenterPagerAdapter
import com.ziperp.dms.main.visitcustomer.model.PresenterModel
import kotlinx.android.synthetic.main.activity_presenter.*

class PresenterActivity : BaseActivity() {
    val dataAll: MutableList<PresenterModel> = arrayListOf()
    val dataBookMarks: MutableList<PresenterModel> = arrayListOf()
    val dataRecent: MutableList<PresenterModel> = arrayListOf()



    override fun setLayoutId(): Int = R.layout.activity_presenter

    override fun initView() {
        initData()

    }

    private fun initData(){
        dataAll.add(PresenterModel("B1","https://static.videezy.com/system/resources/previews/000/008/451/original/Dark_Haired_Girl_in_thought_1.mp4",
            "mp4", true))
        dataAll.add(PresenterModel("B1", "https://static.videezy.com/system/resources/previews/000/008/451/original/Dark_Haired_Girl_in_thought_1.mp4",
            "mp4"))
        dataAll.add(PresenterModel("B1","https://static.videezy.com/system/resources/previews/000/008/451/original/Dark_Haired_Girl_in_thought_1.mp4",
            "mp1"))
        dataAll.add(PresenterModel("B1", "https://static.videezy.com/system/resources/previews/000/008/451/original/Dark_Haired_Girl_in_thought_1.mp4",
            "mp2"))
        dataAll.add(PresenterModel("B2","https://static.videezy.com/system/resources/previews/000/008/451/original/Dark_Haired_Girl_in_thought_1.mp4",
            "mp3", true))
        dataAll.add(PresenterModel("B2", "https://static.videezy.com/system/resources/previews/000/008/451/original/Dark_Haired_Girl_in_thought_1.mp4",
            "mp4"))
        dataAll.add(PresenterModel("B2","https://static.videezy.com/system/resources/previews/000/008/451/original/Dark_Haired_Girl_in_thought_1.mp4",
            "mp4"))
        dataAll.add(PresenterModel("B2", "https://static.videezy.com/system/resources/previews/000/008/451/original/Dark_Haired_Girl_in_thought_1.mp4",
            "mp4"))
        dataAll.add(PresenterModel("B3","https://static.videezy.com/system/resources/previews/000/008/451/original/Dark_Haired_Girl_in_thought_1.mp4",
            "mp1", true))
        dataAll.add(PresenterModel("B4", "https://static.videezy.com/system/resources/previews/000/008/451/original/Dark_Haired_Girl_in_thought_1.mp4",
            "mp2", true))
        dataAll.add(PresenterModel("B5","https://static.videezy.com/system/resources/previews/000/008/451/original/Dark_Haired_Girl_in_thought_1.mp4",
            "mp3", true))
        dataAll.add(PresenterModel("B5", "https://static.videezy.com/system/resources/previews/000/008/451/original/Dark_Haired_Girl_in_thought_1.mp4",
            "mp4"))
        initPager(dataAll, arrayListOf(), arrayListOf())
    }

    private fun initPager(dataAll: MutableList<PresenterModel>, dataBookMarks: MutableList<PresenterModel>, dataRecent: MutableList<PresenterModel>){
        val pagerAdapter = PresenterPagerAdapter(supportFragmentManager, dataAll, dataBookMarks, dataRecent)
        viewpager.adapter = pagerAdapter
        tab.setupWithViewPager(viewpager)
    }

}