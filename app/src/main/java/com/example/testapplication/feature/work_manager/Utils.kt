package com.example.testapplication.feature.work_manager

import android.content.Context
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.example.testapplication.utils.Constants
import java.util.concurrent.TimeUnit

object Utils {

    fun setPeriodicallyWorkRequest(context: Context) {
        val workManager = WorkManager.getInstance(context)

        val periodicWorkRequest = PeriodicWorkRequestBuilder<BootEventsWorker>(15, TimeUnit.MINUTES).build()

        workManager.enqueueUniquePeriodicWork(Constants.PERIODIC_WORKER_TAG, ExistingPeriodicWorkPolicy.KEEP, periodicWorkRequest)

//        workManager.getWorkInfoByIdLiveData(periodicWorkRequest.id)
//            .observe(this, Observer { workInfo ->
//            })
    }

    // TODO it's unclear from requirements how we should stop it so I just leave it here
    fun cancelWorkRequest(context: Context) {
        WorkManager.getInstance(context).cancelAllWorkByTag(Constants.PERIODIC_WORKER_TAG)
    }
}