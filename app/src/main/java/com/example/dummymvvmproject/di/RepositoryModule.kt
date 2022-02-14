package com.example.dummymvvmproject.di

import com.example.dummymvvmproject.data.local.datastore.AppPrefsStorage
import com.example.dummymvvmproject.data.local.room.CacheMapperPrayer
import com.example.dummymvvmproject.data.local.room.OldCahcerToNewModelMappper
import com.example.dummymvvmproject.data.local.room.PrayerDao
import com.example.dummymvvmproject.data.remote.retrofit.PrayerRetrofit
import com.example.dummymvvmproject.repository.MainRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Singleton
    @Provides
    fun provideMainRepository(
        prayerDao: PrayerDao,
        retrofit: PrayerRetrofit,
        cacheMapperPrayer: CacheMapperPrayer,
        oldCahcerToNewModelMappper: OldCahcerToNewModelMappper,
        networkMapperRetrofit: com.example.dummymvvmproject.data.remote.retrofit.NetworkMapperRetrofit,
        prefsStorage: AppPrefsStorage
    ): MainRepository {
        return MainRepository(
            prayerDao,
            retrofit,
            cacheMapperPrayer,
            oldCahcerToNewModelMappper,
            networkMapperRetrofit,
            prefsStorage
        )
    }
}














