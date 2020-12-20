package com.destructo.sushi.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.destructo.sushi.databinding.ListItemReviewMangaLargeBinding
import com.destructo.sushi.listener.MangaReviewListener
import com.destructo.sushi.model.jikan.manga.ReviewEntity


class AllMangaReviewAdapter(private val animeReviewListener: MangaReviewListener) :
    ListAdapter<ReviewEntity, AllMangaReviewAdapter.ViewHolder>(MangaReviewDiffUtil()) {

    class ViewHolder private constructor(val binding: ListItemReviewMangaLargeBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(review: ReviewEntity, listener: MangaReviewListener) {
            binding.review = review
            binding.reviewListener = listener
            binding.executePendingBindings()

        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ListItemReviewMangaLargeBinding.inflate(layoutInflater, parent, false)
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
