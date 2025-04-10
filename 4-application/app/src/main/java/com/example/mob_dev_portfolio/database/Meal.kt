package com.example.mob_dev_portfolio.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "meals_table")
data class Meal (
    @PrimaryKey(autoGenerate = true)
    val mealId: Int,
    val petId: Int,
    val mealName: String,
    val mealDate: String,
    val mealTime: String,
) : Serializable