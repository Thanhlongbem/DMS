package com.ziperp.dms.main.visitcustomer.view

import android.content.Intent
import android.net.Uri
import androidx.recyclerview.widget.LinearLayoutManager
import com.ziperp.dms.R
import com.ziperp.dms.base.BaseFragment
import com.ziperp.dms.main.visitcustomer.adapter.AllVideosAdapter
import com.ziperp.dms.main.visitcustomer.model.PresenterModel
import com.ziperp.dms.utils.StickyHeaderDecoration
import kotlinx.android.synthetic.main.fragment_all_video.*
import java.util.*


class AllVideoFragment(val data: MutableList<PresenterModel>): BaseFragment(), AllVideosAdapter.OnItemPresenterClick {
    var adapter = AllVideosAdapter(arrayListOf())

    override fun setLayoutId(): Int = R.layout.fragment_all_video

    override fun initView() {
        sortData(data)


    }

    private fun sortData(data: MutableList<PresenterModel>){
        Collections.sort(data, Collections.reverseOrder())
        data[0].isSticky = true
        for(i in 1 until data.size - 1){

        }
        adapter = AllVideosAdapter(data)
        adapter.onItemPresenterClick = this
        val layoutManager = LinearLayoutManager(context)
        rv_allVideo.layoutManager = layoutManager
        rv_allVideo.setHasFixedSize(true)
        rv_allVideo.adapter = adapter
        rv_allVideo.addItemDecoration(StickyHeaderDecoration())
        adapter.notifyDataSetChanged()
    }

    override fun onItemPresenterClick(url: String) {

        val openVideo = Intent(Intent.ACTION_VIEW)

        openVideo.setDataAndType(Uri.parse(url), "video/*")

        startActivity(openVideo)
    }
}