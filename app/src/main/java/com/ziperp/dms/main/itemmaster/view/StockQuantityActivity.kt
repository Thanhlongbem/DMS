package com.ziperp.dms.main.itemmaster.view

import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayoutMediator
import com.ziperp.dms.R
import com.ziperp.dms.base.BaseActivity
import com.ziperp.dms.extensions.format
import com.ziperp.dms.extensions.getString
import com.ziperp.dms.main.itemmaster.adapter.SQPagerAdapter
import com.ziperp.dms.main.itemmaster.view.ItemMasterActivity.Companion.EXTRA_ITEM_CODE
import com.ziperp.dms.main.itemmaster.view.ItemMasterActivity.Companion.EXTRA_ITEM_LS_CTR
import com.ziperp.dms.main.itemmaster.view.ItemMasterActivity.Companion.EXTRA_ITEM_NAME
import com.ziperp.dms.main.itemmaster.view.ItemMasterActivity.Companion.EXTRA_ITEM_UNIT
import kotlinx.android.synthetic.main.activity_stock_quantity.*

class StockQuantityActivity : BaseActivity() {
    private var stockQuantityFragment: StockQuantityFragment? = null
    private var stockQuantityByLotSerialFragment: StockQuantityByLotSerialFragment? = null
    private lateinit var pagerAdapter: SQPagerAdapter

    override fun setLayoutId(): Int = R.layout.activity_stock_quantity

    override fun initView() {
        setToolbar(R.string.str_stock_quantity.getString(), true)
        tv_nameStockQty.text = intent.getStringExtra(EXTRA_ITEM_NAME)
        tv_stockUnit.text = intent.getStringExtra(EXTRA_ITEM_UNIT)
        tv_lot_serial_control.text = intent.getStringExtra(EXTRA_ITEM_LS_CTR)
        initPagerAdapter()
    }

    private fun initPagerAdapter(){
        stockQuantityFragment = StockQuantityFragment(intent.getStringExtra(EXTRA_ITEM_CODE))
        stockQuantityByLotSerialFragment = StockQuantityByLotSerialFragment(intent.getStringExtra(EXTRA_ITEM_CODE))
        pagerAdapter = SQPagerAdapter(this, listFragments = listOf(stockQuantityFragment, stockQuantityByLotSerialFragment) as List<Fragment>)
        val listTitles  = listOf(R.string.str_stock_qty_dot.getString(), R.string.str_stock_qty_by_ls.getString())
        viewpager.adapter = pagerAdapter
        viewpager.offscreenPageLimit = 2
        viewpager.isUserInputEnabled = false

        TabLayoutMediator(tab, viewpager) { tab, position ->
            tab.text = listTitles[position]
            viewpager.setCurrentItem(tab.position, true)
        }.attach()
    }

    fun setSum(sum: Double){
        tvSumStockQuantity.text = sum.format()
    }
}