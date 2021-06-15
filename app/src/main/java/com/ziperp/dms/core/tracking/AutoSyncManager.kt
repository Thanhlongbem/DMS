package com.ziperp.dms.core.tracking

import androidx.work.*
import com.ziperp.dms.App
import com.ziperp.dms.extensions.execute
import com.ziperp.dms.extensions.toDateString
import com.ziperp.dms.main.customer.customerdetail.view.CustomerDetailActivity
import com.ziperp.dms.main.customer.customerdetail.view.CustomerModifyActivity
import com.ziperp.dms.main.timekeeping.view.TimekeepingActivity
import com.ziperp.dms.main.visitcustomer.view.CheckinCheckoutActivity
import com.ziperp.dms.utils.LogUtils
import io.reactivex.Single
import java.util.*

object AutoSyncManager {


    var isSynchronizing = false
    var activityList = arrayListOf<String>()

    val TIME_KEEPING_SCREEN = TimekeepingActivity::class.java.simpleName
    val CUSTOMER_DETAIL_SCREEN = CustomerDetailActivity::class.java.simpleName
    val CUSTOMER_MODIFY_SCREEN = CustomerModifyActivity::class.java.simpleName
    val CHECK_IN_OUT_SCREEN = CheckinCheckoutActivity::class.java.simpleName

    fun availableDataSynced(): Single<Boolean>{
        return Single.fromCallable {
            val appDatabase = App.shared().appDatabase
            val timeKeepingList = appDatabase.timeKeepingDao().getAllTimeKeepingSync(Date().toDateString())
                .filter { it.isNeedSync() }

            LogUtils.d("getAllTimeKeepingSyn " + timeKeepingList.size)

            val trackingRequestList = appDatabase.trackingRequestDao().getAllTrackingRequestSync()
            LogUtils.d("trackingRequestList " + trackingRequestList.size)

            val customerList = appDatabase.customerDetailDao().getAllCustomerDetailSync()
            LogUtils.d("customerList " + customerList.size)

            val customerImageList = appDatabase.customerImageDao().getCustomerImagesSync()
            LogUtils.d("customerImageList " + customerImageList.size)

            val customerRouteList = appDatabase.customerRouteDao().getAllRoutesSync()
            LogUtils.d("customerRouteList " + customerRouteList.size)

            val visitCustomerList = appDatabase.visitCustomerDao().getAllVisitCustomerSync()
            LogUtils.d("visitCustomerList " + visitCustomerList.size)

            return@fromCallable (timeKeepingList.isNotEmpty() || trackingRequestList.isNotEmpty()
                    || customerList.isNotEmpty() || customerImageList.isNotEmpty()
                    || customerRouteList.isNotEmpty() || visitCustomerList.isNotEmpty())
        }
    }

    fun checkSyncedData(){
        availableDataSynced().doOnSuccess {
            if(it) autoSync()
        }.execute()
    }

     fun autoSync() {
        val list = WorkManager.getInstance(App.shared()).getWorkInfosForUniqueWork(
            LocationTrackingManager.TAG_SYNC_WORK
        ).get()
//        if(list.isNotEmpty() && list[0].state != WorkInfo.State.ENQUEUED){
//            return // Ignore
//        }

        if(list.isNotEmpty()){
            LogUtils.d("Work queue ${list.size} state ${list.get(0)?.state}")
        }else{
            LogUtils.d("Work queue ${list.size}")
        }
        val workManager = WorkManager.getInstance(App.shared())
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()
        val request = OneTimeWorkRequest.Builder(AutoSyncWorker::class.java)
            .setConstraints(constraints)
            .build()
        workManager.enqueueUniqueWork(LocationTrackingManager.TAG_SYNC_WORK, ExistingWorkPolicy.KEEP, request)
    }

    fun isTimeKeepingScreen(): Boolean{
        return activityList.isNotEmpty() && activityList.contains(TIME_KEEPING_SCREEN)
    }

    fun isCustomerScreen(): Boolean{
        if (activityList.isNotEmpty()){
            return activityList.contains(CUSTOMER_DETAIL_SCREEN)
                    || activityList.contains(CUSTOMER_MODIFY_SCREEN)
                    || activityList.contains(CHECK_IN_OUT_SCREEN)
        }
        return false
    }


}