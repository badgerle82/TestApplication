package com.example.testapplication.data.repository

import android.util.Log
import androidx.lifecycle.LiveData
import com.example.testapplication.data.db.BootEventsDAO
import com.example.testapplication.data.db.BootEventEntity
import com.example.testapplication.domain.models.BootEventModel
import com.example.testapplication.domain.repository.FeatureRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class FeatureRepositoryImpl @Inject constructor(
    private val bootEventsDao: BootEventsDAO
) : FeatureRepository {



    override suspend fun insertBootEvent(bootTimestamp: Long) {
        bootEventsDao.insertBootEvent(BootEventEntity(timestamp = bootTimestamp))
        Log.d("StuupidBoot", "BootEventsRepositoryImpl.insertBootEvent")
    }

    override suspend fun getBootEventTimestamp(): List<Long> {
        val bootEventsEntities = bootEventsDao.getBootEventsTimestamp()
        val timestamps = mutableListOf<Long>()
        for (i in bootEventsEntities) {
            timestamps.add(i.timestamp)
        }
        Log.d("StuupidBoot", "BootEventsRepositoryImpl.getBootEventTimestamp")
        return timestamps
    }

    override suspend fun getBootEventTimestampsLiveData(): LiveData<List<BootEventEntity>> {
        Log.d("StuupidBoot", "BootEventsRepositoryImpl.getBootEventTimestampsLiveData")
        return bootEventsDao.getBootEventsTimestampsLiveData()
    }

    override suspend fun getBootEventTimestampsFlow(): Flow<List<BootEventModel>> {
        Log.d("StuupidBoot", "BootEventsRepositoryImpl.getBootEventTimestampsFlow")
        return bootEventsDao.getBootEventsTimestampsFlow().map { list ->
            list.map { entity ->
                BootEventModel(id = entity.id, timestamp = entity.timestamp)
            }
        }
    }
}