package com.ziperp.dms.camera

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageFormat
import android.hardware.camera2.CameraCharacteristics
import android.hardware.camera2.CameraManager
import android.hardware.camera2.CameraMetadata
import android.os.Handler
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.ziperp.dms.*
import com.ziperp.dms.base.BaseActivity
import com.ziperp.dms.core.rest.*
import com.ziperp.dms.extensions.format
import com.ziperp.dms.extensions.getViewModel
import com.ziperp.dms.extensions.toDateTimeString
import com.ziperp.dms.main.customer.customerdetail.model.CustomerDetail
import com.ziperp.dms.utils.AppUtil
import com.ziperp.dms.utils.LogUtils
import kotlinx.android.synthetic.main.activity_camera.*
import pub.devrel.easypermissions.AfterPermissionGranted
import pub.devrel.easypermissions.EasyPermissions
import java.io.File
import java.util.*


class CameraActivity : BaseActivity() {
    var absolutePath: String = ""
    var isBackCamera: Boolean = false
    var cstNo = ""
    var isVisitCustomer = true
    var curFragment: Fragment = CameraFragment.newInstance(CameraFragment.CAMERA_BACK)

    private val viewModel by lazy {
        getViewModel { Injection.provideImageViewModel() }
    }

    override fun setLayoutId(): Int = R.layout.activity_camera

    override fun initView() {

        cstNo = intent.getStringExtra(CST_VISIT_NO) ?: ""
        isVisitCustomer =  intent.getBooleanExtra(IS_VISIT_CUSTOMER, false)

        cameraTask()

        img_switchCamera.setOnClickListener{
            img_switchCamera.isEnabled = false
            if(isBackCamera){
                invokeFrontCamera()
            } else {
                invokeBackCamera()
            }
        }
        but_upload.setOnClickListener {
            //resize image
            uploadImage()
        }
    }

