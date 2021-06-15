package com.ziperp.dms.main.visitcustomer.view

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.SystemClock
import android.view.View
import androidx.lifecycle.Observer
import com.google.android.gms.maps.model.LatLng
import com.google.android.material.tabs.TabLayoutMediator
import com.ziperp.dms.*
import com.ziperp.dms.base.BaseActivity
import com.ziperp.dms.base.BaseCUDActivity.Companion.CREATE_MODE
import com.ziperp.dms.base.BaseCUDActivity.Companion.EXTRA_CUD_MODE
import com.ziperp.dms.base.BottomBarItem
import com.ziperp.dms.camera.CameraActivity
import com.ziperp.dms.camera.ImagesFragment
import com.ziperp.dms.core.rest.*
import com.ziperp.dms.core.token.DmsUserManager
import com.ziperp.dms.core.tracking.ActivityRecognitionManager
import com.ziperp.dms.core.tracking.LocationTrackingManager
import com.ziperp.dms.core.tracking.ServiceUtils
import com.ziperp.dms.extensions.*
import com.ziperp.dms.main.itemmaster.adapter.SQPagerAdapter
import com.ziperp.dms.main.saleorder.modify.SalesOrderModifyActivity
import com.ziperp.dms.main.saleorder.modify.SalesOrderModifyActivity.Companion.EXTRA_VISIT_CST_NO
import com.ziperp.dms.main.visitcustomer.model.VisitCustomer
import com.ziperp.dms.main.visitcustomer.model.VisitCustomerInfoResponse
import com.ziperp.dms.main.visitcustomer.model.VisitCustomerItem
import com.ziperp.dms.main.visitcustomer.view.StockCountModifyActivity.Companion.EXTRA_CST_VISIT_NO
import com.ziperp.dms.utils.*
import io.reactivex.Single
import kotlinx.android.synthetic.main.activity_checkin_checkout.*
import java.util.*


class CheckinCheckoutActivity : BaseActivity() {
    private lateinit var customer : VisitCustomerItem
    private var cstVisitNo: String = ""
    private var typeCheckIn: String = "V"
    private var isRightLocation = false
    lateinit var visitInfoFragment: VisitInfoFragment
    lateinit var locationFragment: LocationFragment
    lateinit var stockCountFragment: StockCountFragment
    lateinit var imagesFragment: ImagesFragment

    private var visitCustomer = VisitCustomer()
    private var viewMode = MODE_CHECK_IN // 0: Edit, 1: View only

    companion object{
        val TAG = CheckinCheckoutActivity::class.java.canonicalName
        const val RESPONSE_DATA = "RESPONSE_DATA"
        const val VISIT_CUSTOMER_NO = "VISIT_CUSTOMER_NO"
        const val VIEW_MODE = "VIEW_MODE"
        const val IS_RIGHT_LOCATION = "IS_RIGHT_LOCATION"
        const val MODE_CHECK_IN = 0
        const val MODE_VIEW_ONLY = 1

        fun newInstance(
            data: VisitCustomerItem,
            context: Context?,
            isRightLocation: Boolean
        ): Intent {
            val intent = Intent(context, CheckinCheckoutActivity::class.java)
            intent.putExtra(RESPONSE_DATA, data)
            intent.putExtra(IS_RIGHT_LOCATION, isRightLocation)
            return intent
        }

        fun newInstance(
            context: Context?,
            visitCstNo: String
        ): Intent {
            val intent = Intent(context, CheckinCheckoutActivity::class.java)
            intent.putExtra(VISIT_CUSTOMER_NO, visitCstNo)
            intent.putExtra(VIEW_MODE, MODE_VIEW_ONLY)// 1: view only
            return intent
        }
    }

    private val viewModel by lazy {
        getViewModel { Injection.provideVisitCustomerViewModel() }
    }

    override fun setLayoutId(): Int = R.layout.activity_checkin_checkout

