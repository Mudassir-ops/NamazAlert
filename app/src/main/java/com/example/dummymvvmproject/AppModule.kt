package com.example.dummymvvmproject

import android.content.Context

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn


import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@InstallIn(SingletonComponent::class)
@Module
object AppModule {

    @Singleton
    @Provides
    fun provideAppDatabase(@ApplicationContext appContext: Context): AppClass {
        return appContext as AppClass
    }

    @Singleton
    @Provides
    fun provideRandromString():String {
       return "Mudassir Here"
    }

    @Singleton
    @Provides
    fun provideRandomInt():Int {
        return 37397
    }
}