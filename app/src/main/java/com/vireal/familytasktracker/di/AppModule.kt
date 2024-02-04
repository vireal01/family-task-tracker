package com.vireal.familytasktracker.di

import android.app.Application
import androidx.room.Room
import com.vireal.familytasktracker.data.database.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Singleton
    @Provides
    fun db(context: Application): AppDatabase = Room.databaseBuilder(
        context.applicationContext,
        AppDatabase::class.java,
        name = "family_task_tracker"
    ).build()
}
