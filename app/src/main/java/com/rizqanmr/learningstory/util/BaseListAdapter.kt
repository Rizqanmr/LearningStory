package com.rizqanmr.learningstory.util

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView

abstract class BaseListAdapter<T, V : ViewDataBinding> :
    RecyclerView.Adapter<BaseListAdapter.ViewHolder<V>>() {

    private var items: List<T> = listOf()

    abstract fun getLayoutId(): Int

    abstract fun onBind(binding: V, position: Int, item: T)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder<V> {
        return ViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                getLayoutId(), parent, false
            )
        )
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: ViewHolder<V>, position: Int) {
        val item = items[position]
        onBind(holder.binding, position, item)
        holder.binding.executePendingBindings()
    }

    protected fun getItems(): List<T> = items

    fun updateItems(items: List<T>) {
        this.items = items
        notifyDataSetChanged()
    }

    fun addItems(items: List<T>) {
        this.items = this.items.toMutableList().apply { addAll(items) }
        notifyDataSetChanged()
    }

    fun clearItems() {
        this.items = listOf()
        notifyDataSetChanged()
    }

    open class ViewHolder<V : ViewDataBinding>(val binding: V) : RecyclerView.ViewHolder(binding.root)
}