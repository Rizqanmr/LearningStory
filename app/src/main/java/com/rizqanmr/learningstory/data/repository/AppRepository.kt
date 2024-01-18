package com.rizqanmr.learningstory.data.repository

import androidx.lifecycle.LiveData
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.google.gson.Gson
import com.rizqanmr.learningstory.data.StoryRemoteMediator
import com.rizqanmr.learningstory.data.api.ApiConfig
import com.rizqanmr.learningstory.data.api.ApiService
import com.rizqanmr.learningstory.data.model.UserModel
import com.rizqanmr.learningstory.data.model.reqbody.LoginReqBody
import com.rizqanmr.learningstory.data.pref.UserPreference
import com.rizqanmr.learningstory.data.model.reqbody.RegisterReqBody
import com.rizqanmr.learningstory.data.model.response.*
import com.rizqanmr.learningstory.database.StoryDatabase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.HttpException


class AppRepository private constructor(
    private val userPreference: UserPreference,
    private val apiService: ApiService,
    private val database: StoryDatabase
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

    suspend fun registerUser(registerReqBody: RegisterReqBody): Result<RegisterResponse> {
        return try {
            val response = apiService.register(registerReqBody)
            Result.Success(response)
        } catch (e: HttpException) {
            Result.Error(getErrorMessage(e))
        }
    }

    suspend fun login(loginReqBody: LoginReqBody): Result<LoginResponse> {
        return try {
            val response = apiService.login(loginReqBody)
            Result.Success(response)
        } catch (e: HttpException) {
            Result.Error(getErrorMessage(e))
        }
    }

    @OptIn(ExperimentalPagingApi::class)
    fun getStories(): LiveData<PagingData<StoryItemResponse>> {
        val apiServiceWithToken = getToken()
        return Pager(
            config = PagingConfig(
                pageSize = 5,
                prefetchDistance = 1,
                enablePlaceholders = false
            ),
            remoteMediator = StoryRemoteMediator(database, apiServiceWithToken),
            pagingSourceFactory = {
                database.storyDao().getAllStory()
            }
        ).liveData
    }

    suspend fun getStoriesWithMap(): Result<StoryResponse> {
        return try {
            val response = getToken().getStories(1, 1, 10)
            Result.Success(response)
        } catch (e: HttpException) {
            Result.Error(getErrorMessage(e))
        }
    }

    suspend fun getStoryDetail(storyId: String): Result<StoryDetailResponse> {
        return try {
            val response = getToken().getStoryDetail(storyId)
            Result.Success(response)
        } catch (e: HttpException) {
            Result.Error(getErrorMessage(e))
        }
    }

    suspend fun createStory(
        imageFile: MultipartBody.Part,
        description: RequestBody,
        lat: RequestBody?,
        lon: RequestBody?
    ) : Result<RegisterResponse> {
        return try {
            val response = getToken().createStory(imageFile, description, lat, lon)
            Result.Success(response)
        } catch (e: HttpException) {
            Result.Error(getErrorMessage(e))
        }
    }

    private fun getToken(): ApiService {
        val user = runBlocking { userPreference.getSession().first() }
        return ApiConfig.getApiService(user.token)
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
            apiService: ApiService,
            database: StoryDatabase
        ): AppRepository =
            instance ?: synchronized(this) {
                instance ?: AppRepository(userPreference, apiService, database)
            }.also { instance = it }
    }
}