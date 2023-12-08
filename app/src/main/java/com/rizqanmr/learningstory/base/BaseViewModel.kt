package com.rizqanmr.learningstory.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope
import kotlin.coroutines.CoroutineContext

abstract class BaseViewModel : ViewModel(), CoroutineScope {
    private val isLoading = MutableLiveData<Boolean>()

    override val coroutineContext: CoroutineContext
        get() = viewModelScope.coroutineContext

    fun getIsLoading(): LiveData<Boolean> = isLoading

    fun setIsLoading(isLoading: Boolean) {
        this.isLoading.value = isLoading
    }
}