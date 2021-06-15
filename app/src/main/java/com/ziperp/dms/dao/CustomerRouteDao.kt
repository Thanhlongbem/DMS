package com.ziperp.dms.dao

import androidx.room.*
import com.ziperp.dms.main.customer.customerroute.model.CustomerRoute
import com.ziperp.dms.main.timekeeping.model.TimeKeeping
import io.reactivex.Maybe
import io.reactivex.Observable
import io.reactivex.Single

@Dao
interface CustomerRouteDao {

    @Query("SELECT * FROM customerRoute WHERE cstCd =:cstCd")
    fun getAllRoutes(cstCd: String): Single<List<CustomerRoute>>

    @Query("SELECT * FROM customerRoute")
    fun getAllRoutes(): Single<List<CustomerRoute>>

    @Query("SELECT * FROM customerRoute")
    fun getAllRoutesSync(): List<CustomerRoute>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(customerRoute: CustomerRoute): Single<Long>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(customerRoutes: List<CustomerRoute>): Single<List<Long>>

    @Delete
    fun delete(customerRoute: CustomerRoute)

    @Query("DELETE FROM customerRoute WHERE cstCd =:cstCd")
    fun deleteAllByCstCd(cstCd: String)

    @Query("DELETE FROM customerRoute")
    fun deleteAll()

}


