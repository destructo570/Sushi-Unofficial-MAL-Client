package com.destructo.sushi.ui.anime.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.destructo.sushi.databinding.ListItemAnimeBinding
import com.destructo.sushi.databinding.ListItemRecommBinding
import com.destructo.sushi.model.mal.anime.AnimeBasic
import com.destructo.sushi.model.mal.anime.Recommendation
import com.destructo.sushi.model.mal.animeRanking.AnimeRankingData
import com.destructo.sushi.ui.anime.animeDetails.AnimeDetailListener

class AnimeRecommListAdapter(private val animeDetailListener: AnimeRecommListener) :
    ListAdapter<Recommendation, AnimeRecommListAdapter.ViewHolder>(AnimeRecommDiffUtil()) {

    class ViewHolder private constructor(val binding: ListItemRecommBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(recomm: Recommendation, listener: AnimeRecommListener) {
            binding.recommendation = recomm
            binding.listener = listener
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ListItemRecommBinding.inflate(layoutInflater, parent, false)
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
        holder.bind(animeEntity, animeDetailListener)
    }


}

class AnimeRecommDiffUtil : DiffUtil.ItemCallback<Recommendation>() {
    override fun areItemsTheSame(oldItem: Recommendation, newItem: Recommendation): Boolean {
        return oldItem.anime?.id == newItem.anime?.id
    }

    override fun areContentsTheSame(oldItem: Recommendation, newItem: Recommendation): Boolean {
        return oldItem.anime?.title == newItem.anime?.title
                && oldItem.anime?.id == newItem.anime?.id
    }

}