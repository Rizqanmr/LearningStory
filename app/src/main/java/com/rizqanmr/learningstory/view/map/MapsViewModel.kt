package com.rizqanmr.learningstory.view.map

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.rizqanmr.learningstory.base.BaseViewModel
import com.rizqanmr.learningstory.data.model.response.StoryItemResponse
import com.rizqanmr.learningstory.data.repository.AppRepository
import com.rizqanmr.learningstory.data.repository.Result
import kotlinx.coroutines.launch

class MapsViewModel(private val repository: AppRepository) : BaseViewModel() {

    private val listStoryLiveData: MutableLiveData<List<StoryItemResponse>> = MutableLiveData()
    private val errorListStoryLiveData: MutableLiveData<String> = MutableLiveData()

    fun getStories() = viewModelScope.launch {
        setIsLoading(true)

        when (val result = repository.getStories(1)) {
            is Result.Success -> listStoryLiveData.value = result.data.listStory
            is Result.Error -> errorListStoryLiveData.value = result.error
        }

        setIsLoading(false)
    }

    fun listStoryLiveData(): MutableLiveData<List<StoryItemResponse>> = listStoryLiveData

    fun errorListStoryLiveData(): LiveData<String> = errorListStoryLiveData
}