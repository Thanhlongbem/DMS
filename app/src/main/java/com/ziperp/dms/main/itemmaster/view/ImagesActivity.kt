package com.ziperp.dms.main.itemmaster.view

import android.view.View
import androidx.viewpager.widget.ViewPager
import com.ziperp.dms.R
import com.ziperp.dms.base.BaseActivity
import com.ziperp.dms.base.BaseCUDActivity.Companion.CREATE_MODE
import com.ziperp.dms.base.PagerAdapter
import com.ziperp.dms.camera.ImageFragment
import com.ziperp.dms.extensions.getString
import com.ziperp.dms.main.itemmaster.model.ItemMasterImageResponse
import kotlinx.android.synthetic.main.activity_item_master_image.*

class ImagesActivity: BaseActivity() {

    lateinit var listImage: ArrayList<ItemMasterImageResponse.DataImage>
    lateinit var currentImage: ItemMasterImageResponse.DataImage

    companion object {
        const val EXTRA_IMAGE_LIST = "EXTRA_IMAGE_LIST"
        const val EXTRA_POSITION = "EXTRA_POSITION"
    }

    override fun setLayoutId(): Int = R.layout.activity_item_master_image

    override fun initView() {
        listImage = intent.getSerializableExtra(EXTRA_IMAGE_LIST) as ArrayList<ItemMasterImageResponse.DataImage>
        initPager()
        val position = intent.getIntExtra(EXTRA_POSITION, 0)
        updateData(position)
    }

    private fun initPager() {
        val listFragments = listImage.map { image -> ImageFragment(image.download, false) }
        val pagerAdapter = PagerAdapter(supportFragmentManager, CREATE_MODE, listFragments)
        pagerAdapter.listTitles = emptyList()
        imagePager.adapter = pagerAdapter
        imagePager.currentItem = intent.getIntExtra(EXTRA_POSITION, 0)
        imagePager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener{
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}
            override fun onPageSelected(currentPosition: Int) { updateData(currentPosition) }
            override fun onPageScrollStateChanged(state: Int) {}
        })

        btnPrevious.setOnClickListener { if (imagePager.currentItem > 0) imagePager.currentItem-- }
        btnNext.setOnClickListener { if (imagePager.currentItem < listImage.size - 1) imagePager.currentItem++ }
    }

    private fun updateData(position: Int) {
        setToolbar("${R.string.str_images.getString()} ${position + 1}/${listImage.size}", true)
        currentImage = listImage[position]
        btnNext.visibility = if (position < listImage.size - 1) View.VISIBLE else View.GONE
        btnPrevious.visibility = if (position > 0) View.VISIBLE else View.GONE
        tv_fileName.text = currentImage.photoFileNm.substringBeforeLast(".")
    }

}