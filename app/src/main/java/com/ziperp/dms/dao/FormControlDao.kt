package com.ziperp.dms.dao

import androidx.room.*
import com.ziperp.dms.main.customer.customerdetail.model.CustomerDetail
import io.reactivex.Maybe
import io.reactivex.Observable
import io.reactivex.Single

@Dao
interface FormControlDao {

    @Query("SELECT * FROM formControl WHERE controlFormId = :controlFormId LIMIT 1")
    fun getFormControl(controlFormId: String): Single<FormControl>

    @Query("SELECT * FROM formControl")
    fun getFormControls(): Single<List<FormControl>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(formControl: FormControl)

    @Query("DELETE FROM formControl")
    fun deleteAll()

    @Delete
    fun delete(formControl: FormControl)

}


