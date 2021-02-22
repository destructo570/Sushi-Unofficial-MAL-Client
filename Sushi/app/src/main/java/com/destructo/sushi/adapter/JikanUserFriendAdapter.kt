package com.destructo.sushi.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.destructo.sushi.databinding.ListItemUserFriendsBinding
import com.destructo.sushi.listener.ListEndListener
import com.destructo.sushi.listener.MalUrlListener
import com.destructo.sushi.model.jikan.user.friends.Friend

class JikanUserFriendAdapter(private val malUrlListener: MalUrlListener) :
    ListAdapter<Friend, JikanUserFriendAdapter.ViewHolder>(FriendListDiffUtil()) {

    private var listEndListener: ListEndListener? = null

    class ViewHolder private constructor(val binding: ListItemUserFriendsBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(friend: Friend, listener: MalUrlListener) {
            binding.friend = friend
            binding.listener = listener
            binding.executePendingBindings()

        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ListItemUserFriendsBinding.inflate(layoutInflater, parent, false)
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
        val friend = getItem(position)
        holder.bind(friend, malUrlListener)
        if (position == currentList.size - 2) run {
            listEndListener?.onEndReached(position)
        }
    }

    fun setListEndListener(listEndListener: ListEndListener){
        this.listEndListener = listEndListener
    }

}

class FriendListDiffUtil : DiffUtil.ItemCallback<Friend>() {
    override fun areItemsTheSame(oldItem: Friend, newItem: Friend): Boolean {
        return oldItem.username == newItem.username && oldItem.url == newItem.url
    }

    override fun areContentsTheSame(oldItem: Friend, newItem: Friend): Boolean {
        return oldItem.username == newItem.username && oldItem.url == newItem.url
    }

}