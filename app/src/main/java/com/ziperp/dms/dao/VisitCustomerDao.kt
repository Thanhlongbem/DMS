package com.ziperp.dms.dao

import androidx.room.*
import com.ziperp.dms.main.visitcustomer.model.VisitCustomer
import io.reactivex.Single

@Dao
interface VisitCustomerDao {

    @Query("SELECT * FROM visitCustomer")
    fun getAllVisitCustomer(): Single<List<VisitCustomer>>

    @Query("SELECT * FROM visitCustomer")
    fun getAllVisitCustomerSync(): List<VisitCustomer>

    @Query("SELECT * FROM visitCustomer WHERE cstCd =:cstCd")
    fun getVisitCustomers(cstCd: String): Single<List<VisitCustomer>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(visitCustomer: VisitCustomer): Single<Long>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(visitCustomer: List<VisitCustomer>): Single<List<Long>>

    @Delete
    fun delete(visitCustomer: VisitCustomer)

    @Query("DELETE FROM visitCustomer")
    fun deleteAll()

}


