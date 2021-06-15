package com.ziperp.dms.utils

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.res.Resources
import android.content.res.TypedArray
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.net.Uri
import android.text.TextUtils
import android.util.Base64
import android.util.TypedValue
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.android.gms.common.util.IOUtils
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.ziperp.dms.App
import com.ziperp.dms.R
import com.ziperp.dms.extensions.getString
import com.ziperp.dms.main.trackingreports.staff.model.StaffMarker
import com.ziperp.dms.main.visitcustomer.model.ItemMarker
import pub.devrel.easypermissions.EasyPermissions
import java.io.*
import java.text.DecimalFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.sqrt

open class AppUtil{
    companion object {

        fun calculateFileSize(filepath: String?): String? {
            //String filepathstr=filepath.toString();
            val file = File(filepath)
            val fileSizeInBytes = file.length()
            val fileSizeInKB: Long = fileSizeInBytes / 1024
            // Convert the KB to MegaBytes (1 MB = 1024 KBytes)
            val fileSizeInMB = fileSizeInKB / 1024
            return fileSizeInKB.toString() + "KB"
        }

        fun hasPermissionCall(context: Context): Boolean{
            return EasyPermissions.hasPermissions(context, Manifest.permission.CALL_PHONE)
        }


        fun loadImageFromURL(context: Context?, url: String?, imageView: ImageView?, enableCache: Boolean = false, defaultImg: Int = R.drawable.icon_image_default) {
            Glide.with(context!!)
                .load(url)
                .placeholder(defaultImg)
                .diskCacheStrategy(if (enableCache) DiskCacheStrategy.ALL else DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .error(defaultImg)
                .into(imageView!!)
        }


        fun loadImageFromLocal(context: Context?, absolutePath: String?, imageView: ImageView?, defaultImg: Int = R.drawable.icon_image_default) {
            val photoUri = Uri.fromFile(File(absolutePath))
            Glide.with(context!!)
                .load(photoUri)
                .placeholder(defaultImg)
                .error(defaultImg)
                .into(imageView!!);

        }


        fun loadImageFromData(context: Context, data: String, imageView: ImageView) {
            val imageByteArray = Base64.decode(data, Base64.NO_WRAP)
            Glide.with(context)
                .asBitmap()
                .load(imageByteArray)
                .placeholder(R.drawable.avata_default)
                .error(R.drawable.avata_default)
                .into(imageView)
        }

        fun replaceFragmentToActivity(
            fragmentManager: FragmentManager,
            fragment: Fragment, frameId: Int, onBackStack: Boolean = false, tag: String? = null
        ) {
            val transaction = fragmentManager.beginTransaction().setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
            //transaction.setCustomAnimations(R.anim.attack_fragment,R.anim.detack_fragment,R.anim.backstack_in,R.anim.backstack_out);
            transaction.replace(frameId, fragment, tag)
            if (onBackStack) {
                transaction.addToBackStack(null)
            }
            transaction.commit()
        }

        fun addFragmentToActivity(
            fragmentManager: FragmentManager,
            fragment: Fragment, frameId: Int, onBackstack: Boolean, tag: String? = null
        ) {
            val currentFr = fragmentManager.findFragmentById(frameId)
            val transaction = fragmentManager.beginTransaction().setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
            if (currentFr != null) transaction.hide(currentFr)
            transaction.add(frameId, fragment, tag)
            if (onBackstack) {
                transaction.addToBackStack(tag)
            }
            transaction.commit()
        }

        fun addFragmentsToActivity(
            fragmentManager: FragmentManager,
            appearFragment: Fragment,
            fragments: List<Fragment>,
            frameId: Int,
            tag: String?
        ) {
            val transaction = fragmentManager.beginTransaction()
            transaction.add(frameId, appearFragment, tag)
            for (fragment in fragments) {
                if (appearFragment === fragment) {
                    transaction.show(appearFragment)
                } else {
                    transaction.hide(fragment)
                }
            }
            transaction.commit()
        }

        fun replaceFragmentWithAnimationSlide(
            fragmentManager: FragmentManager,
            containerId: Int,
            fragmentToShow: Fragment,
            addBackStack: Boolean = true,
            animateSlide: Boolean = true
        ){
            if (animateSlide){
                if (addBackStack){
                    fragmentManager.beginTransaction()
                        .setCustomAnimations(
                            R.anim.slide_in_right,
                            R.anim.slide_out_left,
                            R.anim.slide_in_left,
                            R.anim.slide_out_right
                        )
                        .replace(containerId, fragmentToShow)
                        .addToBackStack(null)
                        .setReorderingAllowed(true)
                        .commit()
                }else{
                    fragmentManager.beginTransaction()
                        .setCustomAnimations(
                            R.anim.slide_in_right,
                            R.anim.slide_out_left,
                            R.anim.slide_in_left,
                            R.anim.slide_out_right
                        )
                        .replace(containerId, fragmentToShow)
                        .disallowAddToBackStack()
                        .setReorderingAllowed(true)
                        .commit()
                }
            }else{
                if (addBackStack){
                    fragmentManager.beginTransaction()
                        .replace(containerId, fragmentToShow)
                        .addToBackStack(null)
                        .setReorderingAllowed(true)
                        .commit()
                }else{
                    fragmentManager.beginTransaction()
                        .replace(containerId, fragmentToShow)
                        .disallowAddToBackStack()
                        .setReorderingAllowed(true)
                        .commit()
                }
            }
        }

        @SuppressLint("SimpleDateFormat")
        fun convertTimeInMilisecondsToDate(time: Long): String? {
            val dateFomat = "yyyy-MM-dd"
            val format = SimpleDateFormat(dateFomat)
            val calendar = Calendar.getInstance()
            calendar.timeInMillis = time
            return format.format(calendar.time)
        }

        @SuppressLint("SimpleDateFormat")
        fun convertStringToDate(dateString: String): Date {
            return SimpleDateFormat("yyyy-MM-dd").parse(dateString)
        }

        fun getAddrLatLng(lat: String, lng: String): String {
            if(lat.isBlank() || lng.isBlank()) return ""
            val geocoder = Geocoder(App.shared(), Locale.getDefault())
            var addresses: List<Address>? =
                null // Here 1 represent max location result to returned, by documents it recommended 1 to 5
            try {
                addresses = geocoder.getFromLocation(lat.toDouble(), lng.toDouble(), 1)
                if (addresses.isNotEmpty()) {
                    return addresses[0].getAddressLine(0)
                }
            } catch (e: Exception) {
//                e.printStackTrace()
            }
            return ""
        }

        fun getAddrLatLng(context: Context?, lat: Double, lng: Double): String? {
            val geocoder = Geocoder(context, Locale.getDefault())
            var addresses: List<Address>? = null
            // Here 1 represent max location result to returned, by documents it recommended 1 to 5
            try {
                addresses = geocoder.getFromLocation(lat, lng, 1)
                if (addresses.isNotEmpty()) {
                    return addresses[0].getAddressLine(0)
                }
            } catch (e: Exception) {
//                e.printStackTrace()
            }
            return null
        }

        fun showAlertDialog(
            context: Context?,
            message: String,
            doYes: () -> Unit,
            doNo: () -> Unit = {}
        ) {
            val alertDialog = AlertDialog.Builder(context)

            alertDialog.apply {
                setMessage(message)
                setCancelable(false)
                setPositiveButton(R.string.str_yes.getString()) { _, _ -> doYes.invoke() }
                setNegativeButton(R.string.str_no.getString()) { _, _ -> doNo.invoke() }
                show()
            }
        }

        fun formatDateString(date: String) : String{
            return if(date.length == 8){
                "${date[6]}${date[7]}/${date[4]}${date[5]}/${date[0]}${date[1]}${date[2]}${date[3]}"
            }else {
                ""
            }
        }

        fun distanceBetweenTwoPointInKm(lat1: Double, long1: Double, lat2: Double, long2: Double) : Float{
            val loc1 = Location("")
            loc1.latitude = lat1
            loc1.longitude = long1
            val loc2 = Location("")
            loc2.latitude = lat2
            loc2.longitude = long2
            return loc1.distanceTo(loc2)/1000
        }

        fun distanceBetweenTwoPointInMeter(lat1: Double, long1: Double, lat2: Double, long2: Double) : Float{
            val loc1 = Location("")
            loc1.latitude = lat1
            loc1.longitude = long1
            val loc2 = Location("")
            loc2.latitude = lat2
            loc2.longitude = long2
            return loc1.distanceTo(loc2)
        }

        fun distanceBetweenTwoPointInMeter(location1: Location, location2: Location) : Float{
            return location1.distanceTo(location2)
        }

        fun commasValue(value: String) : String {
            return if(value.isNotEmpty()){
                val amount: Double = value.toDouble()
                val formatter = DecimalFormat("#,###.00")
                formatter.format(amount)
            }else {
                ""
            }
        }

        fun commasValueNo(value: String) : String {
            return if(value.isNotEmpty()){
                val amount: Double = value.toDouble()
                val formatter = DecimalFormat("#,###")
                formatter.format(amount)
            }else {
                ""
            }
        }

        fun reformatDate(date: String) : String {
            val myFormat = SimpleDateFormat("dd/MM/yyyy")
            val format = SimpleDateFormat("yyyyMMdd")
            return try {
                myFormat.format(format.parse(date)).toString()
            } catch (e: ParseException) {
                e.printStackTrace()
                "null"
            }
        }

        fun reformatAttachedDate(v: String) : String{
            val myFormat = SimpleDateFormat("dd/MM/yyyy hh:mm")
            val format = SimpleDateFormat("yyyyMMddhhmm")
            return try {
                myFormat.format(format.parse(v)).toString()
            } catch (e: ParseException) {
                e.printStackTrace()
                "null"
            }
        }

        fun reformatDateSheet(date: String): String {
            val myFormat = SimpleDateFormat("yyyy/MM/dd")
            val format = SimpleDateFormat("yyyy/M/d")
            return try {
                myFormat.format(format.parse(date)).toString()
            } catch (e: ParseException) {
                e.printStackTrace()
                date
            }
        }

        fun reformatTimeSheet(time: String): String {
            val myFormat = SimpleDateFormat("mm:ss")
            val format = SimpleDateFormat("m:s")
            return try {
                myFormat.format(format.parse(time)).toString()
            } catch (e: ParseException) {
                e.printStackTrace()
                time
            }
        }

        fun dpToPx(dp: Float): Int {
            return (dp * Resources.getSystem().displayMetrics.density).toInt()
        }

        fun getMultipleAttr(context: Context?, typed: TypedArray, index: Int): String? {
            val value = TypedValue()
            typed.getValue(index, value)
            return if (value.type == TypedValue.TYPE_REFERENCE) {
                context?.getString(typed.getResourceId(index, 0))
            } else {
                typed.getString(index)
            }
        }

        fun getBitmap(drawableRes: Int, context: Context): Bitmap? {
            val drawable: Drawable = context.resources.getDrawable(drawableRes, null)
            val canvas = Canvas()
            val bitmap: Bitmap = Bitmap.createBitmap(
                drawable.intrinsicWidth,
                drawable.intrinsicHeight,
                Bitmap.Config.ARGB_8888
            )
            canvas.setBitmap(bitmap)
            drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight())
            drawable.draw(canvas)
            return bitmap
        }

        fun getFocusStaffPoint(listItem: MutableList<StaffMarker>) : LatLngBounds{
            val latLngBounds = LatLngBounds.Builder()
            listItem.forEach{
                latLngBounds.include(LatLng(it.lat, it.lng))
            }
            return latLngBounds.build()
        }

        fun getFocusPoint2(listItem: MutableList<ItemMarker>) : LatLngBounds{
            val latLngBounds = LatLngBounds.Builder()
            listItem.forEach{
                latLngBounds.include(LatLng(it.lat, it.lng))
            }
            return latLngBounds.build()
        }

        fun getFocusPoint(listItem: MutableList<ItemMarker>) : LatLngBounds{

            var maxLat: Double = listItem[0].lat
            var minLat: Double = listItem[0].lat
            var maxLong: Double = listItem[0].lng
            var minLong: Double = listItem[0].lng
            var pointLatMax: ItemMarker = listItem[0]
            var pointLatMin: ItemMarker = listItem[0]
            var pointLongMax: ItemMarker = listItem[0]
            var pointLongMin: ItemMarker = listItem[0]

            var focusPoint : LatLngBounds = LatLngBounds.builder().include(LatLng(maxLat, maxLong)).include(
                LatLng(
                    maxLat,
                    maxLong
                )
            ).build()

            for (i in 1 until listItem.size) {
                if(listItem[i].lat > maxLat) {
                    maxLat = listItem[i].lat
                    pointLatMax = listItem[i]
                }
                if(listItem[i].lat < minLat) {
                    minLat = listItem[i].lat
                    pointLatMax = listItem[i]
                }
                if(listItem[i].lng > maxLong) {
                    maxLong = listItem[i].lng
                    pointLongMax = listItem[i]
                }
                if(listItem[i].lng < minLong) {
                    minLong = listItem[i].lng
                    pointLongMin = listItem[i]
                }
            }
            if(sqrt(maxLat - minLat) > sqrt(maxLong - minLong)) {
                focusPoint = LatLngBounds.builder().include(
                    LatLng(
                        pointLatMax.lat,
                        pointLatMax.lng
                    )
                ).include(LatLng(pointLatMin.lat, pointLatMin.lng)).build()
            } else {
                focusPoint = LatLngBounds.builder().include(
                    LatLng(
                        pointLongMax.lat,
                        pointLongMax.lng
                    )
                ).include(LatLng(pointLongMin.lat, pointLongMin.lng)).build()
            }

            return focusPoint
        }

        fun duration(HHmmss1: String, HHmmss2: String): Double{
            try{
                val date1 = SimpleDateFormat("HHmmss", Locale.US).parse(HHmmss1) as Date
                val date2 = SimpleDateFormat("HHmmss", Locale.US).parse(HHmmss2) as Date
                return (date2.time - date1.time).toDouble()/(3600*1000)
            }catch (e: Exception){
                return 0.0
            }
        }

        fun showSyncConfirm(context: Context?, completion: (()-> Unit)? = null){
            val alertDialog = AlertDialog.Builder(context)
            alertDialog.setTitle("Synchronization")
            alertDialog.setMessage("You need to sync before doing this action!")
            alertDialog.setPositiveButton("Sync Now") { _, _ ->
                completion?.invoke()
            }
            alertDialog.setNegativeButton("Cancel") { dialog, _ -> dialog.cancel() }
            alertDialog.show()
        }

        fun getFilePathFromURI(context: Context, contentUri: Uri): String? {
            val tempPath = context.filesDir.absolutePath
            val fileName = getFileName(contentUri)
            if (!TextUtils.isEmpty(fileName)) {
                val copyFile = File(tempPath + File.separator.toString() + fileName)
                copy(context, contentUri, copyFile)
                return copyFile.absolutePath
            }
            return null
        }

        private fun getFileName(uri: Uri?): String? {
            if (uri == null) return null
            val path = uri.path
            return path!!.substringAfterLast("/")
        }

        private fun copy(context: Context, srcUri: Uri, dstFile: File?) {
            try {
                val inputStream: InputStream = context.contentResolver.openInputStream(srcUri)
                    ?: return
                val outputStream: OutputStream = FileOutputStream(dstFile)
                IOUtils.copyStream(inputStream, outputStream)
                inputStream.close()
                outputStream.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }


}