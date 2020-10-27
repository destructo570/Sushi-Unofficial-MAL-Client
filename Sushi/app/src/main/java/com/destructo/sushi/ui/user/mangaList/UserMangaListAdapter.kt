package com.destructo.sushi.ui.user.mangaList

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.destructo.sushi.databinding.ListItemUserMangaBinding
import com.destructo.sushi.model.mal.userMangaList.UserMangaData

class UserMangaListAdapter:
    ListAdapter<UserMangaData, UserMangaListAdapter.ViewHolder>(UserMangaDiffUtil()) {


    class ViewHolder private constructor(val binding: ListItemUserMangaBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(mangaEntity: UserMangaData) {
            binding.mangaEntity = mangaEntity.manga
            binding.mangaListStatus = mangaEntity.mangaListStatus
            binding.executePendingBindings()

        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ListItemUserMangaBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val mangaEntity = getItem(position)
        holder.bind(mangaEntity)
    }


}

class UserMangaDiffUtil : DiffUtil.ItemCallback<UserMangaData>() {
    override fun areItemsTheSame(oldItem: UserMangaData, newItem: UserMangaData): Boolean {
        return oldItem.manga?.id == newItem.manga?.id
    }

    override fun areContentsTheSame(oldItem: UserMangaData, newItem: UserMangaData): Boolean {
        return oldItem.manga?.id == newItem.manga?.id
                && oldItem.manga?.title == newItem.manga?.title
    }

}