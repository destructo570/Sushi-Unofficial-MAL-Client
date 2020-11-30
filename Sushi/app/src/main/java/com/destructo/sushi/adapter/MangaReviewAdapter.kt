package com.destructo.sushi.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.destructo.sushi.databinding.ListItemReviewMangaBinding
import com.destructo.sushi.listener.MangaReviewListener
import com.destructo.sushi.model.jikan.manga.ReviewEntity


class MangaReviewAdapter(private val animeReviewListener: MangaReviewListener) :
    ListAdapter<ReviewEntity, MangaReviewAdapter.ViewHolder>(MangaReviewDiffUtil()) {

    class ViewHolder private constructor(val binding: ListItemReviewMangaBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(review: ReviewEntity, listener: MangaReviewListener) {
            binding.review = review
            binding.reviewListener = listener
            binding.executePendingBindings()

        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ListItemReviewMangaBinding.inflate(layoutInflater, parent, false)
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

class MangaReviewDiffUtil : DiffUtil.ItemCallback<ReviewEntity>() {
    override fun areItemsTheSame(oldItem: ReviewEntity, newItem: ReviewEntity): Boolean {
        return oldItem.malId == newItem.malId
    }

    override fun areContentsTheSame(oldItem: ReviewEntity, newItem: ReviewEntity): Boolean {
        return oldItem.malId == newItem.malId && oldItem.url == newItem.url
    }

}