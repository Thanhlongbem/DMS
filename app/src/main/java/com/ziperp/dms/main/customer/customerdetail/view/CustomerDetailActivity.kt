package com.ziperp.dms.main.customer.customerdetail.view

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.view.Menu
import android.view.MenuItem
import androidx.lifecycle.Observer
import com.ziperp.dms.*
import com.ziperp.dms.base.BaseActivity
import com.ziperp.dms.base.BaseCUDActivity.Companion.CREATE_MODE
import com.ziperp.dms.base.BaseCUDActivity.Companion.EXTRA_CUD_MODE
import com.ziperp.dms.base.BaseCUDActivity.Companion.EXTRA_CUD_OBJECT
import com.ziperp.dms.base.BaseCUDActivity.Companion.UPDATE_MODE
import com.ziperp.dms.base.BottomBarItem
import com.ziperp.dms.base.PagerAdapter
import com.ziperp.dms.camera.CameraActivity
import com.ziperp.dms.core.rest.*
import com.ziperp.dms.core.rest.Constants.EXTRA_CUSTOMER_NAME
import com.ziperp.dms.core.tracking.LocationTrackingManager
import com.ziperp.dms.core.tracking.ServiceUtils
import com.ziperp.dms.extensions.*
import com.ziperp.dms.main.customer.customerdetail.model.CustomerDetail
import com.ziperp.dms.main.customer.customernote.view.CustomerNoteActivity
import com.ziperp.dms.main.customer.customernote.view.NoteModifyActivity
import com.ziperp.dms.main.saleorder.modify.SalesOrderModifyActivity
import com.ziperp.dms.main.trackingreports.reportsummation.view.ReportSummationActivity
import com.ziperp.dms.main.visitcustomer.model.VisitCustomerItem
import com.ziperp.dms.main.visitcustomer.view.CheckinCheckoutActivity
import com.ziperp.dms.utils.AppUtil
import com.ziperp.dms.utils.DistanceWrongValueDialog
import com.ziperp.dms.utils.LogUtils
import com.ziperp.dms.utils.NetWorkConnection
import kotlinx.android.synthetic.main.activity_customer_detail.*

class CustomerDetailActivity : BaseActivity(), BottomSheet.BottomSheetListener {

    var data: CustomerDetail? = null
    var detailFragment: DetailFragment? = null
    var relatedFragment: RelatedFragment? = null
    var customerCode: String = ""

    private lateinit var pagerAdapter: PagerAdapter
    val bottomSheet = BottomSheet()

    private val viewModel by lazy {
        getViewModel { Injection.provideCustomerDetailViewModel() }
    }

    override fun setLayoutId(): Int = R.layout.activity_customer_detail

    override fun initView() {
        setToolbar(R.string.str_customer_detail.getString(), true)
        setUpBottomBar()

        customerCode = intent.getStringExtra(Constants.EXTRA_CUSTOMER_CODE) ?: ""
        if (customerCode.isNotBlank()) {
            viewModel.getCustomerDetail(customerCode)
        } else {
            data = intent.getSerializableExtra(Constants.EXTRA_CUSTOMER_DATA) as? CustomerDetail
        }

        initPager()
        dataObserver()

    }

    @SuppressLint("SetTextI18n")
    private fun updateData(data: CustomerDetail) {

        this.data = data
        updateHeader(data)

        detailFragment?.updateData(data)
        relatedFragment?.updateData(data)

        pagerAdapter.notifyDataSetChanged()
    }

    private fun updateHeader(data: CustomerDetail){
        LogUtils.d("Data ${data.toJsonString()}")
        data.apply {
            tv_title.text = if(cstNo.isNotBlank()) "$cstNo - $cstNm" else cstNm

            val result =  mutableListOf(street, districtNm, regionNm, countryNm).filter { it.isNotBlank() }
            val location = if (result.isNotEmpty()) result.reduce { s1, s2 -> "$s1, $s2" } else ""

            tv_location.text = if (addrOnMap.isEmpty()) {
                if (location.isEmpty()) R.string.str_not_available.getString() else location
            } else addrOnMap

            tv_phoneOfCustomerDetail.text =
                officePhone.ifEmptyLetBe(R.string.str_not_available.getString())
            tv_owner.text = regManNm.ifEmptyLetBe(R.string.str_not_available.getString())

        }
    }

