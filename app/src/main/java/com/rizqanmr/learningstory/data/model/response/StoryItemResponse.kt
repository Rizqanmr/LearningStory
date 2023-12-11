package com.rizqanmr.learningstory.data.model.response

import com.google.gson.annotations.SerializedName
import com.rizqanmr.learningstory.base.BaseListItem

data class StoryItemResponse(
    @field:SerializedName("photoUrl")
    val photoUrl: String? = null,

    @field:SerializedName("createdAt")
    val createdAt: String? = null,

    @field:SerializedName("name")
    val name: String? = null,

    @field:SerializedName("description")
    val description: String? = null,

    @field:SerializedName("lon")
    val lon: Double? = null,

    @field:SerializedName("id")
    val id: String? = null,

    @field:SerializedName("lat")
    val lat: Double? = null
) : BaseListItem(0)
