package com.rizqanmr.learningstory

import com.rizqanmr.learningstory.data.model.UserModel
import com.rizqanmr.learningstory.data.model.response.StoryItemResponse

object DataDummy {

    fun generateDummyStoryResponse(): List<StoryItemResponse> {
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
        return items
    }

    fun generateDummyUserSession() = UserModel("123", "token123", true)
}