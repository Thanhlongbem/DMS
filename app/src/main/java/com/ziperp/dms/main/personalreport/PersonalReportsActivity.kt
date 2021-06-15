package com.ziperp.dms.main.personalreport

import android.content.Intent
import com.ziperp.dms.R
import com.ziperp.dms.base.BaseActivity
import com.ziperp.dms.extensions.getString
import com.ziperp.dms.extensions.setOnClickNetworkListener
import com.ziperp.dms.main.personalreport.datasummary.view.DataSummaryActivity
import com.ziperp.dms.main.personalreport.salesresult.view.YourSalesResultActivity
import com.ziperp.dms.main.personalreport.timekeeping.view.TKDiaryActivity
import com.ziperp.dms.main.personalreport.visitdetail.view.VisitDetailActivity
import com.ziperp.dms.main.personalreport.visitresult.VisitResultActivity
import kotlinx.android.synthetic.main.activity_personal_report.*

class PersonalReportsActivity : BaseActivity() {
    override fun setLayoutId(): Int = R.layout.activity_personal_report

    override fun initView() {
        setToolbar(R.string.str_tracking_report_title.getString(), true)
        visit_result_summation.setOnClickNetworkListener { moveTo(VisitResultActivity::class.java) }
        visit_customer_detail.setOnClickNetworkListener { moveTo(VisitDetailActivity::class.java) }
        timekeeping_diary.setOnClickNetworkListener { moveTo(TKDiaryActivity::class.java) }
        your_sale_result.setOnClickNetworkListener { moveTo(YourSalesResultActivity::class.java) }
        monthly_summary.setOnClickNetworkListener { moveTo(DataSummaryActivity::class.java) }
    }

    private fun moveTo(destination: Class<*>) {
        startActivity(Intent(this, destination))
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
    }


}