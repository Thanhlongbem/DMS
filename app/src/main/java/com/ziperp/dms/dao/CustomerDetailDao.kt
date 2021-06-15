package com.ziperp.dms.dao

import androidx.room.*
import com.ziperp.dms.main.customer.customerdetail.model.CustomerDetail
import io.reactivex.Maybe
import io.reactivex.Single

@Dao
interface CustomerDetailDao {

    @Query("SELECT * FROM customerDetail")
    fun getAllCustomerDetail(): Single<List<CustomerDetail>>

    @Query("SELECT * FROM customerDetail")
    fun getAllCustomerDetailSync(): List<CustomerDetail>

    @Query("SELECT * FROM customerDetail WHERE cstCd =:cstCd LIMIT 1")
    fun getCustomerDetail(cstCd: String): Single<CustomerDetail>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(customerDetail: CustomerDetail): Single<Long>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(customerDetail: List<CustomerDetail>): Single<List<Long>>

    @Delete
    fun delete(customerDetail: CustomerDetail)

    @Delete
    fun delete(customerDetails: List<CustomerDetail>)

    @Query("DELETE FROM customerDetail")
    fun deleteAll()

}


