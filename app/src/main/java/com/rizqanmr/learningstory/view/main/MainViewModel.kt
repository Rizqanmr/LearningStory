package com.rizqanmr.learningstory.view.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.liveData
import com.rizqanmr.learningstory.base.BaseListItem
import com.rizqanmr.learningstory.base.BaseViewModel
import com.rizqanmr.learningstory.data.StoryPagingSource
import com.rizqanmr.learningstory.data.repository.AppRepository
import com.rizqanmr.learningstory.data.model.UserModel
import kotlinx.coroutines.launch

class MainViewModel(
    private val repository: AppRepository
) : BaseViewModel() {

    private var listStoryLiveData: LiveData<PagingData<BaseListItem>> = MutableLiveData()

    fun getSession(): LiveData<UserModel> {
        return repository.getSession().asLiveData()
    }

    fun logout() {
        viewModelScope.launch {
            repository.logout()
        }
    }

    fun getStories() = viewModelScope.launch {
        setIsLoading(true)

        val pager = Pager(
            config = PagingConfig(
                pageSize = 5,
                prefetchDistance = 1,
                enablePlaceholders = false
            ),
            pagingSourceFactory = {
                StoryPagingSource(repository)
            }
        )
        listStoryLiveData = pager.liveData.cachedIn(viewModelScope)

        setIsLoading(false)
    }

    fun listStoryLiveData(): LiveData<PagingData<BaseListItem>> = listStoryLiveData

}