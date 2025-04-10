package com.example.mob_dev_portfolio.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface MealDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addMeal(meal: Meal)

    @Query("SELECT * FROM meals_table ORDER BY mealId ASC")
    fun getMeals(): LiveData<List<Meal>>

    @Query("SELECT * FROM meals_table WHERE mealDate = :calendarDate")
    fun getMealsOnDate(calendarDate: String): LiveData<List<Meal>>

}