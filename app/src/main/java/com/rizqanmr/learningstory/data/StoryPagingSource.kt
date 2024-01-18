package com.rizqanmr.learningstory.data

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.rizqanmr.learningstory.data.api.ApiService
import com.rizqanmr.learningstory.data.model.response.StoryItemResponse

class StoryPagingSource(private val apiService: ApiService) : PagingSource<Int, StoryItemResponse>() {

    private companion object {
        const val INITIAL_PAGE_INDEX = 1
    }
    override fun getRefreshKey(state: PagingState<Int, StoryItemResponse>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, StoryItemResponse> {
        return try {
            val page = params.key ?: INITIAL_PAGE_INDEX

            val responseData = apiService.getStories(1, page, params.loadSize)
            LoadResult.Page(
                data = responseData.listStory,
                prevKey = if (page == 1) null else page - 1,
                nextKey = if (responseData.listStory.isEmpty()) null else page + 1
            )
        } catch (exception: Exception) {
            return LoadResult.Error(exception)
        }
    }
}