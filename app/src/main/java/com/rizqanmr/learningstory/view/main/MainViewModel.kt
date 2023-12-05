package com.rizqanmr.learningstory.view.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.rizqanmr.learningstory.data.repository.AppRepository
import com.rizqanmr.learningstory.data.model.UserModel
import kotlinx.coroutines.launch

class MainViewModel(private val repository: AppRepository) : ViewModel() {
    fun getSession(): LiveData<UserModel> {
        return repository.getSession().asLiveData()
    }

    fun logout() {
        viewModelScope.launch {
            repository.logout()
        }
    }
}