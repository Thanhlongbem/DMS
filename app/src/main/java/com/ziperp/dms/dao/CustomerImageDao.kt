package com.ziperp.dms.dao

import androidx.room.*
import com.ziperp.dms.camera.CustomerImage
import com.ziperp.dms.main.customer.customerroute.model.CustomerRoute
import com.ziperp.dms.main.timekeeping.model.TimeKeeping
import io.reactivex.Maybe
import io.reactivex.Observable
import io.reactivex.Single

@Dao
interface CustomerImageDao {

    @Query("SELECT * FROM customerImage WHERE keyNo =:cstCd AND isVisitCustomer =:isVisitCustomer")
    fun getCustomerImages(cstCd: String, isVisitCustomer: Boolean): Single<List<CustomerImage>>

    @Query("SELECT * FROM customerImage WHERE isVisitCustomer =:isVisitCustomer")
    fun getCustomerImages(isVisitCustomer: Boolean): Single<List<CustomerImage>>

    @Query("SELECT * FROM customerImage")
    fun getCustomerImagesSync(): List<CustomerImage>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(customerImage: CustomerImage): Single<Long>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(customerImages: List<CustomerImage>): Single<List<Long>>

    @Delete
    fun delete(customerImage: CustomerImage)

    @Query("DELETE FROM customerImage WHERE isVisitCustomer =:isVisitCustomer")
    fun deleteAll(isVisitCustomer: Boolean)

    @Query("DELETE FROM customerImage")
    fun deleteAll()

}


