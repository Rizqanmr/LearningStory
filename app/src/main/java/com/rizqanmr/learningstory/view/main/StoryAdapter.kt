package com.rizqanmr.learningstory.view.main

import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.rizqanmr.learningstory.R
import com.rizqanmr.learningstory.data.model.response.StoryItemResponse
import com.rizqanmr.learningstory.databinding.ItemStoryBinding
import com.rizqanmr.learningstory.base.BaseListAdapter

class StoryAdapter : BaseListAdapter<StoryItemResponse, ItemStoryBinding>() {

    private lateinit var storyListener: StoryListener

    fun setStoryListener(storyListener: StoryListener) {
        this.storyListener = storyListener
    }

    override fun getLayoutId(): Int = R.layout.item_story
    override fun onBind(binding: ItemStoryBinding, position: Int, item: StoryItemResponse) {
        binding.item = item
        with(binding) {
            Glide.with(root.context)
                .load(item.photoUrl)
                .apply(RequestOptions.placeholderOf(R.drawable.ic_loading).error(R.drawable.ic_error))
                .into(ivItemPhoto)

            cvStory.setOnClickListener { storyListener.onItemClick(binding, position, item) }
        }
    }

    interface StoryListener {
        fun onItemClick(view: ItemStoryBinding, position: Int, item: StoryItemResponse)
    }
}