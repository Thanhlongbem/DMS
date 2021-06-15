package com.ziperp.dms.main.trackingreports

import android.content.Intent
import com.ziperp.dms.App
import com.ziperp.dms.R
import com.ziperp.dms.base.BaseActivity
import com.ziperp.dms.extensions.getString
import com.ziperp.dms.extensions.setOnClickNetworkListener
import com.ziperp.dms.main.trackingreports.customernotorder.view.NewCstNotOrderActivity
import com.ziperp.dms.main.trackingreports.dailyperformance.view.DailyPerformanceActivity
import com.ziperp.dms.main.trackingreports.newpoint.view.NewSalesPointActivity
import com.ziperp.dms.main.trackingreports.picture.view.TrackingPictureActivity
import com.ziperp.dms.main.trackingreports.reportsummation.view.ReportSummationActivity
import com.ziperp.dms.main.trackingreports.salesonroute.view.SalesOnRouteActivity
import com.ziperp.dms.main.trackingreports.salesresult.view.SalesResultActivity
import com.ziperp.dms.main.trackingreports.staff.view.TrackingStaffActivity
import com.ziperp.dms.main.trackingreports.visitcustomer.view.TrackingVisitCustomerActivity
import com.ziperp.dms.utils.LogUtils
import kotlinx.android.synthetic.main.activity_tracking_report.*
import okhttp3.Cache

class TrackingReportsActivity : BaseActivity() {
    override fun setLayoutId(): Int = R.layout.activity_tracking_report

    override fun initView() {
        setToolbar(R.string.str_tracking_report_title.getString(), true)

        trackingStaffLocation.setOnClickNetworkListener { moveTo(TrackingStaffActivity::class.java) }
        trackingVisitCustomer.setOnClickNetworkListener { moveTo(TrackingVisitCustomerActivity::class.java) }
        trackingPicture.setOnClickNetworkListener { moveTo(TrackingPictureActivity::class.java) }
        newCustomerNotOrder.setOnClickNetworkListener { moveTo(NewCstNotOrderActivity::class.java) }
        staffSalesResult.setOnClickNetworkListener { moveTo(SalesResultActivity::class.java) }
        newPointOfSales.setOnClickNetworkListener { moveTo(NewSalesPointActivity::class.java) }
        dailyPerformance.setOnClickNetworkListener { moveTo(DailyPerformanceActivity::class.java) }
        summationReport.setOnClickNetworkListener { moveTo(ReportSummationActivity::class.java) }
        salesEffective.setOnClickNetworkListener { moveTo(SalesOnRouteActivity::class.java) }

        dumpCaches()
    }

    private fun moveTo(destination: Class<*>) {
        startActivity(Intent(this, destination))
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
    }

    private fun dumpCaches() {
        val cacheSize = (5 * 1024 * 1024).toLong()
        val myCache = Cache(App.shared().cacheDir, cacheSize)

        myCache.urls().forEach {
            LogUtils.d("Data ${it}")
        }
    }

}