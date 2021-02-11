package com.destructo.sushi.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.destructo.sushi.databinding.ListItemMangaBinding
import com.destructo.sushi.listener.ListEndListener
import com.destructo.sushi.listener.MalIdListener
import com.destructo.sushi.model.mal.mangaList.MangaListData

class MangaListAdapter(
    private val malIdListener: MalIdListener
):
    ListAdapter<MangaListData, MangaListAdapter.ViewHolder>(MangaListDiffUtil()) {

    private var listEndListener: ListEndListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val mangaEntity = getItem(position)
        holder.bind(mangaEntity,malIdListener)
        if (position == currentList.size - 2) run {
            listEndListener?.onEndReached(position)
        }
    }

    fun setListEndListener(listEndListener: ListEndListener){
        this.listEndListener = listEndListener
    }

    class ViewHolder(val binding: ListItemMangaBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(mangaEntity: MangaListData, malIdListener: MalIdListener){
            binding.mangaEntity = mangaEntity.manga
            binding.mangaListener = malIdListener
            binding.executePendingBindings()
        }
        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ListItemMangaBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(
                    binding
                )
            }
        }
    }


}

class MangaListDiffUtil: DiffUtil.ItemCallback<MangaListData>(){
    override fun areItemsTheSame(oldItem: MangaListData, newItem: MangaListData): Boolean {
        return oldItem.manga.id == newItem.manga.id

    }

    override fun areContentsTheSame(oldItem: MangaListData, newItem: MangaListData): Boolean {
        return oldItem.manga.id == newItem.manga.id
                && oldItem.manga.title == newItem.manga.title
    }

}