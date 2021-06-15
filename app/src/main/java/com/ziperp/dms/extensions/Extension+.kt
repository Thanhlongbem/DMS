package com.ziperp.dms.extensions

import android.annotation.SuppressLint
import android.app.Activity
import android.content.*
import android.content.res.Resources
import android.graphics.Typeface
import android.net.Uri
import android.os.SystemClock
import android.text.TextUtils
import android.util.DisplayMetrics
import android.view.Gravity
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.Dimension
import com.google.gson.Gson
import com.jakewharton.rxbinding2.view.clicks
import com.ziperp.dms.App
import com.ziperp.dms.R
import com.ziperp.dms.core.rest.Constants
import com.ziperp.dms.utils.LogUtils
import com.ziperp.dms.utils.NetWorkConnection
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.math.BigDecimal
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.concurrent.TimeUnit

fun Context.toActivity(): Activity? {
    var context = this
    while (context is ContextWrapper) {
        if (context is Activity) {
            return context
        }
        context = context.baseContext
    }
    return null
}

@JvmOverloads @Dimension(unit = Dimension.PX) fun Number.dpToPx(
    metrics: DisplayMetrics = Resources.getSystem().displayMetrics
): Float {
    return toFloat() * metrics.density
}

@JvmOverloads @Dimension(unit = Dimension.DP) fun Number.pxToDp(
    metrics: DisplayMetrics = Resources.getSystem().displayMetrics
): Float {
    return toFloat() / metrics.density
}

fun Disposable.disposedBy(compositeDisposable: CompositeDisposable){
    compositeDisposable.add(this)
}

fun Context.showCustomToast(context: Context, content: String?) {
    if (TextUtils.isEmpty(content)) return
    val toast = Toast.makeText(context, content, Toast.LENGTH_SHORT)
    val toastView = toast.view
    val toastMessage = toastView.findViewById<TextView>(R.id.message)
    toastMessage.maxLines = 2
    toastMessage.ellipsize = TextUtils.TruncateAt.END
    toastMessage.textSize = 14f
    toastMessage.setTypeface(Typeface.createFromAsset(context.assets, "font/lato.ttf"))
    toastMessage.setTextColor(R.color.white_two.getColor())
    toastMessage.gravity = Gravity.CENTER
    toastView.setBackgroundResource(R.drawable.bg_toast)
    toast.setGravity(Gravity.BOTTOM or Gravity.CENTER, 0,  58.dpToPx().toInt())
    toast.show()
}

fun String.toast(context: Context = App.shared(), length: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(context, this, length).show()
}

inline fun <reified T : View> Activity.find(id: Int): T = findViewById<T>(id)

fun <T> Single<T>.applyOn(): Single<T> {
    return this.subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
}

fun <Any> Observable<Any>.execute(tag: String = ""): Disposable{
    return this.subscribeOn(Schedulers.io())
        .observeOn(Schedulers.io())
        .subscribe({
            if (tag.isNotBlank()) LogUtils.d("$tag Task success")
        }, {
            if (tag.isNotBlank()) LogUtils.d("$tag Task error")
        })
}


fun <T> Single<T>.execute(tag: String = ""): Disposable{
    return this.subscribeOn(Schedulers.io())
        .observeOn(Schedulers.io())
        .subscribe({
            if (tag.isNotBlank()) LogUtils.d("$tag Task success")
        }, {
            if (tag.isNotBlank()) LogUtils.d("$tag Task error")
        })
}



fun String?.ifEmptyLetBe(alt: String): String {
    if (this == null){
        return alt
    }
    if (this.trim().isEmpty()) {
        return alt
    }
    return this
}

fun Any.toJsonString(): String = Gson().toJson(this)

inline fun <T1 : Any, T2 : Any, R : Any> ifLet(p1: T1?, p2: T2?, block: (T1, T2) -> R?): R? {
    return if (p1 != null && p2 != null) block(p1, p2) else null
}

