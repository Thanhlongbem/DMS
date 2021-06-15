package com.ziperp.dms.dao

import androidx.room.*
import com.ziperp.dms.main.timekeeping.model.TimeKeeping
import io.reactivex.Single

@Dao
interface TimeKeepingDao {

    @Query("SELECT * FROM timeKeeping WHERE checkInDay = :day")
    fun getAllTimeKeeping(day: String): Single<List<TimeKeeping>>

    @Query("SELECT * FROM timeKeeping WHERE checkInDay = :day")
    fun getAllTimeKeepingSync(day: String): List<TimeKeeping>

    @Query("SELECT * FROM timeKeeping")
    fun getAllTimeKeeping(): Single<List<TimeKeeping>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(timeKeeping: TimeKeeping): Single<Long>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(timeKeepings: List<TimeKeeping>): Single<List<Long>>

    @Delete
    fun delete(timeKeeping: TimeKeeping)

    @Query("DELETE FROM timeKeeping")
    fun deleteAll()

}


