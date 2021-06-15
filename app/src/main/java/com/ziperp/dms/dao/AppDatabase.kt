package com.ziperp.dms.dao

import androidx.room.*
import com.ziperp.dms.camera.CustomerImage
import com.ziperp.dms.core.rest.TrackingRequest
import com.ziperp.dms.main.customer.customerdetail.model.CustomerDetail
import com.ziperp.dms.main.customer.customerroute.model.CustomerRoute
import com.ziperp.dms.main.timekeeping.model.TimeKeeping
import com.ziperp.dms.main.visitcustomer.model.VisitCustomer
import io.reactivex.Observable
import io.reactivex.Single

@Database(entities = arrayOf(FormControl::class, CachedRequest::class, TimeKeeping::class,
    TrackingRequest::class, CustomerDetail::class, CustomerImage::class, CustomerRoute::class, VisitCustomer::class), version = 20)
abstract class AppDatabase : RoomDatabase() {
    abstract fun formControlDao(): FormControlDao
    abstract fun cachedRequestDao(): CachedRequestDao
    abstract fun timeKeepingDao(): TimeKeepingDao
    abstract fun trackingRequestDao(): TrackingRequestDao
    abstract fun customerDetailDao(): CustomerDetailDao
    abstract fun customerImageDao(): CustomerImageDao
    abstract fun customerRouteDao(): CustomerRouteDao
    abstract fun visitCustomerDao(): VisitCustomerDao
}


