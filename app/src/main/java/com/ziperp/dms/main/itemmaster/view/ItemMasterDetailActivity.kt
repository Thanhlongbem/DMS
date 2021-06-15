package com.ziperp.dms.main.itemmaster.view

import android.content.Intent
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import com.ziperp.dms.Injection
import com.ziperp.dms.R
import com.ziperp.dms.base.BaseActivity
import com.ziperp.dms.base.PagerAdapter
import com.ziperp.dms.core.rest.Status
import com.ziperp.dms.extensions.getString
import com.ziperp.dms.extensions.getViewModel
import com.ziperp.dms.extensions.setOnClickNetworkListener
import com.ziperp.dms.main.itemmaster.model.ItemMasterDetailResponse
import com.ziperp.dms.main.itemmaster.view.ItemMasterActivity.Companion.EXTRA_ITEM_CODE
import com.ziperp.dms.main.itemmaster.view.ItemMasterActivity.Companion.EXTRA_ITEM_LS_CTR
import com.ziperp.dms.main.itemmaster.view.ItemMasterActivity.Companion.EXTRA_ITEM_NAME
import com.ziperp.dms.main.itemmaster.view.ItemMasterActivity.Companion.EXTRA_ITEM_UNIT
import kotlinx.android.synthetic.main.activity_item_master_detail.*

class ItemMasterDetailActivity: BaseActivity() {

    lateinit var data: ItemMasterDetailResponse.ItemMasterDetail

    private val viewModel by lazy { getViewModel { Injection.provideItemMasterViewModel() } }

    override fun setLayoutId(): Int = R.layout.activity_item_master_detail

    override fun initView() {
        dataObserver()

        btn_stock_quantity.setOnClickNetworkListener {
            val intent = Intent(this, StockQuantityActivity::class.java)
            intent.putExtra(EXTRA_ITEM_CODE, data.itemCd)
            intent.putExtra(EXTRA_ITEM_NAME, "${data.itemNo} - ${data.itemNm}")
            intent.putExtra(EXTRA_ITEM_UNIT, data.whUnitNm)
            intent.putExtra(EXTRA_ITEM_LS_CTR, data.lotSeriCtrlNm)
            startActivity(intent)
        }
    }

    private fun dataObserver() {
        val itemCode = intent.getStringExtra(EXTRA_ITEM_CODE)

        itemCode?.let {
            viewModel.getItemMasterDetail(itemCode)
            viewModel.itemMasterDetail.observe(this, Observer {
                when (it.status) {
                    Status.SUCCESS -> {
                        loading_progressbar.visibility = View.GONE
                        it.data?.data?.get(0)?.let { detail ->
                            data = detail
                            setToolbar(data.itemNm, true)
                            updateData(data)
                        }
                    }
                    Status.LOADING -> {
                        loading_progressbar.visibility = View.VISIBLE
                    }
                    Status.ERROR -> {
                        loading_progressbar.visibility = View.GONE
                        Toast.makeText(this, it.message, Toast.LENGTH_LONG).show()
                    }
                }
            })
        } ?: Toast.makeText(this, R.string.str_error, Toast.LENGTH_LONG).show()
    }

    private fun updateData(data: ItemMasterDetailResponse.ItemMasterDetail) {
        tv_title.text = "${data.itemNo} - ${data.itemNm}"
        tv_itemSpec.text = data.itemSpec
        tv_itemUnit.text = data.whUnitNm
        initPager(data)
    }

    private fun initPager(data: ItemMasterDetailResponse.ItemMasterDetail) {
        val detailFragment = DetailFragment(data)
        val imagesFragment = ImagesFragment(data.itemCd)

        val pagerAdapter = PagerAdapter(supportFragmentManager, listFragments = listOf(detailFragment, imagesFragment))
        pagerAdapter.listTitles = listOf(R.string.str_detail.getString(), R.string.str_images.getString())

        viewpager.adapter = pagerAdapter
        viewpager.offscreenPageLimit = 2
        tab.setupWithViewPager(viewpager)
    }
}