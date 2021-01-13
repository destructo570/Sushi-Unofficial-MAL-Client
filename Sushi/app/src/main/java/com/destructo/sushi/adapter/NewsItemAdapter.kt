package com.destructo.sushi.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.destructo.sushi.databinding.ListItemNewsCardHomeBinding
import com.destructo.sushi.listener.MalUrlListener
import com.destructo.sushi.model.mal.news.NewsItem

class NewsItemAdapter(private val malUrlListener: MalUrlListener) :
    ListAdapter<NewsItem, NewsItemAdapter.ViewHolder>(NewsItemDiffUtil()) {

    class ViewHolder private constructor(val binding: ListItemNewsCardHomeBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(newsItem: NewsItem, listener: MalUrlListener) {
            binding.newsItem = newsItem
            binding.listener = listener
            binding.executePendingBindings()

        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ListItemNewsCardHomeBinding.inflate(layoutInflater, parent, false)
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

class NewsItemDiffUtil : DiffUtil.ItemCallback<NewsItem>() {
    override fun areItemsTheSame(oldItem: NewsItem, newItem: NewsItem): Boolean {
        return oldItem.title == newItem.title
    }

    override fun areContentsTheSame(oldItem: NewsItem, newItem: NewsItem): Boolean {
        return oldItem.title == newItem.title
    }

}