    @SuppressLint("ResourceAsColor")
    override fun initView() {
        viewMode = intent.getIntExtra(VIEW_MODE, MODE_CHECK_IN)

        when(viewMode){
            MODE_CHECK_IN -> {// Edit mode
                setUpBottomBar()
                btn_switch.setOnToggledListener{ switch, isOn ->
                    LogUtils.d("isOn $isOn")
                    if(isOn){
                        btn_switch.colorOn = R.color.color_visit_item_3.getColor()
                        btn_switch.colorOff = R.color.color_white.getColor()
                    }else{
                        btn_switch.colorOn = R.color.color_white.getColor()
                        btn_switch.colorOff = R.color.color_visit_closed.getColor()
                    }
                }

                tv_TimeVisit.base = SystemClock.elapsedRealtime()
                tv_TimeVisit.start()
                customer = intent.getSerializableExtra(RESPONSE_DATA) as VisitCustomerItem

                tv_title.text = if(customer.txtCstNm.isEmpty()) "N/A" else customer.txtCstNm.normalize()
                tv_location.text = if(customer.txtAddress.isEmpty()) "N/A" else customer.txtAddress
                tv_phone_checkinout.text = if(customer.txtPhone.isEmpty()) "N/A" else customer.txtPhone
                tv_owner.text = if(customer.repPerson.isEmpty()) "N/A" else customer.repPerson
                tvCurrency.text = DmsUserManager.userInfo.lcBaseCurr
                checkIn()
            }
            MODE_VIEW_ONLY ->{// View only
                setUpBottomBar()
                disable_view.setOnClickListener { /*Do nothing. Just block click switch button*/ }

                cstVisitNo = intent.getStringExtra(VISIT_CUSTOMER_NO)
                if(cstVisitNo.isNotBlank()){
                    viewModel.getVisitCustomerInfo(cstVisitNo)
                }
            }
        }


        setOnRegisterPhoneContextMenu(tv_phone_checkinout)
        dataObserver()

    }


    fun checkIn(){
        typeCheckIn = "V"
        isRightLocation = intent.getBooleanExtra(IS_RIGHT_LOCATION, false)

        val currentLocation = LocationTrackingManager.lastLocation
        var currentAddress = ""
        currentLocation?.let {
            currentAddress = AppUtil.getAddrLatLng(this, it.latitude, it.longitude) ?: ""
        }

        visitCustomer.apply {
            cstVisitNo = "Cached_" +System.currentTimeMillis()
            cstCd = customer.cstCd
            staffId = DmsUserManager.userInfo.staffId
            timeLogPosIn = Date().toDateTimeFullString()
            visitStsIn =  if (isRightLocation) "0" else "1"
            visitResultIn =  "0"
            posNameIn = currentAddress
            latPosIn = if (currentLocation != null) "${currentLocation.latitude}" else ""
            lngPosIn = if (currentLocation != null) "${currentLocation.longitude}" else ""
            moveStsIn = ActivityRecognitionManager.lastActivityValue.toString()
            batteryPerIn = ServiceUtils.getBatteryLevel(this@CheckinCheckoutActivity).toInt()
            deviceName = (Build.MANUFACTURER  + " " + Build.MODEL)
        }
        viewModel.saveVisitCustomer(visitCustomer)
    }

