package com.ziperp.dms.camera

import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.lifecycle.Observer
import androidx.viewpager.widget.ViewPager
import com.ziperp.dms.Injection
import com.ziperp.dms.NotificationType
import com.ziperp.dms.R
import com.ziperp.dms.base.BaseActivity
import com.ziperp.dms.base.BaseCUDActivity
import com.ziperp.dms.base.BaseCUDActivity.Companion.CREATE_MODE
import com.ziperp.dms.base.BaseCUDActivity.Companion.UPDATE_MODE
import com.ziperp.dms.base.PagerAdapter
import com.ziperp.dms.core.rest.Status
import com.ziperp.dms.extensions.getString
import com.ziperp.dms.extensions.getViewModel
import com.ziperp.dms.extensions.toast
import com.ziperp.dms.showNotificationBanner
import com.ziperp.dms.utils.AppUtil
import com.ziperp.dms.utils.NetWorkConnection
import kotlinx.android.synthetic.main.activity_image_modify.*
import kotlinx.android.synthetic.main.activity_image_modify.btnNext
import kotlinx.android.synthetic.main.activity_image_modify.btnPrevious
import kotlinx.android.synthetic.main.activity_image_modify.imagePager
import kotlinx.android.synthetic.main.activity_image_modify.loading_progressbar

class ImageModifyActivity: BaseActivity() {

    lateinit var listImage: ArrayList<CustomerImage>
    lateinit var currentImage: CustomerImage

    companion object {
        const val EXTRA_VIEW_ONLY = "EXTRA_VIEW_ONLY"
        const val EXTRA_IMAGE_LIST = "EXTRA_IMAGE_LIST"
        const val EXTRA_POSITION = "EXTRA_POSITION"
        const val EXTRA_IS_VISIT_CUSTOMER = "EXTRA_IS_VISIT_CUSTOMER"
    }

    private val viewModel by lazy {
        getViewModel { Injection.provideImageViewModel() }
    }

    override fun setLayoutId(): Int = R.layout.activity_image_modify

    override fun initView() {
        listImage = intent.getSerializableExtra(EXTRA_IMAGE_LIST) as ArrayList<CustomerImage>
        initPager()
        val position = intent.getIntExtra(EXTRA_POSITION, 0)
        updateData(position)
        if (intent.getBooleanExtra(EXTRA_VIEW_ONLY, false)) {
            btn_delete.visibility = View.GONE
            edt_fileName.isEnabled = false
            edt_fileNoteContent.isEnabled = false
        }
        btn_delete.setOnClickListener {
            AppUtil.showAlertDialog(this, R.string.str_question_delete.getString(), {
                if(currentImage.isSynchonized){
                    if(NetWorkConnection.isNetworkAvailable()){
                        viewModel.updateImageInfo(generateRequest(BaseCUDActivity.DELETE_MODE))
                    }else{
                        "Could not support action in offline mode".toast()
                    }
                }else{
                    viewModel.deleteOfflineImage(currentImage)
                    setResult(200)
                    finish()
                }
            })
        }

        viewModel.saveFileLiveData.observe(this, Observer { response ->
            when(response.status) {
                Status.LOADING -> loading_progressbar.visibility = View.VISIBLE
                Status.SUCCESS -> {
                    response.data?.let {
                        if(it.isSuccess()){
                            showNotificationBanner(NotificationType.SUCCESS, it.message())
                        }else{
                            showNotificationBanner(NotificationType.ERROR, it.message())
                        }
                    }
                    setResult(200)
                    finish()
                }
                Status.ERROR -> showNotificationBanner(NotificationType.ERROR, response.data?.data?.get(0)?.errMsg?: "Something went wrong!")
            }
        })
    }

    private fun initPager() {
        val listFragments = listImage.map { image ->
            return@map if(!image.isSynchonized && image.localPath.isNotBlank()){
                ImageFragment(image.localPath, true)
            }else{
                ImageFragment(image.download, false)
            }
        }
        val pagerAdapter = PagerAdapter(supportFragmentManager, CREATE_MODE, listFragments)
        pagerAdapter.listTitles = emptyList()
        imagePager.adapter = pagerAdapter
        imagePager.currentItem = intent.getIntExtra(EXTRA_POSITION, 0)
        imagePager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener{
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}
            override fun onPageSelected(currentPosition: Int) {
                updateData(currentPosition)
            }
            override fun onPageScrollStateChanged(state: Int) {}
        })

        btnPrevious.setOnClickListener {
            if (imagePager.currentItem > 0) imagePager.currentItem--
        }
        btnNext.setOnClickListener {
            if (imagePager.currentItem < listImage.size - 1) imagePager.currentItem++
        }
    }

    private fun updateData(position: Int) {
        setToolbar("${R.string.str_images.getString()} ${position + 1}/${listImage.size}", true)
        currentImage = listImage[position]
        btnNext.visibility = if (position < listImage.size - 1) View.VISIBLE else View.GONE
        btnPrevious.visibility = if (position > 0) View.VISIBLE else View.GONE
        edt_fileName.setText(currentImage.fileNm.substringBeforeLast("."))
        edt_fileNoteContent.setText(currentImage.fileNote)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_setting, menu)
        menu?.findItem(R.id.action_save)?.isVisible = !intent.getBooleanExtra(EXTRA_VIEW_ONLY, false)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.action_save -> {
                if(currentImage.isSynchonized){
                    if(NetWorkConnection.isNetworkAvailable()){
                        viewModel.updateImageInfo(generateRequest(UPDATE_MODE))
                    }else{
                        "Could not support action in offline mode".toast()
                    }
                }else{// offline item
                    "Could not support action in offline mode".toast()
                }

            }
        }
        return true
    }

    private fun generateRequest(mode: Int): SaveToSQLFileRequest {
        val request = SaveToSQLFileRequest()
        request.apply {
            pPageMode = if (mode == UPDATE_MODE) "U" else "C"
            pSeq = currentImage.seq.toString()
            pKeyNo = currentImage.keyNo
            pFileNote = edt_fileNoteContent.text.toString()
            pFileType = currentImage.fileType.trim()
            pFileNm = edt_fileName.text.toString() + "." + currentImage.fileNm.substringAfterLast(".") // file type
            pFileNmOld = currentImage.fileNm
            pFileURL = currentImage.fileURL
            pIsSaveAfterUpload = if (mode == UPDATE_MODE) "0" else "1"
            pFileURLOld = currentImage.fileURL
            pFormId = if (intent.getBooleanExtra(EXTRA_IS_VISIT_CUSTOMER, true)) "frmDVisitCst" else "frmCAccount"
        }
        return request
    }
}