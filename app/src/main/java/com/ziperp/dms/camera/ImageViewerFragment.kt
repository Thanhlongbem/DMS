package com.ziperp.dms.camera

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.widget.ImageView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.ziperp.dms.R
import com.ziperp.dms.camera.helper.GenericListAdapter
import com.ziperp.dms.camera.helper.decodeExifOrientation
import com.ziperp.dms.extensions.getString
import com.ziperp.dms.extensions.toSlashDateTimeString
import com.ziperp.dms.utils.AppUtil
import com.ziperp.dms.utils.LogUtils
import kotlinx.android.synthetic.main.fragment_upload_image.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.BufferedInputStream
import java.io.File
import java.util.*
import kotlin.math.max


class ImageViewerFragment : Fragment() {

    /** Default Bitmap decoding options */
    private val bitmapOptions = BitmapFactory.Options().apply {
        inJustDecodeBounds = false
        // Keep Bitmaps at less than 1 MP
        if (max(outHeight, outWidth) > DOWNSAMPLE_SIZE) {
            val scaleFactorX = outWidth / DOWNSAMPLE_SIZE + 1
            val scaleFactorY = outHeight / DOWNSAMPLE_SIZE + 1
            inSampleSize = max(scaleFactorX, scaleFactorY)
        }
    }

    /** Bitmap transformation derived from passed arguments */
    private val bitmapTransformation: Matrix by lazy { decodeExifOrientation(orientation) }

    /** Flag indicating that there is depth data available for this image */
    private var isDepth: Boolean = false
    private var absolutePath: String = ""
    private var orientation: Int = 0

    /** Data backing our Bitmap viewpager */
    private val bitmapList: MutableList<Bitmap> = mutableListOf()

    private fun imageViewFactory() = ImageView(requireContext()).apply {
        layoutParams = ViewGroup.LayoutParams(MATCH_PARENT, MATCH_PARENT)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            absolutePath = it.getString("absolutePath", "")
            orientation = it.getInt("orientation")
            isDepth = it.getBoolean("isDepth")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_upload_image, container, false);
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (absolutePath.isEmpty()) return

        LogUtils.d("absolutePath of image = $absolutePath")
        setToolbar(R.string.str_upload_image.getString(), true)
        //Get file data
        edt_fileName.setText(absolutePath.substringAfterLast("/").substringBeforeLast("."))
        val options = BitmapFactory.Options().apply { inJustDecodeBounds = true }
        BitmapFactory.decodeFile(absolutePath, options)

        val size = options.inSampleSize
        tv_imageInfo.text = "${Date().toSlashDateTimeString()} - ${options.outHeight} x ${options.outHeight} - ${AppUtil.calculateFileSize(absolutePath)}"

        img_imageUpload.apply {
            offscreenPageLimit = 2
            adapter = GenericListAdapter(
                bitmapList,
                itemViewFactory = { imageViewFactory() }) { view, item, _ ->
                view as ImageView
                view.scaleType = ImageView.ScaleType.CENTER_CROP
                Glide.with(view).load(item).into(view)
            }
        }
        lifecycleScope.launch(Dispatchers.IO) {

            // Load input image file
            val inputBuffer = loadInputBuffer()

            // Load the main JPEG image
            addItemToViewPager(img_imageUpload, decodeBitmap(inputBuffer, 0, inputBuffer.size))

            // If we have depth data attached, attempt to load it
            if (isDepth) {
                try {
                    val depthStart = findNextJpegEndMarker(inputBuffer, 2)
                    addItemToViewPager(img_imageUpload, decodeBitmap(
                            inputBuffer, depthStart, inputBuffer.size - depthStart))

                    val confidenceStart = findNextJpegEndMarker(inputBuffer, depthStart)
                    addItemToViewPager(img_imageUpload, decodeBitmap(
                            inputBuffer, confidenceStart, inputBuffer.size - confidenceStart))

                } catch (exc: RuntimeException) {
                    Log.e(TAG, "Invalid start marker for depth or confidence data")
                }
            }
        }
    }

    /** Utility function used to read input file into a byte array */
    private fun loadInputBuffer(): ByteArray {
        val inputFile = File(absolutePath)
        return BufferedInputStream(inputFile.inputStream()).let { stream ->
            ByteArray(stream.available()).also {
                stream.read(it)
                stream.close()
            }
        }
    }

    /** Utility function used to add an item to the viewpager and notify it, in the main thread */
    private fun addItemToViewPager(view: ViewPager2, item: Bitmap) = view.post {
        bitmapList.add(item)
        view.adapter?.notifyDataSetChanged()
    }

    /** Utility function used to decode a [Bitmap] from a byte array */
    private fun decodeBitmap(buffer: ByteArray, start: Int, length: Int): Bitmap {

        // Load bitmap from given buffer
        val bitmap = BitmapFactory.decodeByteArray(buffer, start, length, bitmapOptions)

        // Transform bitmap orientation using provided metadata
        return Bitmap.createBitmap(
                bitmap, 0, 0, bitmap.width, bitmap.height, bitmapTransformation, true)
    }

    fun setToolbar(title: String, isHomeUpEnable: Boolean) {
        toolbar.z = 1000F
        toolbar_title.z = -1000F
        toolbar?.let {
            (activity as AppCompatActivity?)!!.setSupportActionBar(it)
            it.setNavigationOnClickListener { activity?.finish() }
            toolbar_title.text = title
        }
        (activity as AppCompatActivity?)!!.supportActionBar?.let {
            it.title = ""
            it.setDisplayHomeAsUpEnabled(isHomeUpEnable)
            it.setDisplayShowHomeEnabled(isHomeUpEnable)
        }
    }

    fun getNewFileName(): String {
        return edt_fileName.text.toString() + "." + absolutePath.substringAfterLast(".")
    }

    fun getFileNote(): String {
        return edt_fileNoteContent.text.toString()
    }


    companion object {
        private val TAG = ImageViewerFragment::class.java.simpleName


        fun newInstance(absolutePath: String, orientation: Int, isDepth: Boolean): ImageViewerFragment{
            val args = Bundle()
            args.putString("absolutePath", absolutePath)
            args.putInt("orientation", orientation)
            args.putBoolean("isDepth", isDepth)

            val fragment = ImageViewerFragment()
            fragment.arguments = args
            return fragment
        }
        /** Maximum size of [Bitmap] decoded */
        private const val DOWNSAMPLE_SIZE: Int = 1024  // 1MP

        /** These are the magic numbers used to separate the different JPG data chunks */
        private val JPEG_DELIMITER_BYTES = arrayOf(-1, -39)

        /**
         * Utility function used to find the markers indicating separation between JPEG data chunks
         */
        private fun findNextJpegEndMarker(jpegBuffer: ByteArray, start: Int): Int {

            // Sanitize input arguments
            assert(start >= 0) { "Invalid start marker: $start" }
            assert(jpegBuffer.size > start) {
                "Buffer size (${jpegBuffer.size}) smaller than start marker ($start)" }

            // Perform a linear search until the delimiter is found
            for (i in start until jpegBuffer.size - 1) {
                if (jpegBuffer[i].toInt() == JPEG_DELIMITER_BYTES[0] &&
                        jpegBuffer[i + 1].toInt() == JPEG_DELIMITER_BYTES[1]) {
                    return i + 2
                }
            }

            // If we reach this, it means that no marker was found
            throw RuntimeException("Separator marker not found in buffer (${jpegBuffer.size})")
        }
    }
}