    private fun checkOut() {
        typeCheckIn = "O"

        //Check in location
        val currentLocation = LocationTrackingManager.lastLocation
        var currentAddress = ""
        currentLocation?.let {
            currentAddress = AppUtil.getAddrLatLng(this, it.latitude, it.longitude) ?: ""
        }

        //Map location
        val mapLocation = locationFragment.latLng
        var mapAddress = ""
        mapLocation?.let {
            mapAddress = AppUtil.getAddrLatLng(this, mapLocation.latitude, mapLocation.longitude) ?: ""
        }
        val customerAndMapDiff = differenceCustomerAndMap()

        visitCustomer.apply {
            timeLogPosOut = Date().toDateTimeFullString()
            visitStsOut =  if (isRightLocation) "0" else "1"
            visitResultOut =  if(btn_switch.isOn) "0" else "1"
            posNameOut = currentAddress
            latPosOut = if (currentLocation != null) "${currentLocation.latitude}" else ""
            lngPosOut = if (currentLocation != null) "${currentLocation.longitude}" else ""
            moveStsOut = ActivityRecognitionManager.lastActivityValue.toString()
            batteryPerOut = ServiceUtils.getBatteryLevel(this@CheckinCheckoutActivity).toInt()
            visitPosNm = mapAddress
            visitLatPos = if (mapLocation != null) "${mapLocation.latitude}" else ""
            visitLngPos = if (mapLocation != null) "${mapLocation.longitude}" else ""
            isDiffCstAddr = if(customerAndMapDiff.first) 0 else 1
            checkInOutDistance = inOutDiff().toInt()
            checkInDistance = differenceCustomerAndIn().toInt()
            checkOutDistance = differenceCustomerAndOut().toInt()
            visitLocDistance = customerAndMapDiff.second.toInt()
            remark = visitInfoFragment.getRemark()
        }
        viewModel.saveVisitCustomer(visitCustomer)

        tv_TimeVisit.stop()
    }

    private fun dataObserver() {
        viewModel.saveVisitCustomerLiveData2.observe(this, Observer { response ->
            when (response.status) {
                Status.LOADING -> showLoading(true)
                Status.SUCCESS -> {
                    showLoading(false)

                    val cudData = response.data!!
                    val visitCustomer = cudData.data as VisitCustomer
                    when (cudData.status) {
                        CUD_SUCCESS -> {
//                            showNotificationBanner(NotificationType.SUCCESS, cudData.message)
                        }
                        CUD_CONFLICT -> {
                            showNotificationBanner(NotificationType.ERROR, cudData.message)
                        }
                        CUD_OFFLINE -> {
                            showNotificationBanner(NotificationType.INFO, cudData.message)
                        }
                        CUD_ERROR -> {
                            showNotificationBanner(NotificationType.ERROR, cudData.message)
                        }
                    }
                    if (typeCheckIn == "V") {
                        cstVisitNo = visitCustomer.cstVisitNo
                        initPager()
                    }else if(typeCheckIn == "O"){
                        val intent = Intent()
                        if(visitCustomer.isValidCustomerCode()){
                            intent.putExtra(Constants.EXTRA_CUSTOMER_CODE, visitCustomer.cstCd)
                        }
                        if(visitCustomer.isValidVisitCustomerNo()){
                            setResult(Activity.RESULT_OK, intent)
                        }
                        finish()
                    }
                }
                Status.ERROR -> {
                    showLoading(false)
                    showNotificationBanner(
                        NotificationType.ERROR,
                        "Could not create customer right now!"
                    )
                }
            }

        })

        viewModel.visitCustomerInfoLiveData.observe(this, Observer {
            when (it.status) {
                Status.SUCCESS -> {
                    it.data?.let { response ->
                        if (response.visitCustomerInfo.isNotEmpty()) {
                            response.visitCustomerInfo[0].apply {
                                tv_title.text = cstNm
                                tv_location.text = "N/A"
                                tv_phone_checkinout.text = cstPhone
                                tv_owner.text = representative
                                tvCurrency.text = DmsUserManager.userInfo.lcBaseCurr
                                tvDebitValue.text = debit.format()
                                tvLimitValue.text = limit.format()

                                btn_switch.isOn = (visitResult == 0)
                            }
                            initPager(response.visitCustomerInfo[0])
                        }else{
                            initPager()
                        }
                    }
                }
                Status.LOADING -> { }
                Status.ERROR -> { }
            }
        })
    }

