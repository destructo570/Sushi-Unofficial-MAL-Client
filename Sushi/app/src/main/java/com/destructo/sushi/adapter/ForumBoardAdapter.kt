package com.destructo.sushi.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.destructo.sushi.databinding.ListItemForumBoardBinding
import com.destructo.sushi.listener.MalIdListener
import com.destructo.sushi.model.mal.forum.Board

class ForumBoardAdapter(private val malIdListener: MalIdListener) :
    ListAdapter<Board, ForumBoardAdapter.ViewHolder>(ForumBoardDiffUtil()) {

    class ViewHolder private constructor(val binding: ListItemForumBoardBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(board: Board, listener: MalIdListener) {
            binding.board = board
            binding.listener = listener
            binding.executePendingBindings()

        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ListItemForumBoardBinding.inflate(layoutInflater, parent, false)
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

class ForumBoardDiffUtil : DiffUtil.ItemCallback<Board>() {
    override fun areItemsTheSame(oldItem: Board, newItem: Board): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Board, newItem: Board): Boolean {
        return oldItem.title == newItem.title
                && oldItem.id == newItem.id
    }

}