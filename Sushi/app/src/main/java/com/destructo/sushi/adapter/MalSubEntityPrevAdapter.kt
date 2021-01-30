package com.destructo.sushi.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.destructo.sushi.databinding.ListItemMalSubEntityPrevBinding
import com.destructo.sushi.listener.MalIdListener
import com.destructo.sushi.model.jikan.MALSubEntity

class MalSubEntityPrevAdapter(val malIdListener: MalIdListener) : ListAdapter<MALSubEntity,
        MalSubEntityPrevAdapter.ViewHolder>(MalSubEntityDiffUtil()) {


    class ViewHolder private constructor(val binding: ListItemMalSubEntityPrevBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(animeSubEntity: MALSubEntity, malIdListener: MalIdListener) {
            binding.malSubEntity = animeSubEntity
            binding.listener = malIdListener
            binding.executePendingBindings()

        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ListItemMalSubEntityPrevBinding.inflate(layoutInflater, parent, false)
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
        val animeSubEntity = getItem(position)
        holder.bind(animeSubEntity, malIdListener)
    }


}
