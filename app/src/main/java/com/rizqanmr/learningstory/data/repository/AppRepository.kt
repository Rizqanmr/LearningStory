package com.rizqanmr.learningstory.data.repository

import androidx.lifecycle.liveData
import com.google.gson.Gson
import com.rizqanmr.learningstory.data.api.ApiService
import com.rizqanmr.learningstory.data.model.UserModel
import com.rizqanmr.learningstory.data.model.reqbody.LoginReqBody
import com.rizqanmr.learningstory.data.pref.UserPreference
import com.rizqanmr.learningstory.data.model.reqbody.RegisterReqBody
import com.rizqanmr.learningstory.data.model.response.ErrorResponse
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
            emit(Result.Error(getErrorMessage(e)))
        }
    }

    fun login(loginReqBody: LoginReqBody) = liveData {
        emit(Result.Loading)

        try {
            val response = apiService.login(loginReqBody)
            emit(Result.Success(response))
        } catch (e: HttpException) {
            emit(Result.Error(getErrorMessage(e)))
        }
    }

    private fun getErrorMessage(exception: HttpException) : String {
        val jsonInString = exception.response()?.errorBody()?.string()
        val errorBody = Gson().fromJson(jsonInString, ErrorResponse::class.java)
        return errorBody.message.toString()
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