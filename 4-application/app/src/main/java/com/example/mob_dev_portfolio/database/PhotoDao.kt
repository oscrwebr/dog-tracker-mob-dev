package com.example.mob_dev_portfolio.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface PhotoDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addPhoto(photo: Photo)

    @Query("SELECT * FROM photo_table ORDER BY photoId ASC")
    fun getPhotos(): LiveData<List<Photo>>

    @Query("UPDATE photo_table SET name = :changedName WHERE photoId = :photoId")
    fun changeName(changedName: String, photoId: Int)

}