    private fun setUpBottomBar(){
        if(viewMode == MODE_VIEW_ONLY) {
            bottomBar.visibility = View.GONE
            return
        }
        bottomBar.items = arrayListOf(
            BottomBarItem(
                R.string.str_sales_order.getString(),
                R.drawable.icon_sales_order,
                R.color.color_visit_item_1
            ),
            BottomBarItem(
                R.string.str_presentation.getString(),
                R.drawable.icon_presentation,
                R.color.color_visit_item_2
            ),
            BottomBarItem(
                R.string.str_take_picture.getString(),
                R.drawable.ic_picture,
                R.color.color_visit_item_3
            ),
            BottomBarItem(
                R.string.str_count_item.getString(),
                R.drawable.icon_add_item,
                R.color.color_visit_item_4
            ),
            BottomBarItem(
                R.string.str_check_out.getString(),
                R.drawable.icon_check_out,
                R.color.color_visit_item_5
            )
        )

        bottomBar.onBottomItemClickListener = { position ->
            LogUtils.d("OnClicked $position")
            when (position) {
                0 -> createSaleOrder()
                1 -> startActivity(Intent(this, PresenterActivity::class.java))
                2 -> {
                    validateBeforeOffline(visitCustomer){
                        takePicture()
                    }
                }
                3 -> stockCount()
                4 -> {
                    validateBeforeOffline(visitCustomer){
                        showCheckOut()
                    }
                }
            }
        }

    }

    fun createSaleOrder(){
        validate(visitCustomer) {
            val intent = SalesOrderModifyActivity.newInstance(
                DataConvertUtils.convertTitle(customer.txtCstNm),
                customer.cstCd,
                customer.txtPhone,
                CREATE_MODE,
                this
            )
            intent.putExtra(EXTRA_VISIT_CST_NO, cstVisitNo)
            startActivityForResult(intent, 100)
        }
    }

    fun takePicture(){
        if (cstVisitNo.isNotEmpty()) {
            startActivityForResult(
                CameraActivity.newInstance(
                    cstVisitNo,
                    true,
                    applicationContext
                ), 102
            )
        }
    }

    fun stockCount(){
        validate(visitCustomer) {
            val intent = Intent(this, StockCountModifyActivity::class.java)
            intent.putExtra(EXTRA_CUD_MODE, CREATE_MODE)
            intent.putExtra(EXTRA_CST_VISIT_NO, cstVisitNo)
            startActivityForResult(intent, 103)
        }
    }

    fun validateBeforeOffline(visitCustomer: VisitCustomer, completion: (()-> Unit)? = null){
        if (NetWorkConnection.isNetworkAvailable()) {
            if(visitCustomer.isValidCustomerCode() ) {
                if(visitCustomer.isValidVisitCustomerNo()){
                    completion?.invoke()
                }else{
                    //sync visit customer
                    AppUtil.showSyncConfirm(this){
                        syncVisitCustomer(visitCustomer)
                    }
                }
            }else{
                //sync customer
                AppUtil.showSyncConfirm(this){
                    syncCustomer(visitCustomer.cstCd, visitCustomer)
                }
            }
        } else {
            //Offline mode
            completion?.invoke()
            "You're using offline mode!".toast()
        }

    }

    fun validate(visitCustomer: VisitCustomer, completion: (()-> Unit)? = null){
        if (NetWorkConnection.isNetworkAvailable()) {
            if(visitCustomer.isValidCustomerCode() ) {
                if(visitCustomer.isValidVisitCustomerNo()){
                    completion?.invoke()
                }else{
                    //sync visit customer
                    AppUtil.showSyncConfirm(this){
                        syncVisitCustomer(visitCustomer)
                    }
                }
            }else{
                //sync customer
                AppUtil.showSyncConfirm(this){
                    syncCustomer(visitCustomer.cstCd, visitCustomer)
                }
            }
        } else {
            "No internet connection!".toast()
        }

    }

    private fun syncVisitCustomer(visitCustomer: VisitCustomer) {
        showLoading(true)
        viewModel.visitCustomerRepository.syncVisitCustomer(visitCustomer)
            .applyOn()
            .doOnSuccess {
                if(visitCustomer.isValidVisitCustomerNo() && visitCustomer.isValidCustomerCode()){
                    this.visitCustomer = visitCustomer
                    this.cstVisitNo = visitCustomer.cstVisitNo
                    updateCustomerNo(this.cstVisitNo)
                }
            }
            .subscribe({
                showLoading(false)
                "Synchronizing completed".toast()
            }, {
                showLoading(false)
            }).disposedBy(compositeDisposable)
    }

