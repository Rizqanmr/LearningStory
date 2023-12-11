package com.rizqanmr.learningstory.base

import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

abstract class BaseListAdapter(diffCallback: DiffUtil.ItemCallback<BaseListItem>) :
    ListAdapter<BaseListItem, BaseListAdapter.ViewHolder>(diffCallback) {

    abstract fun onBind(binding: ViewDataBinding, position: Int, item: BaseListItem)

    abstract fun onCustomViewHolder(parent: ViewGroup, viewType: Int): ViewHolder

    override fun getItemViewType(position: Int): Int {
        return getItem(position).itemTypeId
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return onCustomViewHolder(parent, viewType)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        onBind(holder.binding, position, item)
        holder.binding.executePendingBindings()
    }

    fun updateItems(items: List<BaseListItem>) {
        submitList(items)
    }

    fun addItems(items: List<BaseListItem>) {
        submitList(currentList.toMutableList().apply { addAll(items) })
    }

    fun clearItems() {
        submitList(null)
    }

    open class ViewHolder(val binding: ViewDataBinding) : RecyclerView.ViewHolder(binding.root)
}