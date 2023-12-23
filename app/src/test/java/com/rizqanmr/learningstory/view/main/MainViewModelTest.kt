package com.rizqanmr.learningstory.view.main

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.AsyncPagingDataDiffer
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.PagingState
import androidx.recyclerview.widget.ListUpdateCallback
import com.rizqanmr.learningstory.DataDummy
import com.rizqanmr.learningstory.MainDispatcherRule
import com.rizqanmr.learningstory.data.model.response.StoryItemResponse
import com.rizqanmr.learningstory.data.repository.AppRepository
import com.rizqanmr.learningstory.getOrAwaitValue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations
import org.robolectric.RobolectricTestRunner

@ExperimentalCoroutinesApi
@RunWith(RobolectricTestRunner::class)
class MainViewModelTest {
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()
    @get:Rule
    val mainDispatcherRules = MainDispatcherRule()
    @Mock
    private lateinit var mockRepository: AppRepository

    private lateinit var viewModel: MainViewModel

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        viewModel = MainViewModel(mockRepository)
    }

    @Test
    fun `getSession should return session data`() {
        val fakeUser = DataDummy.generateDummyUserSession()
        `when`(mockRepository.getSession()).thenReturn(flowOf(fakeUser))

        val result = viewModel.getSession().getOrAwaitValue()
        assertEquals(fakeUser, result)
    }

    @Test
    fun `logout should call repository's logout`() {
        viewModel.logout()
        runBlocking { verify(mockRepository).logout() }
    }

    @Test
    fun `when Get Story Should Not Null and Return Data`() = runTest {
        val dummyStory = DataDummy.generateDummyStoryResponse()
        val data: PagingData<StoryItemResponse> = StoryPagingSource.snapshot(dummyStory)
        val expectedStory = MutableLiveData<PagingData<StoryItemResponse>>()
        expectedStory.value = data

        `when`(mockRepository.getStories()).thenReturn(expectedStory)

        val actualStory = viewModel.getStories().getOrAwaitValue()
        val differ = AsyncPagingDataDiffer(
            diffCallback = StoryAdapter.DIFF_CALLBACK,
            updateCallback = noopListUpdateCallback,
            workerDispatcher = Dispatchers.Main,
        )
        differ.submitData(actualStory)

        assertNotNull(differ.snapshot())
        assertEquals(dummyStory.size, differ.snapshot().size)
        assertEquals(dummyStory[0], differ.snapshot()[0])
    }

    @Test
    fun `when Get Story Empty Should Return No Data`() = runTest {
        val data: PagingData<StoryItemResponse> = PagingData.from(emptyList())
        val expectedQuote = MutableLiveData<PagingData<StoryItemResponse>>()
        expectedQuote.value = data

        `when`(mockRepository.getStories()).thenReturn(expectedQuote)

        val actualQuote = viewModel.getStories().getOrAwaitValue()
        val differ = AsyncPagingDataDiffer(
            diffCallback = StoryAdapter.DIFF_CALLBACK,
            updateCallback = noopListUpdateCallback,
            workerDispatcher = Dispatchers.Main,
        )
        differ.submitData(actualQuote)
        assertEquals(0, differ.snapshot().size)
    }
}

class StoryPagingSource : PagingSource<Int, LiveData<List<StoryItemResponse>>>() {
    companion object {
        fun snapshot(items: List<StoryItemResponse>): PagingData<StoryItemResponse> {
            return PagingData.from(items)
        }
    }
    override fun getRefreshKey(state: PagingState<Int, LiveData<List<StoryItemResponse>>>): Int {
        return 0
    }
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, LiveData<List<StoryItemResponse>>> {
        return LoadResult.Page(emptyList(), 0, 1)
    }
}

val noopListUpdateCallback = object : ListUpdateCallback {
    override fun onInserted(position: Int, count: Int) {}
    override fun onRemoved(position: Int, count: Int) {}
    override fun onMoved(fromPosition: Int, toPosition: Int) {}
    override fun onChanged(position: Int, count: Int, payload: Any?) {}
}