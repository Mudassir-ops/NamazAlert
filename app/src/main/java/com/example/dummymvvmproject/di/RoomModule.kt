package com.example.dummymvvmproject.di

import android.content.Context
import androidx.room.Room
import com.example.dummymvvmproject.data.local.room.PrayerDao
import com.example.dummymvvmproject.data.local.room.PrayerDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object RoomModule {

    @Singleton
    @Provides
    fun provideBlogDb(@ApplicationContext context: Context): PrayerDatabase {
        return Room
            .databaseBuilder(
                context,
                PrayerDatabase::class.java,
                PrayerDatabase.DATABASE_NAME)
            .fallbackToDestructiveMigration()
            .build()
    }

    @Singleton
    @Provides
    fun provideBlogDAO(prayerDatabase: PrayerDatabase): PrayerDao {
        return prayerDatabase.blogDao()
    }
}