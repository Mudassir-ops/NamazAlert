package com.example.dummymvvmproject.data.local.room

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [PrayerCacheEntity::class ], version = 2)
abstract class PrayerDatabase: RoomDatabase() {

    abstract fun blogDao(): PrayerDao

    companion object{
        val DATABASE_NAME: String = "blog_db"
    }


}