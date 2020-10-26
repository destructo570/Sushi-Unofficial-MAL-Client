package com.destructo.sushi.ui.manga.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.bumptech.glide.request.RequestOptions
import com.destructo.sushi.R
import com.destructo.sushi.databinding.ListItemRelatedBinding
import com.destructo.sushi.model.mal.anime.RelatedAnime
import com.destructo.sushi.model.mal.manga.RelatedManga
import com.destructo.sushi.ui.manga.listener.MangaIdListener
import javax.inject.Inject

class MangaRelatedListAdapter(private val mangaIdListener: MangaIdListener) :
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
                    malId -> mangaIdListener.onClick(malId)
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