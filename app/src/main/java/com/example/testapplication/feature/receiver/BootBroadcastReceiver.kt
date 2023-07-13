package com.example.testapplication.feature.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.annotation.CallSuper
import androidx.work.WorkInfo
import androidx.work.WorkManager
import com.example.testapplication.domain.repository.FeatureRepository
import com.example.testapplication.feature.work_manager.Utils.setPeriodicallyWorkRequest
import com.example.testapplication.utils.Constants.PERIODIC_WORKER_TAG
import com.google.common.util.concurrent.ListenableFuture
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import java.util.concurrent.ExecutionException
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

abstract class HiltBootBroadcastReceiver : BroadcastReceiver() {
    @CallSuper
    override fun onReceive(context: Context?, intent: Intent?) {}
}

@AndroidEntryPoint
class BootBroadcastReceiver : HiltBootBroadcastReceiver() {

    @Inject lateinit var featureRepository: FeatureRepository

    override fun onReceive(context: Context?, intent: Intent?) {
        super.onReceive(context, intent)
        goAsync {

            context?.let {
                if (!isWorkScheduled(PERIODIC_WORKER_TAG, it)) {
                    setPeriodicallyWorkRequest(it)
                }
            }

            val intentAction = intent?.action
            if (intentAction.equals(Intent.ACTION_BOOT_COMPLETED)
//                || intentAction.equals(Intent.ACTION_EXTERNAL_APPLICATIONS_AVAILABLE)
            ) {
                val timestamp = System.currentTimeMillis()
                featureRepository.insertBootEvent(timestamp)
                Log.d("StuupidBoot", "BootBroadcastReceiver.onReceive bootEventsRepository.insertBootEvent timestamp=$timestamp")
                // TO-DO: Code to handle BOOT COMPLETED EVENT
                // TO-DO: I can start an service.. display a notification... start an activity

            }
        }
    }

    private fun isWorkScheduled(tag: String, context: Context): Boolean {
        val workInstance = WorkManager.getInstance (context)
        val statuses: ListenableFuture<List<WorkInfo>> = workInstance.getWorkInfosByTag(tag)

        var running = false
        var workInfoList: List<WorkInfo> = mutableListOf()

        try {
            workInfoList = statuses.get()
        } catch (e: ExecutionException) {
            Log.d("BroadcastReceiver", "ExecutionException in isWorkScheduled: $e")
        } catch (e: InterruptedException) {
            Log.d("BroadcastReceiver", "InterruptedException in isWorkScheduled: $e")
        }
        for (workInfo in workInfoList) {
            val state: WorkInfo.State = workInfo.state
            running =
                running || (state == WorkInfo.State.RUNNING || state == WorkInfo.State.ENQUEUED)
        }
        return running
    }
}

fun BroadcastReceiver.goAsync(
    context: CoroutineContext = EmptyCoroutineContext,
    block: suspend CoroutineScope.() -> Unit
) {
    val pendingResult = goAsync()
    CoroutineScope(SupervisorJob()).launch(context) {
        try {
            block()
        } finally {
            pendingResult.finish()
        }
    }
}