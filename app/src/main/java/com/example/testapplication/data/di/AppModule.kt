package com.example.testapplication.data.di

import android.content.Context
import androidx.room.Room
import com.example.testapplication.data.db.TestAppDB
import com.example.testapplication.data.repository.FeatureRepositoryImpl
import com.example.testapplication.domain.repository.FeatureRepository
import com.example.testapplication.utils.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideAppDatabase(
        @ApplicationContext app: Context
    ) = Room.databaseBuilder(
        app,
        TestAppDB::class.java,
        Constants.APP_DB_NAME
    ).build()

    @Provides
    @Singleton
    fun provideBootEventsDao(db: TestAppDB) = db.getBootEventsDao()

    @Provides
    @Singleton
    fun provideFeatureRepository(bootEventsRepositoryImpl: FeatureRepositoryImpl): FeatureRepository = bootEventsRepositoryImpl
}