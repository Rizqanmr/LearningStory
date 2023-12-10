package com.rizqanmr.learningstory.view.createstory

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.rizqanmr.learningstory.base.BaseViewModel
import com.rizqanmr.learningstory.data.model.response.RegisterResponse
import com.rizqanmr.learningstory.data.repository.AppRepository
import com.rizqanmr.learningstory.data.repository.Result
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

class CreateStoryViewModel(private val repository: AppRepository, ) : BaseViewModel() {

    private val createStoryLiveData: MutableLiveData<RegisterResponse?> = MutableLiveData()
    private val errorCreateStoryLiveData: MutableLiveData<String> = MutableLiveData()

    fun createStory(file: File, description: String) = viewModelScope.launch {
        setIsLoading(true)

        val requestBody = description.toRequestBody("text/plain".toMediaType())
        val requestImageFile = file.asRequestBody("image/jpeg".toMediaType())
        val multipartBody = MultipartBody.Part.createFormData(
            "photo",
            file.name,
            requestImageFile
        )

        when (val result = repository.createStory(multipartBody, requestBody)) {
            is Result.Success -> createStoryLiveData.value = result.data
            is Result.Error -> errorCreateStoryLiveData.value = result.error
        }

        setIsLoading(false)
    }

    fun createStoryLiveData(): MutableLiveData<RegisterResponse?> = createStoryLiveData

    fun errorCreateStoryLiveData(): LiveData<String> = errorCreateStoryLiveData
}