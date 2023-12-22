package com.rizqanmr.learningstory.view.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.liveData
import com.rizqanmr.learningstory.base.BaseViewModel
import com.rizqanmr.learningstory.data.StoryPagingSource
import com.rizqanmr.learningstory.data.StoryRemoteMediator
import com.rizqanmr.learningstory.data.repository.AppRepository
import com.rizqanmr.learningstory.data.model.UserModel
import com.rizqanmr.learningstory.data.model.response.StoryItemResponse
import com.rizqanmr.learningstory.database.StoryDatabase
import kotlinx.coroutines.launch

class MainViewModel(
    private val repository: AppRepository,
    private val database: StoryDatabase
) : BaseViewModel() {

    private var listStoryLiveData: LiveData<PagingData<StoryItemResponse>> = MutableLiveData()

    fun getSession(): LiveData<UserModel> {
        return repository.getSession().asLiveData()
    }

    fun logout() {
        viewModelScope.launch {
            repository.logout()
        }
    }

    fun getStories() : LiveData<PagingData<StoryItemResponse>> {
        @OptIn(ExperimentalPagingApi::class)
        val pager = Pager(
            config = PagingConfig(
                pageSize = 5,
                prefetchDistance = 1,
                enablePlaceholders = false
            ),
            remoteMediator = StoryRemoteMediator(database, repository),
            pagingSourceFactory = {
                //StoryPagingSource(repository)
                database.storyDao().getAllStory()
            }
        )
        listStoryLiveData = pager.liveData.cachedIn(viewModelScope)
        return listStoryLiveData
    }

    fun listStoryLiveData(): LiveData<PagingData<StoryItemResponse>> = listStoryLiveData

}