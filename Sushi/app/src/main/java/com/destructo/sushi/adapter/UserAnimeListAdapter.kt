package com.destructo.sushi.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.destructo.sushi.databinding.ListItemUserAnimeBinding
import com.destructo.sushi.listener.AddEpisodeListener
import com.destructo.sushi.listener.ListEndListener
import com.destructo.sushi.listener.MalIdListener
import com.destructo.sushi.model.mal.userAnimeList.UserAnimeData

class UserAnimeListAdapter(
    private val addEpisodeListener: AddEpisodeListener,
    private val malIdListener: MalIdListener,
    private val isWatchingList: Boolean
    ) :
    ListAdapter<UserAnimeData, UserAnimeListAdapter.ViewHolder>(UserAnimeDiffUtil()) {

    private var listEndListener: ListEndListener? = null

    class ViewHolder private constructor(val binding: ListItemUserAnimeBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(animeEntity: UserAnimeData,
                 addEpisodeListener: AddEpisodeListener,
                 malIdListener: MalIdListener,
                 isWatchingList: Boolean
                 ) {
            binding.animeEntity = animeEntity.anime
            binding.animeListStatus = animeEntity.animeListStatus
            binding.episodeListener = addEpisodeListener
            binding.animeIdListener = malIdListener
            if(!isWatchingList){binding.addEpisodeButton.visibility = View.GONE}
            binding.executePendingBindings()

        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ListItemUserAnimeBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val animeSubEntity = getItem(position)
        holder.bind(animeSubEntity, addEpisodeListener, malIdListener, isWatchingList)
        if (position == currentList.size - 1) run {
            listEndListener?.onEndReached(position)
        }
    }

    fun setListEndListener(listEndListener: ListEndListener){
        this.listEndListener = listEndListener
    }

}

class UserAnimeDiffUtil : DiffUtil.ItemCallback<UserAnimeData>() {
    override fun areItemsTheSame(oldItem: UserAnimeData, newItem: UserAnimeData): Boolean {
        return oldItem.anime?.id == newItem.anime?.id
    }

    override fun areContentsTheSame(oldItem: UserAnimeData, newItem: UserAnimeData): Boolean {
        return oldItem.anime == newItem.anime

    }

}