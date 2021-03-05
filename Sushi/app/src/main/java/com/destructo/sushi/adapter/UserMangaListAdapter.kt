package com.destructo.sushi.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.destructo.sushi.databinding.ListItemUserMangaBinding
import com.destructo.sushi.listener.AddChapterListenerUA
import com.destructo.sushi.listener.ListEndListener
import com.destructo.sushi.listener.MalIdListener
import com.destructo.sushi.model.database.UserMangaEntity

class UserMangaListAdapter(
    private val addChapterListener: AddChapterListenerUA,
    private val malIdListener: MalIdListener,
    private val isReadingList: Boolean
):
    ListAdapter<UserMangaEntity, UserMangaListAdapter.ViewHolder>(UserMangaDiffUtil()) {

    private var listEndListener: ListEndListener? = null

    class ViewHolder private constructor(val binding: ListItemUserMangaBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(mangaEntity: UserMangaEntity,
                 chapterListener: AddChapterListenerUA,
                 malIdListener: MalIdListener,
                 isReadingList: Boolean
        ) {
            binding.mangaEntity = mangaEntity
            binding.chapterListener = chapterListener
            binding.mangaIdListener = malIdListener
            if(!isReadingList){binding.addEpisodeButton.visibility = View.GONE}
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
        holder.bind(mangaEntity, addChapterListener, malIdListener, isReadingList)
        if (position == currentList.size - 1) run {
            listEndListener?.onEndReached(position)
        }
    }

    fun setListEndListener(listEndListener: ListEndListener){
        this.listEndListener = listEndListener
    }


}

class UserMangaDiffUtil : DiffUtil.ItemCallback<UserMangaEntity>() {
    override fun areItemsTheSame(oldItem: UserMangaEntity, newItem: UserMangaEntity): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: UserMangaEntity, newItem: UserMangaEntity): Boolean {
        return oldItem == newItem
    }

}