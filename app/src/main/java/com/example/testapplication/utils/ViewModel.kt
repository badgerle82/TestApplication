package com.example.testapplication.utils

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

@Suppress("unused")
fun <T> ViewModel.mutable(value: T? = null): MutableLiveData<T> {
    return MutableLiveData<T>().apply {
        value?.let { this.value = it }
    }
}