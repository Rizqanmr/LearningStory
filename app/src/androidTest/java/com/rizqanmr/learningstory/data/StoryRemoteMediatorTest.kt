package com.rizqanmr.learningstory.data

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingConfig
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.rizqanmr.learningstory.data.api.ApiService
import com.rizqanmr.learningstory.data.model.reqbody.LoginReqBody
import com.rizqanmr.learningstory.data.model.reqbody.RegisterReqBody
import com.rizqanmr.learningstory.data.model.response.LoginResponse
import com.rizqanmr.learningstory.data.model.response.RegisterResponse
import com.rizqanmr.learningstory.data.model.response.StoryDetailResponse
import com.rizqanmr.learningstory.data.model.response.StoryItemResponse
import com.rizqanmr.learningstory.data.model.response.StoryResponse
import com.rizqanmr.learningstory.database.StoryDatabase
import kotlinx.coroutines.test.runTest
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.junit.After
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith

@ExperimentalPagingApi
@RunWith(AndroidJUnit4::class)
class StoryRemoteMediatorTest {

    private var mockApi: ApiService = FakeApiService()
    private var mockDb: StoryDatabase = Room.inMemoryDatabaseBuilder(
        ApplicationProvider.getApplicationContext(),
        StoryDatabase::class.java
    ).allowMainThreadQueries().build()

    @Test
    fun refreshLoadReturnsSuccessResultWhenMoreDataIsPresent() = runTest {
        val remoteMediator = StoryRemoteMediator(
            mockDb,
            mockApi,
        )
        val pagingState = PagingState<Int, StoryItemResponse>(
            listOf(),
            null,
            PagingConfig(10),
            10
        )
        val result = remoteMediator.load(LoadType.REFRESH, pagingState)
        assertTrue(result is RemoteMediator.MediatorResult.Success)
        assertFalse((result as RemoteMediator.MediatorResult.Success).endOfPaginationReached)
    }

    @After
    fun tearDown() {
        mockDb.clearAllTables()
    }
}

class FakeApiService : ApiService {

    override suspend fun register(registerReqBody: RegisterReqBody): RegisterResponse {
        TODO("Not yet implemented")
    }

    override suspend fun login(loginReqBody: LoginReqBody): LoginResponse {
        TODO("Not yet implemented")
    }

    override suspend fun getStories(location: Int?, page: Int?, size: Int?): StoryResponse {
        val items: MutableList<StoryItemResponse> = arrayListOf()
        for (i in 0..100) {
            val story = StoryItemResponse(
                "photo $i",
                "createdAt + $i",
                "name $i",
                "desc $i",
                null,
                "id $i",
                null,
            )
            items.add(story)
        }
        if (page != null) {
            items.subList((page - 1) * size!!, (page - 1) * size + size)
        }
        return StoryResponse(
            error = false,
            message = "Stories fetched successfully",
            listStory = items
        )
    }

    override suspend fun getStoryDetail(storyId: String): StoryDetailResponse {
        TODO("Not yet implemented")
    }

    override suspend fun createStory(
        file: MultipartBody.Part,
        description: RequestBody,
        lat: RequestBody?,
        lon: RequestBody?
    ): RegisterResponse {
        TODO("Not yet implemented")
    }
}