    fun resizeImage(file: File, scaleTo: Int = 1024) {
        if(absolutePath.isNotEmpty()){
            val bmOptions = BitmapFactory.Options()
            bmOptions.inJustDecodeBounds = true
            BitmapFactory.decodeFile(file.absolutePath, bmOptions)
            val photoW = bmOptions.outWidth
            val photoH = bmOptions.outHeight

            // Determine how much to scale down the image
            val scaleFactor = Math.min(photoW / scaleTo, photoH / scaleTo)

            bmOptions.inJustDecodeBounds = false
            bmOptions.inSampleSize = scaleFactor

            val resized = BitmapFactory.decodeFile(file.absolutePath, bmOptions) ?: return
            file.outputStream().use {
                resized.compress(Bitmap.CompressFormat.JPEG, 75, it)
                resized.recycle()
            }
        }
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (hasFocus) {
            window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_FULLSCREEN
                    or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)
        }
    }


    private fun uploadImage() {
        if (absolutePath.isNotEmpty() && cstNo.isNotEmpty()){
            LogUtils.d("Upload image $absolutePath")
            val oldFile: File = applicationContext.getFileStreamPath(absolutePath.substringAfterLast("/", ""))
            if(AppConfiguration.IMAGE_QUALITY == "1"){
                resizeImage(oldFile)
            }

            val newFile: File = applicationContext.getFileStreamPath((curFragment as ImageViewerFragment).getNewFileName())
            oldFile.renameTo(newFile)

            val length = (newFile.length()/ 1024).toInt().format()

            val customerImage = CustomerImage()
            customerImage.keyNo = cstNo
            customerImage.fileNote = (curFragment as ImageViewerFragment).getFileNote()
            customerImage.isVisitCustomer = isVisitCustomer
            customerImage.localPath = newFile.absolutePath
            customerImage.fileNm = (curFragment as ImageViewerFragment).getNewFileName()
            customerImage.attachedDate = Date().toDateTimeString()
            customerImage.fileLength = "${length}KB"

            viewModel.uploadFile2(customerImage)
            viewModel.uploadFileLiveData2.observe(this, Observer { response ->
                when (response.status) {
                    Status.LOADING -> loading_progressbar.visibility = View.VISIBLE
                    Status.SUCCESS -> {
                        loading_progressbar.visibility = View.GONE

                        val cudData = response.data!!
//                        val customerDetail = cudData.data as CustomerDetail
                        when (cudData.status) {
                            CUD_SUCCESS -> {
                                showNotificationBanner(NotificationType.SUCCESS, cudData.message)

                            }
                            CUD_CONFLICT -> {
                                showNotificationBanner(NotificationType.ERROR, cudData.message)
                            }
                            CUD_OFFLINE -> {
                                showNotificationBanner(NotificationType.INFO, cudData.message)

                            }
                            CUD_ERROR -> {
                                showNotificationBanner(NotificationType.ERROR, cudData.message)
                            }
                        }

                        setResult(RESULT_OK)
                        finish()
                    }
                    Status.ERROR -> {
                        loading_progressbar.visibility = View.GONE
                        showNotificationBanner(
                            NotificationType.ERROR,
                            "Could not create customer right now!"
                        )
                    }
                }
            })
        }


    }

    @AfterPermissionGranted(RC_CAMERA_PERM)
    fun cameraTask(){
        if (hasCameraPermission()) {
            invokeBackCamera()
        } else {
            // Ask for one permission
            EasyPermissions.requestPermissions(
                this,
                "Allow access camera to capture image",
                RC_CAMERA_PERM,
                Manifest.permission.CAMERA);
        }
    }


    fun switchImageScreen(absolutePath: String, orientation: Int, isDepth: Boolean) {
        this.absolutePath = absolutePath
        curFragment = ImageViewerFragment.newInstance(absolutePath, orientation, isDepth)
        AppUtil.replaceFragmentToActivity(supportFragmentManager,
            curFragment, R.id.fragment_container, true)
        img_switchCamera.visibility = View.GONE
        but_upload.visibility = View.VISIBLE
    }


    private fun hasCameraPermission(): Boolean {
        return EasyPermissions.hasPermissions(this, Manifest.permission.CAMERA)
    }


    override fun onResume() {
        super.onResume()
        // Before setting full screen flags, we must wait a bit to let UI settle; otherwise, we may
        // be trying to set app to immersive mode before it's ready and the flags do not stick
        fragment_container.postDelayed({
            fragment_container.systemUiVisibility = FLAGS_FULLSCREEN
        }, IMMERSIVE_FLAG_TIMEOUT)
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount > 0) {
            supportFragmentManager.popBackStack()
            but_upload.visibility = View.GONE
        } else {
            finish()
        }
    }


    private fun invokeBackCamera() {
        isBackCamera = true
        but_upload.visibility = View.GONE
        img_switchCamera.visibility = View.VISIBLE
        curFragment = CameraFragment.newInstance(CameraFragment.CAMERA_BACK)
        AppUtil.replaceFragmentToActivity(supportFragmentManager,
            curFragment, R.id.fragment_container)
        Handler().postDelayed({
            img_switchCamera.isEnabled = true
        }, 500)
    }

    private fun invokeFrontCamera() {
        isBackCamera = false
        but_upload.visibility = View.GONE
        curFragment = CameraFragment.newInstance(CameraFragment.CAMERA_FRONT)
        AppUtil.replaceFragmentToActivity(supportFragmentManager,
            curFragment, R.id.fragment_container)
        Handler().postDelayed({
            img_switchCamera.isEnabled = true
        }, 500)
    }


    companion object {
        const val RC_CAMERA_PERM = 123
        const val CAMERA_FRONT = "1"
        const val CAMERA_BACK = "0"
        const val CST_VISIT_NO = "CST_VISIT_NO"
        const val IS_VISIT_CUSTOMER = "IS_VISIT_CUSTOMER"
        val PERMISSIONS_REQUIRED = arrayOf(Manifest.permission.CAMERA)

        fun newInstance(cstVisitNo: String, isVisitCustomer: Boolean, context: Context?): Intent? {
            val intent = Intent(context, CameraActivity::class.java)
            intent.putExtra(CST_VISIT_NO, cstVisitNo)
            intent.putExtra(IS_VISIT_CUSTOMER, isVisitCustomer)
            return intent
        }

        /** Combination of all flags required to put activity into immersive mode */
        const val FLAGS_FULLSCREEN =
            View.SYSTEM_UI_FLAG_LOW_PROFILE or
                    View.SYSTEM_UI_FLAG_FULLSCREEN or
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
//                    View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY

        /** Milliseconds used for UI animations */
        const val ANIMATION_FAST_MILLIS = 50L
        const val ANIMATION_SLOW_MILLIS = 100L
        private const val IMMERSIVE_FLAG_TIMEOUT = 500L

        /** Convenience method used to check if all permissions required by this app are granted */
        fun hasPermissions(context: Context) = PERMISSIONS_REQUIRED.all {
            ContextCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED
        }

        /** Helper class used as a data holder for each selectable camera format item */
        data class FormatItem(val title: String, val cameraId: String, val format: Int)

        /** Helper function used to convert a lens orientation enum into a human-readable string */
        private fun lensOrientationString(value: Int) = when(value) {
            CameraCharacteristics.LENS_FACING_BACK -> "Back"
            CameraCharacteristics.LENS_FACING_FRONT -> "Front"
            CameraCharacteristics.LENS_FACING_EXTERNAL -> "External"
            else -> "Unknown"
        }

        /** Helper function used to list all compatible cameras and supported pixel formats */
        @SuppressLint("InlinedApi")
        private fun enumerateCameras(cameraManager: CameraManager): List<FormatItem> {
            val availableCameras: MutableList<FormatItem> = mutableListOf()

            // Get list of all compatible cameras
            val cameraIds = cameraManager.cameraIdList.filter {
                val characteristics = cameraManager.getCameraCharacteristics(it)
                val capabilities = characteristics.get(
                    CameraCharacteristics.REQUEST_AVAILABLE_CAPABILITIES)
                capabilities?.contains(
                    CameraMetadata.REQUEST_AVAILABLE_CAPABILITIES_BACKWARD_COMPATIBLE) ?: false
            }


            // Iterate over the list of cameras and return all the compatible ones
            cameraIds.forEach { id ->
                val characteristics = cameraManager.getCameraCharacteristics(id)
                val orientation = lensOrientationString(
                    characteristics.get(CameraCharacteristics.LENS_FACING)!!)

                // Query the available capabilities and output formats
                val capabilities = characteristics.get(
                    CameraCharacteristics.REQUEST_AVAILABLE_CAPABILITIES)!!
                val outputFormats = characteristics.get(
                    CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP)!!.outputFormats

                // All cameras *must* support JPEG output so we don't need to check characteristics
                availableCameras.add(FormatItem(
                    "$orientation JPEG ($id)", id, ImageFormat.JPEG))

                // TODO: Return cameras that support RAW capability
//                if (capabilities.contains(
//                        CameraCharacteristics.REQUEST_AVAILABLE_CAPABILITIES_RAW) &&
//                    outputFormats.contains(ImageFormat.RAW_SENSOR)) {
//                    availableCameras.add(FormatItem(
//                        "$orientation RAW ($id)", id, ImageFormat.RAW_SENSOR))
//                }

                // TODO: Return cameras that support JPEG DEPTH capability
//                if (capabilities.contains(
//                        CameraCharacteristics.REQUEST_AVAILABLE_CAPABILITIES_DEPTH_OUTPUT) &&
//                    outputFormats.contains(ImageFormat.DEPTH_JPEG)) {
//                    availableCameras.add(FormatItem(
//                        "$orientation DEPTH ($id)", id, ImageFormat.DEPTH_JPEG))
//                }
            }

            return availableCameras
        }
    }


}