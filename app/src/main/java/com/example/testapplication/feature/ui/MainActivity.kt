package com.example.testapplication.feature.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.testapplication.R
import com.example.testapplication.feature.ui.adapter.BootEventsAdapter
import com.example.testapplication.utils.observeNonNull
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val viewModel: BootEventsMainViewModel by viewModels()
    private lateinit var adapter: BootEventsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val recyclerview = findViewById<RecyclerView>(R.id.recyclerview)
        adapter = BootEventsAdapter()
        recyclerview.adapter = adapter
        recyclerview.layoutManager = LinearLayoutManager(this)

        getTimestamps()
    }

    private fun getTimestamps() {
//        viewModel.getTimestampsLiveData.observe(this) {
//            adapter.updateList(viewModel.mapBootUiList(it))
//        }

        viewModel.getTimestampsLiveDataFromFlow.observeNonNull(this) {
            adapter.updateList(it)
        }
    }
}