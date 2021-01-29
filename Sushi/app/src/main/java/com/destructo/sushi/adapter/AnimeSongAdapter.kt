package com.destructo.sushi.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.destructo.sushi.databinding.ListItemAnimeSongBinding
import com.destructo.sushi.listener.MalUrlListener

class AnimeSongAdapter(private val malUrlListener: MalUrlListener) :
    ListAdapter<String, AnimeSongAdapter.ViewHolder>(AnimeSongsDiffUtil()) {

    class ViewHolder private constructor(val binding: ListItemAnimeSongBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(songTitle: String, listener: MalUrlListener) {
            binding.songTitle = songTitle
            binding.listener = listener
            binding.executePendingBindings()

        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ListItemAnimeSongBinding.inflate(layoutInflater, parent, false)
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

class AnimeSongsDiffUtil : DiffUtil.ItemCallback<String>() {
    override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
        return oldItem == newItem
    }

}