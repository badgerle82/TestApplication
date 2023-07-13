package com.example.testapplication.data.db

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [BootEventEntity::class],
    version = 1,
    exportSchema = false
)
abstract class TestAppDB : RoomDatabase() {

    abstract fun getBootEventsDao(): BootEventsDAO
}