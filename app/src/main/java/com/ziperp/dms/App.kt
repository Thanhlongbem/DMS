package com.ziperp.dms

import android.app.Activity
import android.app.ActivityManager
import android.app.ActivityManager.AppTask
import android.app.Application
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import androidx.lifecycle.ProcessLifecycleOwner
import androidx.room.Room
import com.ziperp.dms.core.rest.Constants
import com.ziperp.dms.core.tracking.AutoSyncManager
import com.ziperp.dms.dao.AppDatabase
import com.ziperp.dms.extensions.getColor
import com.ziperp.dms.extensions.getString
import com.ziperp.dms.main.customer.customerdetail.view.CustomerDetailActivity
import com.ziperp.dms.main.customer.customerdetail.view.CustomerModifyActivity
import com.ziperp.dms.main.timekeeping.view.TimekeepingActivity
import com.ziperp.dms.main.visitcustomer.view.CheckinCheckoutActivity
import com.ziperp.dms.utils.AppConstant
import com.ziperp.dms.utils.DMSPreference
import com.ziperp.dms.utils.LogUtils


class App : Application(), LifecycleObserver, Application.ActivityLifecycleCallbacks {

    companion object {
        private lateinit var instance: App
        fun shared(): App {
            return instance
        }
    }

    var wasInBackground = false
    var currentActivity: Activity? = null

    override fun onCreate() {
        super.onCreate()
        instance = this
        //First init
        Constants.API_BASE_PATH =  DMSPreference.getString(
            AppConstant.BASE_URL,
            Constants.DEFAULT_SERVER
        )
        ProcessLifecycleOwner.get().lifecycle.addObserver(this)
        registerActivityLifecycleCallbacks(this)
    }


    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun onMoveToForeground() {
        // app moved to foreground
        wasInBackground = true
        LogUtils.d("App in foreground")

    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun onMoveToBackground() {
        // app moved to background
        wasInBackground = false
        LogUtils.d("App in background")
    }

    val appDatabase: AppDatabase by lazy{
         Room.databaseBuilder(
             applicationContext,
             AppDatabase::class.java, "dms-database"
         )
            .fallbackToDestructiveMigration()
            .build()
    }

    override fun onActivityCreated(activity: Activity, bundle: Bundle?) {
        AutoSyncManager.activityList.add(activity.javaClass.simpleName)
    }

    override fun onActivityStarted(activity: Activity) {
    }

    override fun onActivityResumed(activity: Activity) {

        LogUtils.d("TimekeepingActivity activity ${TimekeepingActivity.javaClass.simpleName}")
        LogUtils.d("TimekeepingActivity activity ${CustomerDetailActivity::class.java.simpleName}")
        LogUtils.d("TimekeepingActivity activity ${CustomerModifyActivity::class.java.simpleName}")
        LogUtils.d("TimekeepingActivity activity ${CheckinCheckoutActivity::class.java.simpleName}")
    }

    override fun onActivityPaused(activity: Activity) {
    }

    override fun onActivityStopped(activity: Activity) {
    }

    override fun onActivitySaveInstanceState(activity: Activity, bundle: Bundle) {
    }

    override fun onActivityDestroyed(activity: Activity) {
        LogUtils.d("onActivityDestroyed ${activity.localClassName}")
        AutoSyncManager.activityList.remove(activity.javaClass.simpleName)
    }

    fun getActivityList(){
        val am = applicationContext.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val componentName = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            am.appTasks[0].taskInfo.topActivity
        } else {
            am.getRunningTasks(1)[0].topActivity
        }
        LogUtils.d("componentName ${componentName?.className} ${componentName?.shortClassName}")
    }

}

enum class NotificationType { INFO, SUCCESS, ERROR }

//Global function
fun showNotificationBanner(type: NotificationType, message: String) {
    val layout: View = LayoutInflater.from(App.shared()).inflate(R.layout.banner, null)
    val title = layout.findViewById(R.id.banner_name) as TextView
    val content = layout.findViewById(R.id.banner_text) as TextView
    val background = layout.findViewById(R.id.customToast) as RelativeLayout
    val image = layout.findViewById(R.id.banner_profile) as ImageView
    content.text = message
    when (type) {
        NotificationType.INFO -> {
            title.text = R.string.str_information.getString()
            background.setBackgroundColor(R.color.colorPrimary.getColor())
            image.setImageResource(R.drawable.icon_info)
        }
        NotificationType.SUCCESS -> {
            title.text = R.string.str_success.getString()
            background.setBackgroundColor(R.color.color_tag_confirm.getColor())
            image.setImageResource(R.drawable.icon_confirm)
        }
        NotificationType.ERROR -> {
            title.text = R.string.str_error.getString()
            background.setBackgroundColor(R.color.color_unconfirm.getColor())
            image.setImageResource(R.drawable.icon_error)
        }
    }

    val toast = Toast(App.shared())
    toast.setGravity(Gravity.FILL_HORIZONTAL or Gravity.TOP, 0, 0)
    toast.duration = Toast.LENGTH_SHORT
    toast.view = layout
    toast.show()
}