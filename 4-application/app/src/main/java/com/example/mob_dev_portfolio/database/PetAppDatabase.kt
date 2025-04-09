package com.example.mob_dev_portfolio.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

// Room Database Learning from Stevdza-San ROOM Database Tutorial 2020 (Link: https://www.youtube.com/watch?v=lwAvI3WDXBY&list=PLSrm9z4zp4mEPOfZNV9O-crOhoMa0G2-o)
@Database(entities = [Pet::class, Photo::class, Walk::class], version = 7)
abstract class PetAppDatabase: RoomDatabase() {

    abstract fun petDao(): PetDao
    abstract fun photoDao(): PhotoDao
    abstract fun walkDao(): WalkDao

    companion object{
        @Volatile
        private var INSTANCE: PetAppDatabase? = null

        fun getDatabase(context: Context): PetAppDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = buildRoomDB(context)
                INSTANCE = instance
                return instance
            }
        }


        private fun buildRoomDB(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                PetAppDatabase::class.java,
                "pet_app_database"
            )
                .fallbackToDestructiveMigration()
                .build()
        }


}