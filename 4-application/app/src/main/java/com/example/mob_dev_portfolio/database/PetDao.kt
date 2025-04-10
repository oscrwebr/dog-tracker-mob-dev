package com.example.mob_dev_portfolio.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query


// Code adapted from: https://developer.android.com/training/data-storage/room#kotlin & https://developer.android.com/kotlin/coroutines

@Dao
interface PetDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addPet(pet: Pet)

    @Query("SELECT * FROM pet_table ORDER BY id ASC")
    fun getPets(): LiveData<List<Pet>>

    @Query("DELETE FROM pet_table WHERE id=:id")
    fun deletePet(id: Int)
}