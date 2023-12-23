package com.rizqanmr.learningstory.view.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import com.rizqanmr.learningstory.data.model.response.StoryItemResponse
import com.rizqanmr.learningstory.databinding.ItemStoryBinding

class StoryAdapter : PagingDataAdapter<StoryItemResponse, StoryViewHolder>(DIFF_CALLBACK) {

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<StoryItemResponse>() {
            override fun areItemsTheSame(oldItem: StoryItemResponse, newItem: StoryItemResponse): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: StoryItemResponse, newItem: StoryItemResponse): Boolean {
                return oldItem.id == newItem.id
            }
        }
    }

    private lateinit var storyListener: StoryListener

    fun setStoryListener(storyListener: StoryListener) {
        this.storyListener = storyListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoryViewHolder {
        val binding = ItemStoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return StoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: StoryViewHolder, position: Int) {
        val item = getItem(position)
        if (item != null) {
            holder.bindData(item, storyListener)
        }
    }

    interface StoryListener {
        fun onItemClick(itemStory: ItemStoryBinding, itemResponse: StoryItemResponse)
    }
}