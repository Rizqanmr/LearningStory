package com.rizqanmr.learningstory.data

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.rizqanmr.learningstory.data.model.response.StoryItemResponse
import com.rizqanmr.learningstory.data.model.response.StoryResponse
import com.rizqanmr.learningstory.data.repository.AppRepository
import com.rizqanmr.learningstory.data.repository.Result

class StoryPagingSource(private val repository: AppRepository) : PagingSource<Int, StoryItemResponse>() {

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
            var response = StoryResponse()

            when (val responseData = repository.getStories(1, page, params.loadSize)) {
                is Result.Success -> response = responseData.data
                is Result.Error -> { }
            }
            LoadResult.Page(
                data = response.listStory,
                prevKey = if (page == 1) null else page - 1,
                nextKey = if (response.listStory.isEmpty()) null else page + 1
            )
        } catch (exception: Exception) {
            return LoadResult.Error(exception)
        }
    }
}