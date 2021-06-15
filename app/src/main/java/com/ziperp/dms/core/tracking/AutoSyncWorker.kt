package com.ziperp.dms.core.tracking

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.work.RxWorker
import androidx.work.WorkerParameters
import com.ziperp.dms.Injection
import com.ziperp.dms.R
import com.ziperp.dms.extensions.toSlashDateTimeString
import com.ziperp.dms.main.home.HomeActivity
import com.ziperp.dms.utils.LogUtils
import io.reactivex.Single
import java.util.*

class AutoSyncWorker(
    context: Context,
    workerParameters: WorkerParameters
) : RxWorker(context, workerParameters) {

    private val notificationManager by lazy { applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager }
    private var notificationCompat: NotificationCompat.Builder? = null

    override fun createWork(): Single<Result> {
        LogUtils.d("AutoSyncManager.isCustomerScreen() ${AutoSyncManager.isCustomerScreen()}")
        LogUtils.d("AutoSyncManager.isTimeKeepingScreen() ${AutoSyncManager.isTimeKeepingScreen()}")
        showNotification()
        AutoSyncManager.isSynchronizing = true
        val visitRepo = Injection.provideVisitCustomerRepository()
        val customerRepo = Injection.provideCustomerDetailRepository()
        val timeKeeping = Injection.provideTimeKeepingRepository()
        val location = Injection.provideLocationRepository()
        return Single.just(true)
            .flatMap {
                return@flatMap if(!AutoSyncManager.isCustomerScreen()){
                    customerRepo.syncAllData()
                        .flatMap {visitRepo.syncAllData()}
                }else{
                    Single.just(it)
                }
            }
            .flatMap {
                return@flatMap if(!AutoSyncManager.isTimeKeepingScreen()){
                    timeKeeping.syncAllData()
                }else{
                    Single.just(it)
                }
            }
            .flatMap {
                location.syncAllData()
            }.map {
                LogUtils.d("Task complete")
                stopNotification()
                Result.success()
            }.onErrorReturn {
                LogUtils.d("Task error")
                stopNotification()
                Result.failure()
            }.doOnSuccess { AutoSyncManager.isSynchronizing = false }
            .doOnError { AutoSyncManager.isSynchronizing = false }
    }

    fun showNotification() {
        if(AutoSyncManager.isCustomerScreen() || AutoSyncManager.isTimeKeepingScreen()) return
        val intent = Intent(applicationContext, HomeActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or
                    Intent.FLAG_ACTIVITY_CLEAR_TASK
        }

        val pendingIntent: PendingIntent = PendingIntent.getActivity(
            applicationContext, 0, intent, 0
        )

        val notificationManager =
            applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        //Create notification channel
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val syncChannel = NotificationChannel(
                DmsService.SYNC_ID,
                DmsService.SYNC_CHANNEL,
                NotificationManager.IMPORTANCE_HIGH
            )
            syncChannel.description = DmsService.SYNC_CHANNEL
            notificationManager.createNotificationChannel(syncChannel)
        }

        //Creating a notification and setting its various attributes
        notificationCompat = NotificationCompat.Builder(applicationContext, DmsService.SYNC_ID)
            .setSmallIcon(R.drawable.ic_synch)
            .setColor(ContextCompat.getColor(applicationContext, R.color.colorPrimaryDark))
            .setContentTitle("Auto Sync")
            .setContentText("Synchronizing")
            .setPriority(NotificationCompat.PRIORITY_MIN)
            .setOngoing(true)
            .setOnlyAlertOnce(true)
            .setProgress(100, 0, true)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

        //Initial Alert
        notificationManager.notify(1, notificationCompat!!.build())
    }

    fun stopNotification() {
        if(AutoSyncManager.isCustomerScreen() || AutoSyncManager.isTimeKeepingScreen()) return
        notificationCompat?.let {
            notificationCompat!!
                .setContentText("Synchronize complete at ${Date().toSlashDateTimeString()}")
                .setProgress(0, 0, false)
                .setOngoing(false)
            notificationManager.notify(1, notificationCompat!!.build())
        }

    }

}