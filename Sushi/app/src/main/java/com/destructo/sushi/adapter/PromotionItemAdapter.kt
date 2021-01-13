package com.destructo.sushi.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.destructo.sushi.databinding.ListItemPromotionalHomeBinding
import com.destructo.sushi.listener.MalUrlListener
import com.destructo.sushi.model.mal.promotion.PromotionalItem

class PromotionItemAdapter(private val malUrlListener: MalUrlListener) :
    ListAdapter<PromotionalItem , PromotionItemAdapter.ViewHolder>(PromotionItemDiffUtil()) {

    class ViewHolder private constructor(val binding: ListItemPromotionalHomeBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(promotionItem: PromotionalItem, listener: MalUrlListener) {
            binding.promotionItem = promotionItem
            binding.listener = listener
            binding.executePendingBindings()

        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ListItemPromotionalHomeBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(
                    binding
                )
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val animeEntity = getItem(position)
        holder.bind(animeEntity, malUrlListener)
    }

}

class PromotionItemDiffUtil : DiffUtil.ItemCallback<PromotionalItem>() {
    override fun areItemsTheSame(oldItem: PromotionalItem, newItem: PromotionalItem): Boolean {
        return oldItem.title == newItem.title
    }

    override fun areContentsTheSame(oldItem: PromotionalItem, newItem: PromotionalItem): Boolean {
        return oldItem.title == newItem.title
    }

}