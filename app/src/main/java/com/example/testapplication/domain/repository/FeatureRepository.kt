package com.example.testapplication.domain.repository

import androidx.lifecycle.LiveData
import com.example.testapplication.data.db.BootEventEntity
import com.example.testapplication.domain.models.BootEventModel
import kotlinx.coroutines.flow.Flow

interface FeatureRepository {

    suspend fun insertBootEvent(bootTimestamp: Long)

    suspend fun getBootEventTimestamp(): List<Long>

    suspend fun getBootEventTimestampsLiveData(): LiveData<List<BootEventEntity>>

    suspend fun getBootEventTimestampsFlow(): Flow<List<BootEventModel>>
}