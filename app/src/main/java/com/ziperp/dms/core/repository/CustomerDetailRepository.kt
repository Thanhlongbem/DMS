package com.ziperp.dms.core.repository

import com.google.gson.Gson
import com.ziperp.dms.App
import com.ziperp.dms.Injection
import com.ziperp.dms.camera.CustomerImage
import com.ziperp.dms.core.rest.*
import com.ziperp.dms.core.rest.Constants.CUD.TYPE_CREATE
import com.ziperp.dms.core.rest.Constants.CUD.TYPE_UPDATE
import com.ziperp.dms.core.service.CustomerDetailService
import com.ziperp.dms.dao.CustomerDetailDao
import com.ziperp.dms.extensions.execute
import com.ziperp.dms.extensions.ifEmptyLetBe
import com.ziperp.dms.main.customer.customerdetail.model.CustomerDetail
import com.ziperp.dms.main.customer.customerdetail.model.CustomerDetailResponse
import com.ziperp.dms.main.customer.customerroute.model.CustomerRoute
import com.ziperp.dms.main.customer.list.model.CUDCustomerRequest
import com.ziperp.dms.main.visitcustomer.model.VisitCustomer
import com.ziperp.dms.utils.AppUtil
import com.ziperp.dms.utils.LogUtils
import com.ziperp.dms.utils.NetWorkConnection
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.functions.BiFunction
import io.reactivex.functions.Function3

class CustomerDetailRepository(private val customerDetailService: CustomerDetailService,  val customerDao: CustomerDetailDao) {

    val imageDao = App.shared().appDatabase.customerImageDao()
    val routeDao = App.shared().appDatabase.customerRouteDao()
    val routeRepository = Injection.provideCustomerRouteRepository()
    val imageRepository = Injection.provideCustomerImageRepository()
    val visitCustomerDao = App.shared().appDatabase.visitCustomerDao()
//    val visitCustomerRepository = Injection.provideVisitCustomerRepository()

    fun getCustomerDetail(request: BodyRequest): Single<CustomerDetailResponse>{
        return customerDetailService.getCustomerDetail(request)
    }

    fun syncCustomer(customer: CustomerDetail): Single<CudData>{
        return getRelatedData(customer)
            .flatMap {
                saveCustomerApi(customer)
                    .flatMap {
                        if(it.isSuccess() && customer.isValidCustomerCode() && (customer.listRoutes.isNotEmpty())){
                            customer.listRoutes.forEach { route -> route.cstCd = customer.cstCd}
                            return@flatMap routeRepository.addRoutes(customer.listRoutes)
                        }
                        return@flatMap Single.just(it)
                    }.flatMap {
                        if(it.isSuccess() && customer.isValidCustomerCode() && (customer.listImages.isNotEmpty())){
                            customer.listImages.forEach { image -> image.keyNo = customer.cstCd}
                            return@flatMap imageRepository.uploadFiles(customer.listImages)
                        }else Single.just(it)
                    }
                    .flatMap {
                        if(it.isSuccess() && customer.isValidCustomerCode() && (customer.listVisitCustomers.isNotEmpty())){
                            customer.listVisitCustomers.forEach { visitCustomer ->
                                visitCustomer.cstCd = customer.cstCd
                            }
                            visitCustomerDao.insert(customer.listVisitCustomers).execute() // Update visit customer
                        }
                        return@flatMap Single.just(it)
                    }
            }
    }

    fun syncAllData(): Single<CudData>{
        val routeRepository = Injection.provideCustomerRouteRepository()
        val imageRepository = Injection.provideCustomerImageRepository()
        return syncCustomerData()
            .flatMap {
                routeRepository.syncAllCustomerRoutes()
            }.flatMap {
                imageRepository.syncAllCustomerImages()
            }
    }

    fun saveCustomer(customer: CustomerDetail): Single<CudData>{
        return saveCustomerToDB(customer)
            .doOnSuccess { customer.id = Integer(it.toInt()) }
            .flatMap { syncCustomer(customer) } //always success
            .onErrorResumeNext { Single.just(CudData(CUD_ERROR, "Could not save customer in cache"))}
    }

    fun delete(customer: CustomerDetail): Single<CudData>{
        return if(customer.isValidCustomerCode()){
            deleteCustomerInApi(customer)
        } else{
            deleteCustomerComplete(customer)
                .map { CudData(CUD_SUCCESS, "Delete success")}
        }
    }

    private fun saveCustomerApi(customer: CustomerDetail): Single<CudData>{
        if(!customer.isValidCustomerCode()){
            return saveCustomerApi(TYPE_CREATE, customer)
        }else{//Has Customer Code, Update
            if(customer.isNeedSynchronized()){
                return saveCustomerApi(TYPE_UPDATE, customer)
            }
        }
        return Single.just(CudData(CUD_ERROR, "Invalid Customer Info"))
    }

