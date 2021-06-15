package com.ziperp.dms.core.repository

import com.google.gson.Gson
import com.ziperp.dms.core.rest.*
import com.ziperp.dms.core.service.CustomerRouteService
import com.ziperp.dms.dao.CustomerRouteDao
import com.ziperp.dms.extensions.execute
import com.ziperp.dms.main.customer.customerroute.model.AddRouteRequest
import com.ziperp.dms.main.customer.customerroute.model.CustomerRoute
import com.ziperp.dms.main.customer.customerroute.model.CustomerRouteRequest
import com.ziperp.dms.utils.LogUtils
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.functions.BiFunction

class CustomerRouteRepository(
    private val customerRouteService: CustomerRouteService,
    private val routeDao: CustomerRouteDao
) {

    fun getCustomerRoutes(customerCode: String): Single<List<CustomerRoute>> {
        return Single.zip(
            getRoutesFromApi(customerCode),
            getRoutesFromDB(customerCode),
            BiFunction { apiList: List<CustomerRoute>, cachedList: List<CustomerRoute> ->
                val result = arrayListOf<CustomerRoute>()
                LogUtils.d("Result " + apiList.size  + " cachedList " + cachedList.size)
                if (apiList.isNotEmpty()) {
                    result.addAll(apiList)
                    cachedList.forEach {
                        if (!apiList.contains(it)) {
                            result.add(it)
                        }
                    }
                    LogUtils.d("Combine api and cached")
                } else if (cachedList.isNotEmpty()) {
                    result.addAll(cachedList)
                    LogUtils.d("Get from cached")
                }
                return@BiFunction result
            })

    }


    fun addRoutes(customerRoutes: List<CustomerRoute>): Single<CudData> {
        return Observable.fromIterable(customerRoutes)
            .flatMap {
                return@flatMap addRoute(it).toObservable()
            }.toList()
            .map {
                CudData(CUD_SUCCESS, "Save Route successfully")
            }
    }

    private fun addRoute(customerRoute: CustomerRoute): Single<CudData> {
        return saveRouteToDB(customerRoute)
            .doOnSuccess { customerRoute.id = Integer(it.toInt()) }
            .flatMap {
                return@flatMap if (customerRoute.isValidCustomerCode()) {
                    saveRouteInApi(customerRoute)
                } else {
                    Single.just(CudData(CUD_OFFLINE, "Save your update in offline"))
                }
            } //always success//always success

    }

    private fun getRoutesFromApi(customerCode: String): Single<List<CustomerRoute>> {
        val pJsonData = Gson().toJson(CustomerRouteRequest(customerCode))
        val bodyRequest = BodyRequestFactory.get(RequestType.CUSTOMER_ROUTE, pJsonData)
        return customerRouteService.getRouteList(bodyRequest)
            .map {
                it.data.forEach { item ->
                    item.isSynchonized = true
                    item.cstCd = customerCode
                }
                return@map it.data
            }
            .onErrorResumeNext(Single.just(arrayListOf()))
    }

    private fun getRoutesFromDB(customerCode: String): Single<List<CustomerRoute>> {
        return routeDao
            .getAllRoutes(customerCode)
            .onErrorResumeNext {
                Single.just(arrayListOf())
            }
    }

    fun syncAllCustomerRoutes(): Single<CudData> {
        return routeDao
            .getAllRoutes()
            .onErrorResumeNext {
                Single.just(arrayListOf())
            }.flatMap { it ->
                val list = it.filter { it.isValidCustomerCode() }
                return@flatMap if (list.isNotEmpty()) {
                    addRoutes(list)
                } else {
                    Single.just(CudData(CUD_SUCCESS, "Empty data"))
                }
            }
    }

    private fun saveRouteInApi(customerRoute: CustomerRoute): Single<CudData> {
        val pJsonData = Gson().toJson(AddRouteRequest(customerRoute.cstCd, customerRoute.routeId))
        val bodyRequest = BodyRequestFactory.get(RequestType.ADD_ROUTE, pJsonData, pPageMode = "A")
        return customerRouteService.addRoute2(bodyRequest)
            .map {
                deleteRouteInDB(customerRoute).execute()// delete
                if (it.isSuccess()) {
                    customerRoute.isSynchonized = true
                    return@map CudData(CUD_SUCCESS, it.message())
                } else {
                    CudData(CUD_CONFLICT, it.message())
                }
            }.onErrorResumeNext {
                Single.just(CudData(CUD_OFFLINE, "Save your update in offline"))
            }
    }

    private fun saveRouteToDB(customerRoute: CustomerRoute): Single<Long> {
        return routeDao.insert(customerRoute)
    }

    private fun deleteRouteInDB(customerRoute: CustomerRoute): Single<Int> {
        return Single.fromCallable { routeDao.delete(customerRoute) }
            .map { DAO_SUCCESS }
    }

    fun deleteRoute(customerRoute: CustomerRoute): Single<CudData> {
        val pJsonData = Gson().toJson(AddRouteRequest(customerRoute.cstCd, customerRoute.routeId))
        val bodyRequest = BodyRequestFactory.get(RequestType.ADD_ROUTE, pJsonData, pPageMode = "D")
        return customerRouteService.addRoute2(bodyRequest)
            .map {
                deleteRouteInDB(customerRoute).execute()// delete
                if (it.isSuccess()) {
                    customerRoute.isSynchonized = true
                    return@map CudData(CUD_SUCCESS, it.message())
                } else {
                    return@map CudData(CUD_CONFLICT, it.message())
                }
            }.onErrorResumeNext {
                if(customerRoute.isValidCustomerCode()){//cache
                    return@onErrorResumeNext Single.just(CudData(CUD_ERROR, "Could not remove route!"))
                }else{
                    deleteRouteInDB(customerRoute).execute()
                    return@onErrorResumeNext Single.just(CudData(CUD_SUCCESS, "Delete cache data successfully!"))
                }
            }
    }


}