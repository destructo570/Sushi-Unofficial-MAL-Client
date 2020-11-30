package com.destructo.sushi.ui.animeSchedule

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.destructo.sushi.databinding.ListItemScheduleAnimeBinding
import com.destructo.sushi.model.jikan.season.AnimeSubEntity
import com.destructo.sushi.listener.MalIdListener

class ScheduleAdapter(val malIdListener: MalIdListener): ListAdapter<AnimeSubEntity, ScheduleAdapter.ViewHolder>(SeasonAnimeDiffUtil()) {


    class ViewHolder private constructor(val binding: ListItemScheduleAnimeBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(animeSubEntity: AnimeSubEntity, malIdListener: MalIdListener){
            binding.animeSubEntity = animeSubEntity
            binding.listener = malIdListener
            binding.executePendingBindings()

        }
        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ListItemScheduleAnimeBinding.inflate(layoutInflater, parent, false)
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
        val animeSubEntity = getItem(position)
        holder.bind(animeSubEntity, malIdListener)
    }


}

class SeasonAnimeDiffUtil: DiffUtil.ItemCallback<AnimeSubEntity>() {
    override fun areItemsTheSame(oldItem: AnimeSubEntity, newItem: AnimeSubEntity): Boolean {
        return oldItem.url == newItem.url
                && oldItem.title == newItem.title
    }

    override fun areContentsTheSame(oldItem: AnimeSubEntity, newItem: AnimeSubEntity): Boolean {
        return oldItem.url == newItem.url
                && oldItem.title == newItem.title
    }

}