package com.example.testapplication.data.db

import androidx.lifecycle.LiveData
import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface BootEventsDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBootEvent(bootCounter: BootEventEntity)

    @Delete
    suspend fun deleteBootEvent(bootCounter: BootEventEntity)

    @Query("SELECT * FROM boot_table ORDER BY timestamp ASC")
    suspend fun getBootEventsTimestamp(): List<BootEventEntity>

    @Query("SELECT * FROM boot_table ORDER BY timestamp ASC")
    fun getBootEventsTimestampsLiveData(): LiveData<List<BootEventEntity>>

    @Query("SELECT * FROM boot_table ORDER BY timestamp ASC")
    fun getBootEventsTimestampsFlow(): Flow<List<BootEventEntity>>
}