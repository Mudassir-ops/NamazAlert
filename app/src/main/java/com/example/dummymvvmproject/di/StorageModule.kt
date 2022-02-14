package com.example.prefrences


import com.example.dummymvvmproject.data.local.datastore.AppPrefsStorage
import com.example.dummymvvmproject.data.local.datastore.PreferenceStorage
import dagger.Binds
import dagger.Module

import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
abstract class StorageModule {

    @Binds
    @Singleton
    abstract fun providesPreferenceStorage(
        appPreferenceStorage: AppPrefsStorage
    ): PreferenceStorage


}