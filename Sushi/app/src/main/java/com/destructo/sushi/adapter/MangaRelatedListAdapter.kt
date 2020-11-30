package com.destructo.sushi.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.destructo.sushi.databinding.ListItemRelatedBinding
import com.destructo.sushi.listener.MalIdListener
import com.destructo.sushi.model.mal.manga.RelatedManga

class MangaRelatedListAdapter(private val malIdListener: MalIdListener) :
    ListAdapter<RelatedManga, MangaRelatedListAdapter.ViewHolder>(MangaRelatedAnimeDiffUtil()) {


    class ViewHolder private constructor(val binding: ListItemRelatedBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(relatedManga: RelatedManga) {
            binding.relationType.text = relatedManga.relationTypeFormatted
            binding.itemTitle.text = relatedManga.manga?.title
            binding.coverUrl = relatedManga.manga?.mainPicture?.medium
            binding.executePendingBindings()

        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ListItemRelatedBinding.inflate(layoutInflater, parent, false)
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
        holder.itemView.setOnClickListener {
            mangaEntity.manga?.id?.let {
                    malId -> malIdListener.onClick(malId)
            }
        }
    }


}

class MangaRelatedAnimeDiffUtil : DiffUtil.ItemCallback<RelatedManga>() {
    override fun areItemsTheSame(oldItem: RelatedManga, newItem: RelatedManga): Boolean {
        return oldItem.manga?.id == newItem.manga?.id
    }

    override fun areContentsTheSame(oldItem: RelatedManga, newItem: RelatedManga): Boolean {
        return oldItem.manga?.title == newItem.manga?.title
                && oldItem.manga?.id == newItem.manga?.id
    }

}