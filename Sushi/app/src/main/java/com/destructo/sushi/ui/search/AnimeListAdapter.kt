package com.destructo.sushi.ui.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.destructo.sushi.databinding.ListItemAnimeBinding
import com.destructo.sushi.listener.ListEndListener
import com.destructo.sushi.listener.MalIdListener
import com.destructo.sushi.model.mal.animeList.AnimeListData

class AnimeListAdapter(private val malIdListener: MalIdListener) :
    ListAdapter<AnimeListData, AnimeListAdapter.ViewHolder>(AnimeListDiffUtil()) {

    private var listEndListener: ListEndListener? = null

    class ViewHolder private constructor(val binding: ListItemAnimeBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(animeEntity: AnimeListData, listener: MalIdListener) {
            binding.animeEntity = animeEntity.anime
            binding.animeListener = listener
            binding.executePendingBindings()

        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ListItemAnimeBinding.inflate(layoutInflater, parent, false)
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
        holder.bind(animeEntity, malIdListener)
        if (position == currentList.size - 2) run {
            listEndListener?.onEndReached(position)
        }
    }

    fun setListEndListener(listEndListener: ListEndListener){
        this.listEndListener = listEndListener
    }

}

class AnimeListDiffUtil : DiffUtil.ItemCallback<AnimeListData>() {
    override fun areItemsTheSame(oldItem: AnimeListData, newItem: AnimeListData): Boolean {
        return oldItem.anime.id == newItem.anime.id
    }

    override fun areContentsTheSame(oldItem: AnimeListData, newItem: AnimeListData): Boolean {
        return oldItem.anime == newItem.anime
    }

}