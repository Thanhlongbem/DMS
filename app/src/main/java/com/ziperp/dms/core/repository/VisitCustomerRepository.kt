package com.ziperp.dms.core.repository

import com.google.gson.Gson
import com.ziperp.dms.App
import com.ziperp.dms.Injection
import com.ziperp.dms.core.rest.*
import com.ziperp.dms.core.rest.Constants.CUD.TYPE_CREATE
import com.ziperp.dms.core.rest.Constants.CUD.TYPE_UPDATE
import com.ziperp.dms.core.service.VisitCustomerService
import com.ziperp.dms.dao.VisitCustomerDao
import com.ziperp.dms.extensions.execute
import com.ziperp.dms.extensions.ifEmptyLetBe
import com.ziperp.dms.main.visitcustomer.model.*
import com.ziperp.dms.utils.AppUtil
import com.ziperp.dms.utils.LogUtils
import io.reactivex.Observable
import io.reactivex.Single

class VisitCustomerRepository(
    private val visitCustomerService: VisitCustomerService,
    private val visitCustomerDao: VisitCustomerDao
) {

    val imageDao = App.shared().appDatabase.customerImageDao()
    val imageRepository = Injection.provideCustomerImageRepository()

    fun getVisitCustomer(request: BodyRequest): Single<VisitCustomerResponse> {
        return visitCustomerService.getVisitCustomer(request)
    }

    fun getVisitCustomerInfo(request: BodyRequest): Single<VisitCustomerInfoResponse> {
        return visitCustomerService.getVisitCustomerInfo(request)
    }

    fun getVisitCustomerOrder(request: BodyRequest): Single<VisitCustomerOrderResponse> {
        return visitCustomerService.getVisitCustomerOrder(request)
    }

    fun saveVisitCustomer(visitCustomer: VisitCustomer) : Single<CudData>{
        return saveVisitCustomerToDB(visitCustomer)
            .doOnSuccess { visitCustomer.id = Integer(it.toInt()) }
            .flatMap {
                saveVisitCustomerInApi(visitCustomer)
            }
            .onErrorResumeNext {  Single.just(CudData(CUD_ERROR, "Could not save customer in cache"))}
    }

    fun syncVisitCustomer(visitCustomer: VisitCustomer) : Single<CudData>{
        return imageDao.getCustomerImages(visitCustomer.cstVisitNo, true)
            .onErrorResumeNext { Single.just(arrayListOf()) }
            .map {
                visitCustomer.listImages = it
                return@map visitCustomer
            }.flatMap { customer ->
                LogUtils.d("Sync customer first " + customer.cstVisitNo)
                saveVisitCustomerInApi(customer)
                    .flatMap {
                        LogUtils.d("Sync customer image " + customer.listImages.size)
                        if (customer.isValidVisitCustomerNo() && (customer.listImages.isNotEmpty())) {
                            customer.listImages.forEach { image ->
                                image.keyNo = customer.cstVisitNo
                            }
                            imageDao.insert(customer.listImages).execute()
                            return@flatMap imageRepository.uploadFiles(customer.listImages)
                        } else Single.just(it)
                    }
            }
    }

    private fun saveVisitCustomerInApi(visitCustomer: VisitCustomer): Single<CudData> {
        if(visitCustomer.isValidCustomerCode()){
            return if (!visitCustomer.isValidVisitCustomerNo()) {
                saveVisitCustomerInApi(TYPE_CREATE, visitCustomer)
                    .flatMap {
                        if (it.isSuccess() && visitCustomer.isNeedSyncCheckOut()) {
                            saveVisitCustomerInApi(TYPE_UPDATE, visitCustomer)
                        } else {
                            Single.just(it)
                        }
                    }
            } else {//Has customer visit no
                saveVisitCustomerInApi(TYPE_UPDATE, visitCustomer)
            }
        }else{
            return Single.just(CudData(CUD_OFFLINE, "Sync new customer data first"))
        }
    }

    private fun saveVisitCustomerInApi(type: String, visitCustomer: VisitCustomer): Single<CudData>{
        val request = SaveVisitCustomerInfoRequest()
        when(type){
            TYPE_CREATE ->{
                request.apply {
                    cstVisitNo = ""
                    cstCd = visitCustomer.cstCd
                    staffId = visitCustomer.staffId
                    timeLogPos = visitCustomer.timeLogPosIn
                    visitSts = visitCustomer.visitStsIn
                    visitResult = visitCustomer.visitResultIn
                    latPos = visitCustomer.latPosIn
                    lngPos = visitCustomer.lngPosIn
                    posName = visitCustomer.posNameIn.ifEmptyLetBe(
                        AppUtil.getAddrLatLng(
                            App.shared(),
                            latPos.toDouble(),
                            lngPos.toDouble()
                        ) ?: ""
                    )
                    typeCheckin = "V"
                    moveSts = visitCustomer.moveStsIn
                    batteryPer = visitCustomer.batteryPerIn
                    deviceName = visitCustomer.deviceName
                }
            }
            TYPE_UPDATE -> {
                request.apply {
                    cstVisitNo = visitCustomer.cstVisitNo
                    cstCd = visitCustomer.cstCd
                    staffId = visitCustomer.staffId
                    timeLogPos = visitCustomer.timeLogPosOut
                    visitSts = visitCustomer.visitStsOut
                    visitResult = visitCustomer.visitResultOut
                    latPos = visitCustomer.latPosOut
                    lngPos = visitCustomer.lngPosOut
                    posName = visitCustomer.posNameOut.ifEmptyLetBe(
                        AppUtil.getAddrLatLng(
                            App.shared(),
                            latPos.toDouble(),
                            lngPos.toDouble()
                        ) ?: ""
                    )
                    typeCheckin = "O"
                    moveSts = visitCustomer.moveStsOut
                    batteryPer = visitCustomer.batteryPerOut
                    deviceName = visitCustomer.deviceName
                    visitLatPos = visitCustomer.visitLatPos
                    visitLngPos = visitCustomer.visitLngPos
                    visitPosNm = visitCustomer.visitPosNm.ifEmptyLetBe(
                        if (visitLatPos.isNotBlank() && visitLngPos.isNotBlank()) {
                            AppUtil.getAddrLatLng(
                                App.shared(),
                                visitLatPos.toDouble(),
                                visitLngPos.toDouble()
                            ) ?: ""
                        } else ""
                    )
                    isDiffCstAddr = visitCustomer.isDiffCstAddr
                    checkInDistance = visitCustomer.checkInDistance
                    checkOutDistance = visitCustomer.checkOutDistance
                    checkInOutDistance = visitCustomer.checkInOutDistance
                    visitLocDistance = visitCustomer.visitLocDistance
                    remark =  visitCustomer.remark
                }
            }
        }

        val pJSONData = Gson().toJson(request)
        val bodyRequest = BodyRequestFactory.get(RequestType.SAVE_VISIT_CUSTOMER_INFO, pJSONData, type)

        return visitCustomerService.saveVisitCustomerInfo(bodyRequest)
            .map {
                LogUtils.d("Save status " + it.isSuccess())
                if (it.isSuccess()) {
                    when (type) {
                        TYPE_CREATE -> {
                            visitCustomer.isSynchonizedCheckIn = true
                            if(!visitCustomer.isNeedSyncCheckOut()){
                                visitCustomerDao.delete(visitCustomer)
                            }
                        }
                        TYPE_UPDATE -> {
                            visitCustomer.isSynchonizedCheckOut = true
                            visitCustomerDao.delete(visitCustomer)
                        }
                    }
                    visitCustomer.cstVisitNo = it.getCstVisitNo()
                    return@map CudData(CUD_SUCCESS, it.message())
                } else CudData(CUD_CONFLICT, it.message())
            }.onErrorResumeNext { Single.just(CudData(CUD_OFFLINE, "Save your update in offline")) }
    }

    private fun saveVisitCustomerToDB(visitCustomer: VisitCustomer): Single<Long> {
        return visitCustomerDao.insert(visitCustomer)
    }

    fun syncAllData(): Single<CudData>{
        val imageRepository = Injection.provideCustomerImageRepository()
        return syncVisitCustomerData()
            .flatMap {
                imageRepository.syncAllVisitCustomerImages()
            }
    }

    private fun syncVisitCustomerData(): Single<CudData> {
        //Sync customer with image
        return visitCustomerDao.getAllVisitCustomer()
            .flatMapObservable { list ->
                LogUtils.d("syncAllData + size " + list.size)
                Observable.fromIterable(list)
            }.flatMapSingle { customer -> syncVisitCustomer(customer) }
            .toList()
            .map { CudData(CUD_SUCCESS, "Sync success ${it.size} customers") }
    }

}