package com.rizqanmr.learningstory.base

import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView

abstract class BaseListAdapter(diffCallback: DiffUtil.ItemCallback<BaseListItem>) :
    PagingDataAdapter<BaseListItem, BaseListAdapter.ViewHolder>(diffCallback) {

    abstract fun onBind(binding: ViewDataBinding, position: Int, item: BaseListItem)

    abstract fun onCustomViewHolder(parent: ViewGroup, viewType: Int): ViewHolder

    override fun getItemViewType(position: Int): Int {
        return getItem(position)?.itemTypeId ?: 0
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return onCustomViewHolder(parent, viewType)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        if (item != null) {
            onBind(holder.binding, position, item)
        }
        holder.binding.executePendingBindings()
    }

    open class ViewHolder(val binding: ViewDataBinding) : RecyclerView.ViewHolder(binding.root)
}