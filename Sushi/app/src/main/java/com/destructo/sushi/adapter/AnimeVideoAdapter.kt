package com.destructo.sushi.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.destructo.sushi.databinding.ListItemVideoBinding
import com.destructo.sushi.model.jikan.anime.support.Promo
import com.destructo.sushi.listener.AnimePromoListener

class AnimeVideoAdapter(private val animePromoListener: AnimePromoListener) :
    ListAdapter<Promo, AnimeVideoAdapter.ViewHolder>(AnimeVideoDiffUtil()) {

    class ViewHolder private constructor(val binding: ListItemVideoBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(promoVideo: Promo, listener: AnimePromoListener) {
            binding.promoVideo = promoVideo
            binding.promoListener = listener
            binding.executePendingBindings()

        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ListItemVideoBinding.inflate(layoutInflater, parent, false)
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
        holder.bind(animeEntity, animePromoListener)
    }


}

class AnimeVideoDiffUtil : DiffUtil.ItemCallback<Promo>() {
    override fun areItemsTheSame(oldItem: Promo, newItem: Promo): Boolean {
        return oldItem.videoUrl == newItem.videoUrl
    }

    override fun areContentsTheSame(oldItem: Promo, newItem: Promo): Boolean {
        return oldItem.videoUrl == newItem.videoUrl
    }

}