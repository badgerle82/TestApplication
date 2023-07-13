package com.example.testapplication.feature.ui

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.*
import com.example.testapplication.data.db.BootEventEntity
import com.example.testapplication.domain.repository.FeatureRepository
import com.example.testapplication.feature.ui.adapter.BootEventUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BootEventsMainViewModel @Inject constructor(
    private val repository: FeatureRepository
) : ViewModel() {

    lateinit var getTimestampsLiveData: LiveData<List<BootEventEntity>>
//    lateinit var getTimestampsLiveDataFromFlow: LiveData<List<BootEventModel>>
    lateinit var getTimestampsLiveDataFromFlow: LiveData<List<BootEventUiModel>>

    init {
//        getTimestamps()
        getTimestampsFromFlow()
    }

    @SuppressLint("SuspiciousIndentation")
    fun mapBootUiList(timestampList: List<BootEventEntity>): List<BootEventUiModel> {
        val bootEventUiList = mutableListOf<BootEventUiModel>()
            if (timestampList.isEmpty()) {
                bootEventUiList.add(BootEventUiModel(id = 0, text = "No boots detected"))
            } else {
                timestampList.forEach {
                    bootEventUiList.add(BootEventUiModel(id = it.id, text = "${it.id} - ${it.timestamp}"))
                }
            }
        Log.d("StuupidBoot", "BootEventsMainViewModel.mapBootUiList bootEventUiList=$bootEventUiList")
        return bootEventUiList
    }

    private fun getTimestamps() {
        viewModelScope.launch {
            getTimestampsLiveData = repository.getBootEventTimestampsLiveData()
        }
    }

    private fun getTimestampsFromFlow() {
        viewModelScope.launch {
            getTimestampsLiveDataFromFlow = repository.getBootEventTimestampsFlow().map { listModels ->
                if (listModels.isEmpty()) {
                    listOf(BootEventUiModel(id = 0, text = "No boots detected"))
                } else {
                    listModels.map { model ->
                        BootEventUiModel(id = model.id, text = "${model.id} - ${model.timestamp}")
                    }
                }
            }.asLiveData()
        }
    }
}