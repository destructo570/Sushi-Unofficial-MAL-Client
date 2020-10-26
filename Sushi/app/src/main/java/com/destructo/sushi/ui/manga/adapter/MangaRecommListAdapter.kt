package com.destructo.sushi.ui.manga.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.destructo.sushi.databinding.ListItemRecommBinding
import com.destructo.sushi.databinding.ListItemRecommMangaBinding
import com.destructo.sushi.model.mal.manga.Recommendation

class MangaRecommListAdapter(private val mangaDetailListener: MangaRecommListener) :
    ListAdapter<Recommendation, MangaRecommListAdapter.ViewHolder>(MangaRecommDiffUtil()) {

    class ViewHolder private constructor(val binding: ListItemRecommMangaBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(recomm: Recommendation, listener: MangaRecommListener) {
            binding.recommendation = recomm
            binding.listener = listener
            binding.executePendingBindings()

        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ListItemRecommMangaBinding.inflate(layoutInflater, parent, false)
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
        holder.bind(animeEntity, mangaDetailListener)
    }


}

class MangaRecommDiffUtil : DiffUtil.ItemCallback<Recommendation>() {
    override fun areItemsTheSame(oldItem: Recommendation, newItem: Recommendation): Boolean {
        return oldItem.manga?.id == newItem.manga?.id
    }

    override fun areContentsTheSame(oldItem: Recommendation, newItem: Recommendation): Boolean {
        return oldItem.manga?.title == newItem.manga?.title
                && oldItem.manga?.id == newItem.manga?.id
    }

}