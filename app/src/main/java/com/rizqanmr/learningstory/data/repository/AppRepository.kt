package com.rizqanmr.learningstory.data.repository

import androidx.lifecycle.liveData
import com.rizqanmr.learningstory.data.api.ApiService
import com.rizqanmr.learningstory.data.model.UserModel
import com.rizqanmr.learningstory.data.pref.UserPreference
import com.rizqanmr.learningstory.data.model.reqbody.RegisterReqBody
import kotlinx.coroutines.flow.Flow
import retrofit2.HttpException

class AppRepository private constructor(
    private val userPreference: UserPreference,
    private val apiService: ApiService
) {
    suspend fun saveSession(user: UserModel) {
        userPreference.saveSession(user)
    }

    fun getSession(): Flow<UserModel> {
        return userPreference.getSession()
    }

    suspend fun logout() {
        userPreference.logout()
    }

    fun registerUser(registerReqBody: RegisterReqBody) = liveData {
        emit(Result.Loading)
        try {
            val response = apiService.register(registerReqBody)
            emit(Result.Success(response))
        } catch (e: HttpException) {
            emit(Result.Error(e.message.toString()))
        }
    }

    companion object {
        @Volatile
        private var instance: AppRepository? = null
        fun getInstance(
            userPreference: UserPreference,
            apiService: ApiService
        ): AppRepository =
            instance ?: synchronized(this) {
                instance ?: AppRepository(userPreference, apiService)
            }.also { instance = it }
    }
}