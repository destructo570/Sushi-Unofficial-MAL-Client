package com.destructo.sushi.ui.anime.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.destructo.sushi.databinding.ListItemReviewBinding
import com.destructo.sushi.databinding.ListItemVideoBinding
import com.destructo.sushi.model.jikan.anime.support.Promo
import com.destructo.sushi.model.jikan.common.Review

class AnimeReviewListAdapter(private val animeReviewListener: AnimeReviewListener) :
    ListAdapter<Review, AnimeReviewListAdapter.ViewHolder>(AnimeReviewDiffUtil()) {

    class ViewHolder private constructor(val binding: ListItemReviewBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(review: Review, listener: AnimeReviewListener) {
            binding.review = review
            binding.reviewListener = listener
            binding.executePendingBindings()

        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ListItemReviewBinding.inflate(layoutInflater, parent, false)
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
        holder.bind(animeEntity, animeReviewListener)
    }


}

class AnimeReviewDiffUtil : DiffUtil.ItemCallback<Review>() {
    override fun areItemsTheSame(oldItem: Review, newItem: Review): Boolean {
        return oldItem.malId == newItem.malId
    }

    override fun areContentsTheSame(oldItem: Review, newItem: Review): Boolean {
        return oldItem.malId == newItem.malId && oldItem.url == newItem.url
    }

}