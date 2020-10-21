package com.destructo.sushi.ui.anime.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.destructo.sushi.databinding.ListItemRecommBinding
import com.destructo.sushi.databinding.ListItemRelatedBinding
import com.destructo.sushi.model.mal.anime.Recommendation
import com.destructo.sushi.model.mal.anime.RelatedAnime

class AnimeRelatedListAdapter(private val animeDetailListener: AnimeRecommListener) :
    ListAdapter<RelatedAnime, AnimeRelatedListAdapter.ViewHolder>(AnimeRelatedAnimeDiffUtil()) {

    class ViewHolder private constructor(val binding: ListItemRelatedBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(relatedAnime: RelatedAnime, listener: AnimeRecommListener) {
            binding.relatedAnime = relatedAnime
            binding.listener = listener
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ListItemRelatedBinding.inflate(layoutInflater, parent, false)
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

class AnimeRelatedAnimeDiffUtil : DiffUtil.ItemCallback<RelatedAnime>() {
    override fun areItemsTheSame(oldItem: RelatedAnime, newItem: RelatedAnime): Boolean {
        return oldItem.anime?.id == newItem.anime?.id
    }

    override fun areContentsTheSame(oldItem: RelatedAnime, newItem: RelatedAnime): Boolean {
        return oldItem.anime?.title == newItem.anime?.title
                && oldItem.anime?.id == newItem.anime?.id
    }

}