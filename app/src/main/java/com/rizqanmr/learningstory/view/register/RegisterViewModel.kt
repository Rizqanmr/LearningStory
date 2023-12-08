package com.rizqanmr.learningstory.view.register

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.rizqanmr.learningstory.base.BaseViewModel
import com.rizqanmr.learningstory.data.repository.AppRepository
import com.rizqanmr.learningstory.data.model.reqbody.RegisterReqBody
import com.rizqanmr.learningstory.data.model.response.RegisterResponse
import com.rizqanmr.learningstory.data.repository.Result
import kotlinx.coroutines.launch

class RegisterViewModel(private val repository: AppRepository) : BaseViewModel() {

    private val registerLiveData: MutableLiveData<RegisterResponse?> = MutableLiveData()
    private val errorRegisterLiveData: MutableLiveData<String> = MutableLiveData()

    fun registerUser(registerReqBody: RegisterReqBody) = viewModelScope.launch {
        setIsLoading(true)

        when (val result = repository.registerUser(registerReqBody)) {
            is Result.Success -> registerLiveData.value = result.data
            is Result.Error -> errorRegisterLiveData.value = result.error
        }

        setIsLoading(false)
    }

    fun registerLiveData(): MutableLiveData<RegisterResponse?> = registerLiveData

    fun errorRegisterLiveData(): LiveData<String> = errorRegisterLiveData
}