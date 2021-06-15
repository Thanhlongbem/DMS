package com.ziperp.dms.main.customer.customerimage

import android.annotation.SuppressLint
import android.view.Menu
import android.view.MenuItem
import androidx.recyclerview.widget.LinearLayoutManager
import com.ziperp.dms.Injection
import com.ziperp.dms.R
import com.ziperp.dms.base.BaseActivity
import com.ziperp.dms.camera.CameraActivity
import com.ziperp.dms.camera.ImagesFragment
import com.ziperp.dms.core.rest.Constants
import com.ziperp.dms.extensions.getString
import com.ziperp.dms.extensions.getViewModel
import com.ziperp.dms.utils.AppUtil
import com.ziperp.dms.utils.LogUtils

class CustomerImageActivity: BaseActivity() {

    private val viewModel by lazy {
        getViewModel { Injection.provideImageViewModel() }
    }
    lateinit var adapter: CustomerImageAdapter
    var layoutManager = LinearLayoutManager(this)
    lateinit var customerCode : String

    override fun setLayoutId(): Int = R.layout.activity_customer_image

    @SuppressLint("SetTextI18n")
    override fun initView() {
        setToolbar(R.string.str_images.getString(), true)

        customerCode = intent.getStringExtra(Constants.EXTRA_CUSTOMER_CODE)
        val customerName = intent.getStringExtra(Constants.EXTRA_CUSTOMER_NAME)

        val imageFragment = ImagesFragment.newInstance(customerCode, false)
        AppUtil.addFragmentToActivity(supportFragmentManager, imageFragment, R.id.fragment_image, false, "")
//        viewModel.getListImage2(customerCode, false)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_setting, menu)
        menu?.findItem(R.id.action_upload)?.isVisible = true
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_upload -> {
                startActivity(CameraActivity.newInstance(customerCode, false, this))
            }
        }
        return true
    }

}