    fun updateCustomerNo(cstVisitNo: String){
        visitInfoFragment.cstVisitNo = cstVisitNo
        stockCountFragment.cstNo = cstVisitNo
        imagesFragment.customerNo = cstVisitNo
    }

    private fun syncCustomer(cstCd: String, visitCustomer: VisitCustomer) {
        showLoading(true)
        val customerDetailRepository = Injection.provideCustomerDetailRepository()
        customerDetailRepository.customerDao
            .getCustomerDetail(cstCd)
            .flatMap {customer ->
                customerDetailRepository.syncCustomer(customer)
                    .flatMap {
                        if (customer.isValidCustomerCode()) {
                            visitCustomer.cstCd = customer.cstCd
                            this@CheckinCheckoutActivity.customer.cstCd = customer.cstCd
                            return@flatMap viewModel.visitCustomerRepository
                                .syncVisitCustomer(visitCustomer)
                                .map {it ->
                                    if(visitCustomer.isValidVisitCustomerNo() && visitCustomer.isValidCustomerCode()){
                                        this.visitCustomer = visitCustomer
                                        this.cstVisitNo = visitCustomer.cstVisitNo
                                        updateCustomerNo(this.cstVisitNo)
                                    }
                                    return@map it
                                }
                        } else return@flatMap Single.just( CudData(CUD_ERROR,"Could not sync customer"))
                    }
            }.applyOn()
            .subscribe({
                showLoading(false)
                "Synchronizing completed".toast()
            }, {
                showLoading(false)
            }).disposedBy(compositeDisposable)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            when(requestCode) {
                100 -> viewpager.setCurrentItem(0, true)
                102 -> viewpager.setCurrentItem(2, true)
                103 -> viewpager.setCurrentItem(1, true)
            }
        }
    }

    private fun showCheckOut() {
        if (ServiceUtils.isGPSEnable(this)) {
            val location = LocationTrackingManager.lastLocation
            var distanceValue = -1f
            if (location != null) {
                if (customer.posLat.isNotEmpty() && customer.posLng.isNotEmpty()) {
                    distanceValue = AppUtil.distanceBetweenTwoPointInMeter(
                        location.latitude,
                        location.longitude,
                        customer.posLat.toDouble(),
                        customer.posLng.toDouble()
                    )}
                if (distanceValue != -1f && distanceValue < AppConfiguration.MAX_CHECK_IN_DISTANCE_ACCEPT) {
                    isRightLocation = true // check right location
                    AppUtil.showAlertDialog(this, "Do you want to checkout?", { checkOut() })
                } else {
                    val distanceWrongValueDialog = DistanceWrongValueDialog.newInstance(
                        DistanceWrongValueDialog.TYPE_CHECK_OUT,
                        customer,
                        distanceValue.toDouble(),
                        location.latitude,
                        location.longitude
                    ) {
                        isRightLocation = false // check right location
                        checkOut()
                    }
                    distanceWrongValueDialog.show(supportFragmentManager, null)
                }
            } else {"Could not calculate distance between current location and customer".toast(this) }
        } else {
            ServiceUtils.showSettingsAlert(this)
        }
    }

    private fun differenceCustomerAndMap(): Pair<Boolean, Float> {
        val location = locationFragment.latLng
        var distanceValue = -1f
        if (location != null) {
            if (customer.posLat.isNotEmpty() && customer.posLng.isNotEmpty()) {
                distanceValue = AppUtil.distanceBetweenTwoPointInMeter(
                    location.latitude,
                    location.longitude,
                    customer.posLat.toDouble(),
                    customer.posLng.toDouble()
                )
            }
            if (distanceValue != -1f && distanceValue < AppConfiguration.MAX_CHECK_IN_DISTANCE_ACCEPT) {
                return Pair(true, distanceValue) // check right location
            }
        }else{
            return Pair(false, 0f)
        }
        return Pair(false, -1f)
    }


    private fun inOutDiff(): Float {
        var distanceValue = -1f
        if (visitCustomer.latPosIn.isNotBlank() && visitCustomer.lngPosIn.isNotBlank()
            && visitCustomer.latPosOut.isNotBlank() && visitCustomer.lngPosOut.isNotBlank() ) {
            distanceValue = AppUtil.distanceBetweenTwoPointInMeter(
                visitCustomer.latPosIn.toDouble(),
                visitCustomer.lngPosIn.toDouble(),
                visitCustomer.latPosOut.toDouble(),
                visitCustomer.lngPosOut.toDouble()
            )

        }
        return distanceValue
    }


    private fun differenceCustomerAndIn(): Float {
        var distanceValue = -1f

        if (visitCustomer.latPosIn.isNotBlank() && visitCustomer.lngPosIn.isNotBlank()
            && customer.posLat.isNotBlank() && customer.posLng.isNotBlank() ) {
            distanceValue = AppUtil.distanceBetweenTwoPointInMeter(
                visitCustomer.latPosIn.toDouble(),
                visitCustomer.lngPosIn.toDouble(),
                customer.posLat.toDouble(),
                customer.posLng.toDouble()
            )

        }
        return distanceValue
    }


    private fun differenceCustomerAndOut(): Float {
        var distanceValue = -1f

        if (visitCustomer.latPosOut.isNotBlank() && visitCustomer.lngPosOut.isNotBlank()
            && customer.posLat.isNotBlank() && customer.posLng.isNotBlank() ) {
            distanceValue = AppUtil.distanceBetweenTwoPointInMeter(
                visitCustomer.latPosOut.toDouble(),
                visitCustomer.lngPosOut.toDouble(),
                customer.posLat.toDouble(),
                customer.posLng.toDouble()
            )

        }
        return distanceValue
    }


    private fun initPager(visitCstInfo: VisitCustomerInfoResponse.VisitCustomerInfo? = null) {
        visitInfoFragment = VisitInfoFragment(cstVisitNo, visitCstInfo?.remark ?: "", viewMode)
        stockCountFragment =  StockCountFragment.newInstance(cstVisitNo)
        imagesFragment = ImagesFragment.newInstance(cstVisitNo, true)
        locationFragment = LocationFragment.newInstance()
        visitCstInfo?.let {
            locationFragment.locationName = it.visitPosNm
            if(it.visitLatPos.isNotBlank() && it.visitLngPos.isNotBlank()){
                locationFragment.latLng = LatLng(it.visitLatPos.toDouble(), it.visitLngPos.toDouble())
            }else{
                locationFragment.isDisableMap = false
            }
            locationFragment.isGetLocation = false
        }
        val pagerAdapter = SQPagerAdapter(
            this, listFragments = listOf(
                visitInfoFragment,
                stockCountFragment,
                imagesFragment,
                locationFragment
            )
        )
        val listTitles = listOf(
            R.string.str_visit_info.getString(),
            R.string.str_stock_count.getString(),
            R.string.str_images.getString(),
            R.string.str_location.getString()
        )
        viewpager.adapter = pagerAdapter
        viewpager.offscreenPageLimit = 4
        viewpager.isUserInputEnabled = false
        TabLayoutMediator(tab, viewpager) { tab, position ->
            tab.text = listTitles[position]
            viewpager.setCurrentItem(tab.position, true)
        }.attach()
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (hasFocus) {
            if(viewMode == MODE_CHECK_IN){
                window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        or View.SYSTEM_UI_FLAG_FULLSCREEN
                        or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)
            }
        }
    }

    override fun onBackPressed() {
        //Block backPress
        if(viewMode == MODE_VIEW_ONLY){
            super.onBackPressed()
        }
    }

}