inline fun <T1 : Any, T2 : Any, T3 : Any, R : Any> ifLet(
    p1: T1?,
    p2: T2?,
    p3: T3?,
    block: (T1, T2, T3) -> R?
): R? {
    return if (p1 != null && p2 != null && p3 != null) block(p1, p2, p3) else null
}


fun Double.format(): String{
    val valueAsBD: BigDecimal = BigDecimal.valueOf(this)
    valueAsBD.setScale(2, BigDecimal.ROUND_HALF_UP)
    val custom = DecimalFormatSymbols()
    custom.decimalSeparator = '.'
    custom.groupingSeparator = ','
    custom.currencySymbol = ""
    val format: DecimalFormat = DecimalFormat.getInstance() as DecimalFormat
    format.decimalFormatSymbols = custom
    return try {format.format(valueAsBD)} catch (e: Exception) {return "$this"}
}

fun Int.format(): String{
    val custom = DecimalFormatSymbols()
    custom.decimalSeparator = '.'
    custom.groupingSeparator = ','
    custom.currencySymbol = ""
    val format: DecimalFormat = DecimalFormat.getInstance() as DecimalFormat
    format.decimalFormatSymbols = custom
    return try {format.format(this)} catch (e: Exception) {return "$this"}
}

fun Float.format(): String{
    val custom = DecimalFormatSymbols()
    custom.decimalSeparator = '.'
    custom.groupingSeparator = ','
    custom.currencySymbol = ""
    val format: DecimalFormat = DecimalFormat.getInstance() as DecimalFormat
    format.decimalFormatSymbols = custom
    return try {format.format(this)} catch (e: Exception) {return "$this"}
}

fun String.toDoubleValue(): Double{
    val custom = DecimalFormatSymbols()
    custom.decimalSeparator = '.'
    custom.groupingSeparator = ','
    custom.currencySymbol = ""
    val format: DecimalFormat = DecimalFormat.getInstance() as DecimalFormat
    format.decimalFormatSymbols = custom
    return try {format.parse(this).toDouble()} catch (e: Exception) {return 0.0}
}

fun String.toRequestBody(): RequestBody {
    return this.toRequestBody("text/plain".toMediaTypeOrNull())
}


fun Context.setClipboard(v: String): Boolean {
    val myClipboard= getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    val myClip: ClipData = ClipData.newPlainText(Constants.CLIPBOARD, v)
    myClipboard.setPrimaryClip(myClip)
    "Copied".toast(this)
    return true
}

fun Context.call(v: String){
    val callIntent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$v"))
    startActivity(callIntent)
}

fun Context.mail(v: String) {
    val intentMail = Intent(Intent.ACTION_SEND)
    intentMail.type = "message/rfc822"
    intentMail.putExtra(Intent.EXTRA_EMAIL, v) // the To mail.
    intentMail.putExtra(Intent.EXTRA_SUBJECT, "Subject goes here")
    intentMail.putExtra(Intent.EXTRA_TEXT, "Content goes here")
    try {
        startActivity(Intent.createChooser(intentMail, "Message to User to do what next"))
    } catch (ex: ActivityNotFoundException) {
        "There are no email clients installed.".toast(this)
    }
}

private var lastClickedTime = 0L
fun View.setSafeClickListener(onViewClick: () -> Unit) {
    this.setOnClickListener {
        if (SystemClock.elapsedRealtime() - lastClickedTime > 1000){
            lastClickedTime = SystemClock.elapsedRealtime()
            onViewClick.invoke()
        }
    }
}

@SuppressLint("CheckResult")
fun View.setSafeOnClickListener(onClick: (View) -> Unit) {
    this.clicks().throttleFirst(1000, TimeUnit.MILLISECONDS).subscribe {
        onClick(this)
    }
}

fun View.setOnClickNetworkListener(onClick: () -> Unit) {
    this.setOnClickListener {
//        if(NetWorkConnection.isNetworkAvailable()) {
            onClick()
//        } else {
//            "Internet is not available. Please try later!".toast()
//        }
    }
}