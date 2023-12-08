package com.rizqanmr.learningstory.view.storydetail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.rizqanmr.learningstory.base.BaseViewModel
import com.rizqanmr.learningstory.data.model.response.StoryDetailResponse
import com.rizqanmr.learningstory.data.repository.AppRepository
import com.rizqanmr.learningstory.data.repository.Result
import kotlinx.coroutines.launch
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Locale

class StoryDetailViewModel(private val repository: AppRepository, ) : BaseViewModel() {

    private var storyId: String = ""
    private val storyDetailLiveData: MutableLiveData<StoryDetailResponse?> = MutableLiveData()
    private val errorStoryDetailData: MutableLiveData<String> = MutableLiveData()

    fun setStoryId(storyId: String) {
        this.storyId = storyId
    }

    fun getStoryDetail() = viewModelScope.launch {
        setIsLoading(true)

        when (val result = repository.getStoryDetail(storyId)) {
            is Result.Success -> storyDetailLiveData.value = result.data
            is Result.Error -> errorStoryDetailData.value = result.error
        }

        setIsLoading(false)
    }

    fun storyDetailLiveData(): MutableLiveData<StoryDetailResponse?> = storyDetailLiveData

    fun errorStoryDetailLiveData(): LiveData<String> = errorStoryDetailData

    fun getDate(date: String): String {
        var simpleDateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
        return try {
            val formatter = simpleDateFormat.parse(date)
            simpleDateFormat = SimpleDateFormat("dd MMM yyyy HH:mm", Locale.getDefault())
            simpleDateFormat.format(formatter!!)
        } catch (e: ParseException) {
            e.printStackTrace()
            date
        }
    }
}