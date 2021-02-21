package com.destructo.sushi.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.destructo.sushi.databinding.ListItemProfileUserAnimeBinding
import com.destructo.sushi.listener.ListEndListener
import com.destructo.sushi.listener.MalIdListener
import com.destructo.sushi.model.jikan.user.animeList.Anime

class JikanUserAnimeListAdapter(private val malIdListener: MalIdListener) :
    ListAdapter<Anime, JikanUserAnimeListAdapter.ViewHolder>(ProfileAnimeListDiffUtil()) {

    private var listEndListener: ListEndListener? = null

    class ViewHolder private constructor(val binding: ListItemProfileUserAnimeBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(anime: Anime, listener: MalIdListener) {
            binding.anime = anime
            binding.listener = listener
            binding.executePendingBindings()

        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ListItemProfileUserAnimeBinding.inflate(layoutInflater, parent, false)
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

class ProfileAnimeListDiffUtil : DiffUtil.ItemCallback<Anime>() {
    override fun areItemsTheSame(oldItem: Anime, newItem: Anime): Boolean {
        return oldItem.malId == newItem.malId
    }

    override fun areContentsTheSame(oldItem: Anime, newItem: Anime): Boolean {
        return oldItem.malId == newItem.malId
    }

}