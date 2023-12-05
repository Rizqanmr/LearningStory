package com.rizqanmr.learningstory.view.register

import androidx.lifecycle.ViewModel
import com.rizqanmr.learningstory.data.repository.AppRepository
import com.rizqanmr.learningstory.data.model.reqbody.RegisterReqBody

class RegisterViewModel(private val repository: AppRepository) : ViewModel() {

    fun registerUser(registerReqBody: RegisterReqBody) = repository.registerUser(registerReqBody)
}