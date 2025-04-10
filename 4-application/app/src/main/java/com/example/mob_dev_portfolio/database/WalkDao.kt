package com.example.mob_dev_portfolio.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface WalkDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun addWalk (walk: Walk)

    @Query("SELECT * from walks_table WHERE date = :calendarDate")
    fun getWalksOnDate(calendarDate: String): LiveData<List<Walk>>

    @Query("SELECT * FROM walks_table ORDER BY walkId ASC")
    fun getWalks(): LiveData<List<Walk>>

}