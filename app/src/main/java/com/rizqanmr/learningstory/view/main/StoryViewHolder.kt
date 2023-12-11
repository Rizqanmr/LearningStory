package com.rizqanmr.learningstory.view.main

import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.rizqanmr.learningstory.R
import com.rizqanmr.learningstory.base.BaseListAdapter
import com.rizqanmr.learningstory.data.model.response.StoryItemResponse
import com.rizqanmr.learningstory.databinding.ItemStoryBinding

class StoryViewHolder(binding: ItemStoryBinding) : BaseListAdapter.ViewHolder(binding) {
    fun bindData(
        itemResponse: StoryItemResponse,
        listener: StoryAdapter.StoryListener
    ) {
        (binding as? ItemStoryBinding)?.let { itemStory ->
            itemStory.item = itemResponse
            with(itemStory) {
                Glide.with(root.context)
                    .load(itemResponse.photoUrl)
                    .apply(RequestOptions.placeholderOf(R.drawable.ic_loading).error(R.drawable.ic_error))
                    .into(ivItemPhoto)

                cvStory.setOnClickListener { listener.onItemClick(itemStory, itemResponse) }
            }
        }
    }
}