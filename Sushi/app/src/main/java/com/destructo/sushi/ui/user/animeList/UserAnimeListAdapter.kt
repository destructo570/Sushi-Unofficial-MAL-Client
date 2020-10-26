package com.destructo.sushi.ui.user.animeList

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.destructo.sushi.databinding.ListItemAnimeBinding
import com.destructo.sushi.databinding.ListItemScheduleAnimeBinding
import com.destructo.sushi.databinding.ListItemUserAnimeBinding
import com.destructo.sushi.model.jikan.season.AnimeSubEntity
import com.destructo.sushi.model.mal.anime.Anime
import com.destructo.sushi.model.mal.userAnimeList.UserAnimeData
import com.destructo.sushi.ui.animeSchedule.ScheduleAdapter

class UserAnimeListAdapter :
    ListAdapter<UserAnimeData, UserAnimeListAdapter.ViewHolder>(UserAnimeDiffUtil()) {


    class ViewHolder private constructor(val binding: ListItemUserAnimeBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(animeEntity: UserAnimeData) {
            binding.animeEntity = animeEntity.anime
            binding.executePendingBindings()

        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ListItemUserAnimeBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val animeSubEntity = getItem(position)
        holder.bind(animeSubEntity)
    }


}

class UserAnimeDiffUtil : DiffUtil.ItemCallback<UserAnimeData>() {
    override fun areItemsTheSame(oldItem: UserAnimeData, newItem: UserAnimeData): Boolean {
        return oldItem.anime?.id == newItem.anime?.id
    }

    override fun areContentsTheSame(oldItem: UserAnimeData, newItem: UserAnimeData): Boolean {
        return oldItem.anime?.id == newItem.anime?.id
                && oldItem.anime?.title == newItem.anime?.title
    }

}