    private fun dataObserver() {

        viewModel.customerDetailData.observe(this, Observer {
            when (it.status) {
                Status.SUCCESS -> {
                    showLoading(false)
                    it.data?.let { response ->
                        if(response.data.isNotEmpty()){
                            updateData(response.data[0])
                        }
                    }
                }
                Status.LOADING -> {
                    showLoading(true)
                }
                Status.ERROR -> {
                    showLoading(false)
                }
            }
        })

        viewModel.deleteRequestStatus2.observe(this, Observer { response ->
            when (response.status) {
                Status.LOADING -> showLoading(true)
                Status.SUCCESS -> {
                    showLoading(false)
                    val cudData = response.data!!
                    when (cudData.status) {
                        CUD_SUCCESS -> {
                            showNotificationBanner(NotificationType.SUCCESS, cudData.message)
                            setResult(RESULT_RELOAD)// Purpose back
                            finish()
                        }
                        CUD_CONFLICT -> {
                            showNotificationBanner(NotificationType.ERROR, cudData.message)
                        }
                        CUD_ERROR -> {
                            showNotificationBanner(NotificationType.ERROR, cudData.message)
                        }
                    }
                }
            }
        })
    }

    private fun initPager() {

        detailFragment = DetailFragment()
        relatedFragment = RelatedFragment()

        data?.let {
            detailFragment!!.data = it
            relatedFragment!!.data = it
        }
        pagerAdapter = PagerAdapter(
            supportFragmentManager,
            listFragments = listOf(detailFragment!!, relatedFragment!!)
        )
        pagerAdapter.listTitles =
            listOf(R.string.str_detail.getString(), R.string.str_related.getString())
        viewpager.adapter = pagerAdapter
        viewpager.offscreenPageLimit = 2
        tab.setupWithViewPager(viewpager)

        data?.let {
            updateHeader(it)
        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_setting, menu)
        menu?.findItem(R.id.action_edit)?.isVisible = true
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_edit -> {
                if (data != null) {
                    val intent = Intent(this, CustomerModifyActivity::class.java)
                    intent.putExtra(EXTRA_CUD_MODE, UPDATE_MODE)
                    intent.putExtra(EXTRA_CUD_OBJECT, data)
                    startActivityForResult(intent, 100)
                }
            }
        }
        return true
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent?) {
        super.onActivityResult(requestCode, resultCode, intent)
        LogUtils.d("Data $requestCode $resultCode")
        if(requestCode == 100){
            when (resultCode){
                Activity.RESULT_OK -> {
                    intent?.getStringExtra(Constants.EXTRA_CUSTOMER_CODE)?.let {
                        customerCode = it
                        viewModel.getCustomerDetail(customerCode)
                    }

                }
                Activity.RESULT_CANCELED -> {
                    (intent?.getSerializableExtra(Constants.EXTRA_CUSTOMER_DATA) as? CustomerDetail)?.let {
                        updateData(it)
                    }
                }
                RESULT_FINISH ->{
                    setResult(RESULT_RELOAD)
                    finish()
                }
            }
        }
    }

    private fun setUpBottomBar() {
        bottom_bar.items = arrayListOf(
            BottomBarItem(
                R.string.str_sales_order.getString(),
                R.drawable.icon_sales_order,
                R.color.color_visit_item_1
            ),
            BottomBarItem(
                R.string.str_check_in.getString(),
                R.drawable.ic_checkin_small,
                R.color.color_visit_item_5
            ),
            BottomBarItem(
                R.string.str_take_note.getString(),
                R.drawable.ic_add_note,
                R.color.color_visit_item_3
            ),
            BottomBarItem(
                R.string.str_take_picture.getString(),
                R.drawable.ic_picture,
                R.color.color_tag_opening
            ),
            BottomBarItem(
                R.string.str_more.getString(),
                R.drawable.ic_more,
                R.color.color_visit_item_4
            )
        )

        bottom_bar.onBottomItemClickListener = { position ->
            data?.let {
                when (position) {
                    0 -> createSaleOrder(it)
                    1 -> visitCustomer(it)
                    2 -> addNote(it)
                    3 -> validateBeforeOffline(it) {
                        takePicture()
                    }
                    4 -> bottomSheet.show(supportFragmentManager, "")
                }
            }
        }
    }

    override fun onOptionClick(position: Int) {
        data?.let {
            when (position) {
                0 -> createSaleOrder(it)
                1 -> visitCustomer(it)
                2 -> addNote(it)
                3 -> validateBeforeOffline(it) {
                    takePicture()
                }
                4 -> {
                    if (NetWorkConnection.isNetworkAvailable()) {
                        val intent = Intent(this, ReportSummationActivity::class.java)
                        intent.putExtra(ReportSummationActivity.EXTRA_CUSTOMER, it.cstCd)
                        startActivity(intent)
                    } else {
                        "Internet is not available. Please try later!".toast()
                    }
                }
                5 -> { //Delete
                    AppUtil.showAlertDialog(this, R.string.str_question_delete.getString(), {
                        viewModel.deleteCustomer(it)
                    })
                }
            }
        }

        bottomSheet.dismiss()
    }

    fun createSaleOrder(customerDetail: CustomerDetail){
        validate(customerDetail) {
            startActivity(
                SalesOrderModifyActivity.newInstance(
                    customerDetail.cstNm,
                    customerDetail.cstCd,
                    customerDetail.officePhone,
                    CREATE_MODE,
                    this
                )
            )
        }
    }

    fun visitCustomer(customerDetail: CustomerDetail){
        customerDetail.let {
            val visitCustomer = VisitCustomerItem(
                cstCd = it.cstCd,
                txtCstNm = it.cstNm,
                posLat = it.addrLat,
                posLng = it.addrLng,
                txtAddress = it.addrOnMap,
                repPerson = it.regManNm,
                txtPhone = it.officePhone,
                remark = it.remark
            )
            checkIn(visitCustomer)
        }
    }

    fun validateBeforeOffline(customerDetail: CustomerDetail, completion: (()-> Unit)? = null){
        if (NetWorkConnection.isNetworkAvailable()) {
            if(customerDetail.isValidCustomerCode() ) {
                completion?.invoke()
            }else{
                AppUtil.showSyncConfirm(this){
                    syncCustomer(customerDetail)
                }
            }
        } else {
            completion?.invoke()
            "You're using offline mode!".toast()
        }
    }

    fun addNote(customerDetail: CustomerDetail){
        validate(customerDetail) {
            val intent = Intent(this, NoteModifyActivity::class.java)
            intent.putExtra(EXTRA_CUD_MODE, CREATE_MODE)
            intent.putExtra(CustomerNoteActivity.EXTRA_OBJECT_ID, customerDetail.cstCd)
            intent.putExtra(EXTRA_CUSTOMER_NAME, customerDetail.cstNm)
            startActivity(intent)
        }
    }

    fun takePicture(){
        data?.let {
            startActivity(CameraActivity.newInstance(it.cstCd, false, this))
        }
    }

    fun checkIn(visitCustomer: VisitCustomerItem){
        visitCustomer.let {
            if (ServiceUtils.isGPSEnable(this)) {
                val location = LocationTrackingManager.lastLocation
                var distanceValue = -1f
                if (location != null) {
                    if (it.posLat.isNotEmpty() && it.posLng.isNotEmpty()) {
                        distanceValue = AppUtil.distanceBetweenTwoPointInMeter(
                            location.latitude,
                            location.longitude,
                            it.posLat.toDouble(),
                            it.posLng.toDouble()
                        )
                    }
                    if (distanceValue != -1f && distanceValue < AppConfiguration.MAX_CHECK_IN_DISTANCE_ACCEPT) {
                        startActivityForResult(
                            Intent(
                                CheckinCheckoutActivity.newInstance(
                                    it,
                                    this,
                                    true
                                )
                            ), 100
                        )
                    } else {
                        val distanceWrongValueDialog =
                            DistanceWrongValueDialog.newInstance(
                                DistanceWrongValueDialog.TYPE_CHECK_IN,
                                it,
                                distanceValue.toDouble(),
                                location.latitude,
                                location.longitude
                            ) {
                                startActivityForResult(
                                    CheckinCheckoutActivity.newInstance(
                                        it,
                                        this,
                                        false
                                    ), 100
                                )
                            }
                        distanceWrongValueDialog.show(supportFragmentManager, null)
                    }
                } else {
                    "Could not calculate distance between current location and customer".toast(
                        this
                    )
                }

            } else {
                ServiceUtils.showSettingsAlert(this)
            }
        }
    }

    private fun validate(customerDetail: CustomerDetail, completion: (()-> Unit)? = null){
        if (NetWorkConnection.isNetworkAvailable()) {
            if(customerDetail.isValidCustomerCode() ) {
                completion?.invoke()
            }else{
                AppUtil.showSyncConfirm(this){
                    syncCustomer(customerDetail)
                }
            }
        } else {
            "No internet connection!".toast()
        }
    }

    private fun syncCustomer(customerDetail: CustomerDetail) {
        showLoading(true)
        viewModel.customerDetailRepository
            .syncCustomer(customerDetail)
            .applyOn()
            .subscribe({
                showLoading(false)
                if(customerDetail.isValidCustomerCode()){
                    customerCode = customerDetail.cstCd
                    viewModel.getCustomerDetail(customerCode)
                    "Synchronizing successfull".toast()
                }
            },{
                showLoading(false)
            }).disposedBy(compositeDisposable)
    }


}