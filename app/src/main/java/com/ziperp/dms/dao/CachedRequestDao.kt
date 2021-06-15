package com.ziperp.dms.dao

import androidx.room.*
import io.reactivex.Single

@Dao
interface CachedRequestDao {

    @Query("SELECT * FROM cachedRequest")
    fun getCachedRequests(): Single<List<CachedRequest>>

    @Query("SELECT * FROM cachedRequest WHERE requestId = :requestId")
    fun getCachedRequestsByType(requestId: Int): Single<List<CachedRequest>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(cachedRequest: CachedRequest)

    @Query("DELETE FROM cachedRequest WHERE timestamp = :id")
    fun deleteById(id: Long)
    
}


