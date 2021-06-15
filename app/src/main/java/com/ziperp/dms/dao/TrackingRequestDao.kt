package com.ziperp.dms.dao

import androidx.room.*
import com.ziperp.dms.core.rest.TrackingRequest
import com.ziperp.dms.main.timekeeping.model.TimeKeeping
import io.reactivex.Single

@Dao
interface TrackingRequestDao {

    @Query("SELECT * FROM trackingRequest")
    fun getAllTrackingRequest(): Single<List<TrackingRequest>>

    @Query("SELECT * FROM trackingRequest")
    fun getAllTrackingRequestSync(): List<TrackingRequest>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(trackingRequest: TrackingRequest): Single<Long>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(trackingRequest: List<TrackingRequest>): Single<List<Long>>

    @Delete
    fun delete(trackingRequest: TrackingRequest)

    @Delete
    fun delete(trackingRequests: List<TrackingRequest>)

    @Query("DELETE FROM trackingRequest")
    fun deleteAll()

}


