package com.rizqanmr.learningstory.view.main

import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.rizqanmr.learningstory.R
import com.rizqanmr.learningstory.data.model.response.StoryItemResponse
import com.rizqanmr.learningstory.databinding.ItemStoryBinding
import com.rizqanmr.learningstory.util.BaseListAdapter

class StoryAdapter : BaseListAdapter<StoryItemResponse, ItemStoryBinding>() {

    override fun getLayoutId(): Int = R.layout.item_story
    override fun onBind(binding: ItemStoryBinding, position: Int, item: StoryItemResponse) {
        binding.item = item
        binding.apply {
            Glide.with(root.context)
                .load(item.photoUrl)
                .apply(RequestOptions.placeholderOf(R.drawable.ic_loading).error(R.drawable.ic_error))
                .into(ivStory)
        }
    }

}