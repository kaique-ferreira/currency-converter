package com.aruana.currencyprice.databinding

import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.aruana.currencyprice.BR

class DataBindingViewHolder<T>(private val binding: ViewDataBinding) :
        RecyclerView.ViewHolder(binding.root) {

    fun bind(item: T) {
        binding.setVariable(BR.item, item)
        binding.executePendingBindings()
    }
}