package com.rizqanmr.learningstory.view.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.rizqanmr.learningstory.base.BaseViewModel
import com.rizqanmr.learningstory.data.repository.AppRepository
import com.rizqanmr.learningstory.data.model.UserModel
import com.rizqanmr.learningstory.data.model.response.StoryItemResponse
import kotlinx.coroutines.launch

class MainViewModel(
    private val repository: AppRepository
) : BaseViewModel() {

    fun getSession(): LiveData<UserModel> {
        return repository.getSession().asLiveData()
    }

    fun logout() {
        viewModelScope.launch {
            repository.logout()
        }
    }

    fun getStories() : LiveData<PagingData<StoryItemResponse>> =
        repository.getStories().cachedIn(viewModelScope)

}