package com.example.mob_dev_portfolio.database

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "photo_table")
data class Photo (
    @PrimaryKey(autoGenerate = true)
    val photoId: Int,
    val name: String?,
    val photo: String
) : Serializable