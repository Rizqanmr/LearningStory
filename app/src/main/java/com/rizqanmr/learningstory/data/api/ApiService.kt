package com.rizqanmr.learningstory.data.api

import com.rizqanmr.learningstory.data.model.reqbody.LoginReqBody
import com.rizqanmr.learningstory.data.model.reqbody.RegisterReqBody
import com.rizqanmr.learningstory.data.model.response.StoryDetailResponse
import com.rizqanmr.learningstory.data.model.response.LoginResponse
import com.rizqanmr.learningstory.data.model.response.RegisterResponse
import com.rizqanmr.learningstory.data.model.response.StoryResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiService {

    @POST("register")
    suspend fun register(@Body registerReqBody: RegisterReqBody): RegisterResponse

    @POST("login")
    suspend fun login(@Body loginReqBody: LoginReqBody): LoginResponse

    @GET("stories")
    suspend fun getStories(): StoryResponse

    @GET("stories/{storyId}")
    suspend fun getStoryDetail(@Path("storyId") storyId: String): StoryDetailResponse
}