package com.destructo.sushi.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.destructo.sushi.databinding.ListItemRecommBinding
import com.destructo.sushi.listener.MalIdListener
import com.destructo.sushi.model.mal.manga.Recommendation

class MangaRecommListAdapter(private val malIdListener: MalIdListener) :
    ListAdapter<Recommendation, MangaRecommListAdapter.ViewHolder>(MangaRecommDiffUtil()) {

    class ViewHolder private constructor(val binding: ListItemRecommBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(recomm: Recommendation) {
            binding.listItemTitle.text = recomm.manga?.title
            binding.listItemRecomms.text = recomm.numRecommendations.toString()
            binding.coverUrl = recomm.manga?.mainPicture?.medium
            binding.executePendingBindings()

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
        val mangaEntity = getItem(position)
        holder.bind(mangaEntity)
        holder.itemView.setOnClickListener{
            mangaEntity.manga?.id?.let {
                    malId -> malIdListener.onClick(malId)
            }
        }
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