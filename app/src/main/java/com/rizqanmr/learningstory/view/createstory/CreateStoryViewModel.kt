package com.rizqanmr.learningstory.view.createstory

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.rizqanmr.learningstory.base.BaseViewModel
import com.rizqanmr.learningstory.data.model.response.RegisterResponse
import com.rizqanmr.learningstory.data.repository.AppRepository
import com.rizqanmr.learningstory.data.repository.Result
import com.rizqanmr.learningstory.util.CommonFunction
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

class CreateStoryViewModel(private val repository: AppRepository, ) : BaseViewModel() {

    private val createStoryLiveData: MutableLiveData<RegisterResponse?> = MutableLiveData()
    private val errorCreateStoryLiveData: MutableLiveData<String> = MutableLiveData()

    fun createStory(
        file: File,
        description: String,
        latitude: String?,
        longitude: String?
    ) = viewModelScope.launch {
        setIsLoading(true)

        val desc = description.toRequestBody("text/plain".toMediaType())
        val lat = latitude?.toRequestBody("text/plain".toMediaType())
        val lon = longitude?.toRequestBody("text/plain".toMediaType())
        val multipartBody = CommonFunction.prepareFilePart(file, "photo")

        when (val result = repository.createStory(multipartBody, desc, lat, lon)) {
            is Result.Success -> createStoryLiveData.value = result.data
            is Result.Error -> errorCreateStoryLiveData.value = result.error
        }

        setIsLoading(false)
    }

    fun createStoryLiveData(): MutableLiveData<RegisterResponse?> = createStoryLiveData

    fun errorCreateStoryLiveData(): LiveData<String> = errorCreateStoryLiveData
}