package com.destructo.sushi.ui.user.mangaList

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.destructo.sushi.databinding.ListItemUserMangaBinding
import com.destructo.sushi.model.mal.userMangaList.UserMangaData
import com.destructo.sushi.ui.ListEndListener
import com.destructo.sushi.ui.anime.listener.AnimeIdListener
import com.destructo.sushi.ui.manga.listener.MangaIdListener
import com.destructo.sushi.ui.user.animeList.AddEpisodeListener

class UserMangaListAdapter(
    private val addChapterListener: AddChapterListener,
    private val mangaIdListener: MangaIdListener
):
    ListAdapter<UserMangaData, UserMangaListAdapter.ViewHolder>(UserMangaDiffUtil()) {

    private var listEndListener: ListEndListener? = null

    class ViewHolder private constructor(val binding: ListItemUserMangaBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(mangaEntity: UserMangaData,
                 chapterListener: AddChapterListener,
                 mangaIdListener: MangaIdListener
        ) {
            binding.mangaEntity = mangaEntity.manga
            binding.mangaListStatus = mangaEntity.mangaListStatus
            binding.chapterListener = chapterListener
            binding.mangaIdListener = mangaIdListener
            binding.executePendingBindings()

        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ListItemUserMangaBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val mangaEntity = getItem(position)
        holder.bind(mangaEntity,addChapterListener,mangaIdListener)
        if (position == currentList.size - 1) run {
            listEndListener?.onEndReached(position)
        }
    }

    fun setListEndListener(listEndListener: ListEndListener){
        this.listEndListener = listEndListener
    }


}

class UserMangaDiffUtil : DiffUtil.ItemCallback<UserMangaData>() {
    override fun areItemsTheSame(oldItem: UserMangaData, newItem: UserMangaData): Boolean {
        return oldItem.manga?.id == newItem.manga?.id
    }

    override fun areContentsTheSame(oldItem: UserMangaData, newItem: UserMangaData): Boolean {
        return oldItem.manga == newItem.manga
    }

}