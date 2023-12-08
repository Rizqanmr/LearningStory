package com.rizqanmr.learningstory.view.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.rizqanmr.learningstory.base.BaseViewModel
import com.rizqanmr.learningstory.data.repository.AppRepository
import com.rizqanmr.learningstory.data.model.UserModel
import com.rizqanmr.learningstory.data.model.reqbody.LoginReqBody
import com.rizqanmr.learningstory.data.model.response.LoginResponse
import com.rizqanmr.learningstory.data.repository.Result
import kotlinx.coroutines.launch

class LoginViewModel(private val repository: AppRepository) : BaseViewModel() {

    private val loginLiveData: MutableLiveData<LoginResponse?> = MutableLiveData()
    private val errorLoginLiveData: MutableLiveData<String> = MutableLiveData()

    fun saveSession(user: UserModel) {
        viewModelScope.launch {
            repository.saveSession(user)
        }
    }

    fun login(loginReqBody: LoginReqBody) = viewModelScope.launch {
        setIsLoading(true)

        when (val result = repository.login(loginReqBody)) {
            is Result.Success -> loginLiveData.value = result.data
            is Result.Error -> errorLoginLiveData.value = result.error
        }

        setIsLoading(false)
    }

    fun loginLiveData(): MutableLiveData<LoginResponse?> = loginLiveData

    fun errorLoginLiveData(): LiveData<String> = errorLoginLiveData
}