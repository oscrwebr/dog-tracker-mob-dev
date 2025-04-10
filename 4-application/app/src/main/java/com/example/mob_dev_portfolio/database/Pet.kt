package com.example.mob_dev_portfolio.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable
import java.util.Date

@Entity(tableName = "pet_table")
data class Pet (
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val name: String,
    val petType: String,
    val breed: String,
    val dateOfBirth: String,
    val gender: String,
    val profilePicture: String?
) : Serializable