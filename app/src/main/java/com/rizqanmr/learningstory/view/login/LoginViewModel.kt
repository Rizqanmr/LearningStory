package com.rizqanmr.learningstory.view.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rizqanmr.learningstory.data.repository.AppRepository
import com.rizqanmr.learningstory.data.model.UserModel
import com.rizqanmr.learningstory.data.model.reqbody.LoginReqBody
import kotlinx.coroutines.launch

class LoginViewModel(private val repository: AppRepository) : ViewModel() {
    fun saveSession(user: UserModel) {
        viewModelScope.launch {
            repository.saveSession(user)
        }
    }

    fun login(loginReqBody: LoginReqBody) = repository.login(loginReqBody)
}