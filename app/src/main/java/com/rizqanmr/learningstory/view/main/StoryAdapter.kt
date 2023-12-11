package com.rizqanmr.learningstory.view.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.DiffUtil
import com.rizqanmr.learningstory.R
import com.rizqanmr.learningstory.data.model.response.StoryItemResponse
import com.rizqanmr.learningstory.databinding.ItemStoryBinding
import com.rizqanmr.learningstory.base.BaseListAdapter
import com.rizqanmr.learningstory.base.BaseListItem

class StoryAdapter : BaseListAdapter(DIFF_CALLBACK) {

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<BaseListItem>() {
            override fun areItemsTheSame(oldItem: BaseListItem, newItem: BaseListItem): Boolean {
                return if (oldItem is StoryItemResponse && newItem is StoryItemResponse) {
                    oldItem.itemTypeId == newItem.itemTypeId
                } else false
            }

            override fun areContentsTheSame(oldItem: BaseListItem, newItem: BaseListItem): Boolean {
                return if (oldItem is StoryItemResponse && newItem is StoryItemResponse) {
                    (oldItem as? StoryItemResponse)?.equals(newItem as? StoryItemResponse)
                        ?: false
                } else false
            }
        }
    }

    private lateinit var storyListener: StoryListener

    fun setStoryListener(storyListener: StoryListener) {
        this.storyListener = storyListener
    }

    override fun onBind(binding: ViewDataBinding, position: Int, item: BaseListItem) {
        // do nothing
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)

        if (holder is StoryViewHolder && item is StoryItemResponse) {
            holder.bindData(item, storyListener)
        }
        super.onBindViewHolder(holder, position)
    }

    override fun onCustomViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return StoryViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.item_story, parent, false
            )
        )
    }

    interface StoryListener {
        fun onItemClick(itemStory: ItemStoryBinding, itemResponse: StoryItemResponse)
    }
}