    private fun saveCustomerApi(type: String, customer: CustomerDetail): Single<CudData>{
        val request = CUDCustomerRequest()
        var customerCode = ""
        if(customer.isValidCustomerCode()){// Purpose for update
            customerCode = customer.cstCd
        }

        request.apply {
            cstCd = customerCode
            cstNm = customer.cstNm
            cstNo = customer.cstNo
            cstDiv = customer.cstDiv
            officePhone = customer.officePhone
            officeFax = customer.officeFax
            workTel = customer.workTel
            email = customer.email
            representative = customer.representative
            cstBizLicNo = customer.cstBizLicNo
            taxCode = customer.taxCode
            gender = customer.gender
            birthdayDate = customer.birthdayDate
            cstGrp1 = customer.cstGrp1
            cstGrp2 = customer.cstGrp2
            cstGrp3 = customer.cstGrp3
            cstGrp4 = customer.cstGrp4
            regMan = customer.regMan
            street = customer.street
            districtCd = customer.districtCd
            regionCd = customer.regionCd
            countryCd = customer.countryCd
            addrLat = customer.addrLat
            addrLng = customer.addrLng
            addrOnMap = customer.addrOnMap.ifEmptyLetBe(AppUtil.getAddrLatLng(customer.addrLat, customer.addrLng))

            remark = customer.remark
        }

        val pJsonData = Gson().toJson(request)
        val bodyRequest = BodyRequestFactory.get(RequestType.CUD_CUSTOMER, pJsonData, pPageMode = type)

        return customerDetailService.cudCustomer(bodyRequest)
            .map {
                if(it.isSuccess()){
                    deleteCustomerToDB(customer).execute()
                    customer.cstCd = it.getCstCd()
                    customer.isSynchonized = true
                    return@map CudData(CUD_SUCCESS,it.message())
                }else {
                    return@map CudData(CUD_CONFLICT,it.message())
                }
            }.onErrorResumeNext {
                Single.just(CudData(CUD_OFFLINE, "Save your update in offline"))
            }
    }

    private fun saveCustomerToDB(customer: CustomerDetail): Single<Long> {
        return customerDao.insert(customer)
    }


    private fun syncCustomerData(): Single<CudData> {
        //Sync customer with image, route
        return customerDao.getAllCustomerDetail()
            .flatMapObservable { list ->
                LogUtils.d("syncAllData + size " + list.size)
                Observable.fromIterable(list)
            }
            .flatMapSingle { customer -> syncCustomer(customer) }
            .toList()
            .map { CudData(CUD_SUCCESS, "Sync success ${it.size} customers") }
    }




    private fun getRelatedData(customer: CustomerDetail): Single<CustomerDetail>{
        return Single.zip(
            routeDao.getAllRoutes(customer.cstCd).onErrorResumeNext(Single.just(arrayListOf())),
            imageDao.getCustomerImages(customer.cstCd, false).onErrorResumeNext(Single.just(arrayListOf())),
            visitCustomerDao.getVisitCustomers(customer.cstCd).onErrorResumeNext(Single.just(arrayListOf())),
            Function3{ routes: List<CustomerRoute>, images: List<CustomerImage>, visitCustomers: List<VisitCustomer> ->
                return@Function3 Triple(routes, images, visitCustomers)
            }).map {
            customer.listRoutes = it.first
            customer.listImages = it.second
            customer.listVisitCustomers = it.third
            return@map customer
        }
    }

    private fun deleteCustomerInApi(customer: CustomerDetail): Single<CudData> {
        val request = CUDCustomerRequest()
        request.cstCd = customer.cstCd
        val pJsonData = Gson().toJson(request)
        val bodyRequest = BodyRequestFactory.get(RequestType.CUD_CUSTOMER, pJsonData, pPageMode = "D")
        return customerDetailService.cudCustomer(bodyRequest)
            .map {
                if(it.isSuccess()){
                    deleteCustomerComplete(customer).execute()
                    return@map CudData(CUD_SUCCESS,it.message())
                }else CudData(CUD_CONFLICT,it.message())
            }.onErrorResumeNext { handleError(it) }
    }

    private fun deleteCustomerToDB(customer: CustomerDetail): Single<Int> {
        return Single.fromCallable{customerDao.delete(customer)}
            .map { DAO_SUCCESS }
    }

    private fun deleteCustomerComplete(customer: CustomerDetail): Single<Int>{
        return getRelatedData(customer)
//            .flatMap{getRelatedData(customer)}
            .doOnSuccess {
                customer.listImages.forEach {
                    imageDao.delete(it)
                }
                customer.listRoutes.forEach {
                    routeDao.delete(it)
                }
                deleteCustomerToDB(customer).execute()
            } .map { DAO_SUCCESS }

    }

    fun handleError(throwable: Throwable): Single<CudData>{
        return if(NetWorkConnection.isNetworkAvailable()){
            Single.just(CudData(CUD_ERROR, "Could not delete customer without internet"))
        }else{
            Single.just(CudData(CUD_ERROR, "Unexpected result!"))
        }
    }


}
