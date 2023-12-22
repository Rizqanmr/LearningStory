package com.rizqanmr.learningstory.database

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.rizqanmr.learningstory.data.model.response.StoryItemResponse

@Dao
interface StoryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStory(story: List<StoryItemResponse>)

    @Query("SELECT * FROM story")
    fun getAllStory(): PagingSource<Int, StoryItemResponse>

    @Query("DELETE FROM story")
    suspend fun deleteAll()
}