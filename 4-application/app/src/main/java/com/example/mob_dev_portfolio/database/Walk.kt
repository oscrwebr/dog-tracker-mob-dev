package com.example.mob_dev_portfolio.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "walks_table")
data class Walk (
    @PrimaryKey(autoGenerate = true)
    val walkId: Int,
    val petId: Int,
    val walkName: String,
    val date: String,
    val time: String,
    val walkRoute: String
)