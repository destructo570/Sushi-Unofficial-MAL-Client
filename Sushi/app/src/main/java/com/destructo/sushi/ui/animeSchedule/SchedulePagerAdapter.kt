package com.destructo.sushi.ui.animeSchedule

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.destructo.sushi.databinding.ItemAnimeScheduleBinding
import com.destructo.sushi.model.jikan.season.AnimeSubEntity

class SchedulePagerAdapter:
    ListAdapter<List<AnimeSubEntity?>?, SchedulePagerAdapter.ViewHolder>(AnimeScheduleDiffUtil()){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class ViewHolder(val binding: ItemAnimeScheduleBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(animeList: List<AnimeSubEntity?>?){
            binding.listOfAnime = animeList
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemAnimeScheduleBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(
                    binding
                )
            }
        }
    }
}


class AnimeScheduleDiffUtil :DiffUtil.ItemCallback<List<AnimeSubEntity?>?>(){
    override fun areItemsTheSame(
        oldItem: List<AnimeSubEntity?>,
        newItem: List<AnimeSubEntity?>
    ): Boolean {
       return oldItem[0]?.malId == newItem[0]?.malId
    }

    override fun areContentsTheSame(
        oldItem: List<AnimeSubEntity?>,
        newItem: List<AnimeSubEntity?>
    ): Boolean {
        return oldItem[0]?.malId == newItem[0]?.malId
    }


}