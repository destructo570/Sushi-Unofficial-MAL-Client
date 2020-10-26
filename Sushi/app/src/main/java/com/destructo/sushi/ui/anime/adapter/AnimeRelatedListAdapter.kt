package com.destructo.sushi.ui.anime.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.destructo.sushi.databinding.ListItemRelatedBinding
import com.destructo.sushi.model.mal.anime.RelatedAnime
import com.destructo.sushi.ui.anime.listener.AnimeIdListener

class AnimeRelatedListAdapter(private val animeIdListener: AnimeIdListener) :
    ListAdapter<RelatedAnime, AnimeRelatedListAdapter.ViewHolder>(AnimeRelatedAnimeDiffUtil()) {

    class ViewHolder private constructor(val binding: ListItemRelatedBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(relatedAnime: RelatedAnime) {
            binding.relationType.text = relatedAnime.relationTypeFormatted
            binding.itemTitle.text = relatedAnime.anime?.title
            binding.coverUrl = relatedAnime.anime?.mainPicture?.medium
            binding.executePendingBindings()
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
        holder.bind(animeEntity)
        holder.itemView.setOnClickListener{
            animeEntity.anime?.id?.let {
                    malId -> animeIdListener.onClick(malId)
            }
        }
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