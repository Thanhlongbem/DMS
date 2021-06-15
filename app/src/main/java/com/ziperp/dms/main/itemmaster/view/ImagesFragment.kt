package com.ziperp.dms.main.itemmaster.view

import android.annotation.SuppressLint
import android.content.Intent
import android.view.View
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.ziperp.dms.Injection
import com.ziperp.dms.R
import com.ziperp.dms.base.BaseFragment
import com.ziperp.dms.core.rest.Status
import com.ziperp.dms.extensions.getString
import com.ziperp.dms.extensions.getViewModel
import com.ziperp.dms.main.itemmaster.adapter.ImageAdapter
import com.ziperp.dms.main.itemmaster.view.ImagesActivity.Companion.EXTRA_IMAGE_LIST
import com.ziperp.dms.main.itemmaster.view.ImagesActivity.Companion.EXTRA_POSITION
import kotlinx.android.synthetic.main.fragment_item_master_image.*

class ImagesFragment(val itemCd: String): BaseFragment() {
    lateinit var adapter: ImageAdapter
    private var viewMode = true

    private val viewModel by lazy { getViewModel { Injection.provideItemMasterViewModel() } }

    override fun setLayoutId(): Int = R.layout.fragment_item_master_image

    override fun initView() {
        genData()
        handleClickEvent()
    }

    @SuppressLint("SetTextI18n")
    private fun genData(){
        adapter = ImageAdapter(arrayListOf(), viewMode)
        rv_images.layoutManager = LinearLayoutManager(context)
        rv_images.setHasFixedSize(true)
        rv_images.adapter = adapter
        adapter.onImageClicked = { position -> showImage(position) }

        viewModel.getFileOfItemMaster(itemCd)
        viewModel.itemMasterImageData.observe(this, Observer {
            when (it.status) {
                Status.SUCCESS -> {
                    loading_progressbar.visibility = View.GONE
                    it.data?.let { response ->
                        if (!response.data.isNullOrEmpty()) {
                            adapter.addData(response.data)
                            rv_images.visibility = View.VISIBLE
                            tv_empty.visibility = View.GONE
                        } else {
                            rv_images.visibility = View.GONE
                            tv_empty.visibility = View.VISIBLE
                        }

                        tv_title.text = "${response.data.size} ${R.string.str_images.getString()}"
                    }
                }
                Status.LOADING -> { loading_progressbar.visibility = View.VISIBLE }
                Status.ERROR -> {
                    loading_progressbar.visibility = View.GONE
                    rv_images.visibility = View.GONE
                    tv_empty.visibility = View.VISIBLE
                }
            }
        })
    }

    private fun handleClickEvent(){
        btn_switch.setOnClickListener {
            viewMode = !viewMode
            rv_images.layoutManager = if (viewMode) LinearLayoutManager(context) else GridLayoutManager(context, 3)
            btn_switch.setImageResource(if (viewMode) R.drawable.ic_list_black else R.drawable.ic_grid_black)
            adapter = ImageAdapter(adapter.data, viewMode)
            adapter.onImageClicked = { position -> showImage(position) }
            rv_images.adapter = adapter
        }
    }

    private fun showImage(position: Int) {
        val intent = Intent(activity, ImagesActivity::class.java)
        intent.putExtra(EXTRA_IMAGE_LIST, adapter.data)
        intent.putExtra(EXTRA_POSITION, position)
        startActivity(intent)
    }
}