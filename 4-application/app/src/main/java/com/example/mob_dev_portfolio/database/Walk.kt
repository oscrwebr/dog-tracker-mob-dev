package com.example.mob_dev_portfolio.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "walks_table")
data class Walk (
    @PrimaryKey
    val walkId: Int,
    val walkName: String,
    val date: String,
    val time: String,
    val distance: Double,
    val walkRoute: String?
)