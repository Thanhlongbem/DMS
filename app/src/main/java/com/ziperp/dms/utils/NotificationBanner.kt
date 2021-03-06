package com.ziperp.dms.utils

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.os.Build
import android.util.AttributeSet
import android.view.GestureDetector
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.OvershootInterpolator
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.Keep
import com.google.android.material.card.MaterialCardView
import com.ziperp.dms.R
import com.ziperp.dms.extensions.notifyVibrate

class NotificationBanner : MaterialCardView {

    private lateinit var avatar: ImageView
    private lateinit var name: TextView
    private lateinit var text: TextView
    private val animatorSet = AnimatorSet()
    private val translate = ObjectAnimator.ofFloat(this, "TranslationY", -60f, 1f).setDuration(300)
    private val fade = ObjectAnimator.ofFloat(this, "alpha", 0f, 1f).setDuration(300)
    lateinit var info: BannerInfo
    var listener: BannerClickListener? = null
    var vibrate = true
    var hideAfterClick = true

    constructor(context: Context) : super(context) {
        bindViews(context)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        bindViews(context)
    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle) {
        bindViews(context)
    }

    /**
     * Convenience method to bind inflated [View] to [MaterialCardView]
     *
     * @param context to inflate [View]
     */
    private fun bindViews(context: Context) {
        val view = LayoutInflater.from(context).inflate(R.layout.banner, this)

        if (isInEditMode)
            return

        translate.interpolator = AccelerateDecelerateInterpolator()
        fade.interpolator = OvershootInterpolator()
        avatar = view.findViewById(R.id.banner_profile)
        name = view.findViewById(R.id.banner_name)
        text = view.findViewById(R.id.banner_text)
        setOnTouchListener(BannerGestureListener(context, this, object : BannerGestureListener.ClickListener {

            override fun onClick(view: View) {
                if (hideAfterClick) {
                    handler.removeCallbacks(hideAction)
                    visibility = View.INVISIBLE
                }

                listener?.onClick(view, info)
            }

            override fun onFlingUp(view: View) {
                handler.removeCallbacks(hideAction)
                hideAction.run()
            }
        }))
    }

    /**
     * Internal method to apply behavior to [NotificationBanner]
     */
    private fun show() {
        if (vibrate)
            context.notifyVibrate()

        handler.removeCallbacks(hideAction)
        visibility = View.VISIBLE
        animatorSet.playTogether(translate, fade)
        animatorSet.start()
        handler.postDelayed(hideAction, 7000)
    }

    /**
     * [Runnable] action that hides banner with animation if supported
     */
    private val hideAction = Runnable {
        visibility = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            animatorSet.reverse()
            View.INVISIBLE
        } else
            View.INVISIBLE
    }

    /**
     * Show [BannerInfo] that was previously supplied via info
     */
    fun notifyMessage() {
        if (!::info.isInitialized)
            throw IllegalStateException("BannerInfo has not been set")

        avatar.setImageBitmap(info.avatar)
        name.text = info.name
        text.text = info.text
        show()
    }

    /**
     * Immediately bind and show [BannerInfo]
     *
     * @param info to bind to banner
     */
    fun notifyMessage(info: BannerInfo) {
        this.info = info
        avatar.setImageBitmap(info.avatar)
        name.text = info.name
        text.text = info.text
        show()
    }

    companion object {

        /**
         * Internal [GestureDetector] to listen for common actions to banner
         *
         * @param context to create [GestureDetector]
         * @param view to pass to listeners
         * @param clickListener for simple callbacks
         */
        private class BannerGestureListener(context: Context, view: View, clickListener: ClickListener) : View.OnTouchListener {

            private val gestureDetector: GestureDetector

            init {
                gestureDetector = GestureDetector(context, object : GestureDetector.SimpleOnGestureListener() {

                    override fun onSingleTapUp(e: MotionEvent): Boolean {
                        clickListener.onClick(view)
                        return true
                    }

                    override fun onFling(e1: MotionEvent?, e2: MotionEvent?, velocityX: Float, velocityY: Float): Boolean {
                        return if (velocityY < -100) {
                            clickListener.onFlingUp(view)
                            true
                        } else
                            super.onFling(e1, e2, velocityX, velocityY)
                    }
                })
            }

            @SuppressLint("ClickableViewAccessibility")
            override fun onTouch(p0: View?, p1: MotionEvent?): Boolean {
                gestureDetector.onTouchEvent(p1)
                return true
            }

            interface ClickListener {

                fun onClick(view: View)
                fun onFlingUp(view: View)
            }
        }
    }
}

@Keep
open class BannerInfo(val avatar: Bitmap, val name: String, val text: String)

interface BannerClickListener {

    /**
     * Listener to react to banner click action
     *
     * @param view [NotificationBanner] that was clicked
     * @param info [BannerInfo] that was shown
     */
    fun onClick(view: View, info: BannerInfo)
}