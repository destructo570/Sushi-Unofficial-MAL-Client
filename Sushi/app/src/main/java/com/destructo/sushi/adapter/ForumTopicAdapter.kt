package com.destructo.sushi.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.destructo.sushi.databinding.ListItemForumTopicBinding
import com.destructo.sushi.listener.MalIdListener
import com.destructo.sushi.model.mal.forum.TopicData

class ForumTopicAdapter(private val malIdListener: MalIdListener) :
    ListAdapter<TopicData, ForumTopicAdapter.ViewHolder>(ForumTopicDiffUtil()) {

    class ViewHolder private constructor(val binding: ListItemForumTopicBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(topic: TopicData, listener: MalIdListener) {
            binding.topic = topic
            binding.listener = listener
            binding.executePendingBindings()

        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ListItemForumTopicBinding.inflate(layoutInflater, parent, false)
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
        holder.bind(animeEntity, malIdListener)
    }


}

class ForumTopicDiffUtil : DiffUtil.ItemCallback<TopicData>() {
    override fun areItemsTheSame(oldItem: TopicData, newItem: TopicData): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: TopicData, newItem: TopicData): Boolean {
        return oldItem.title == newItem.title
                && oldItem.id == newItem.id
    }

}