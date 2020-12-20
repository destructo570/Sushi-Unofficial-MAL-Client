package com.destructo.sushi.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.destructo.sushi.databinding.ListItemStaffLargeBinding
import com.destructo.sushi.listener.AnimeStaffListener
import com.destructo.sushi.model.jikan.anime.support.Staff


class AllAnimeStaffAdapter(private val animeStaffListener: AnimeStaffListener) :
    ListAdapter<Staff, AllAnimeStaffAdapter.ViewHolder>(AnimeStaffDiffUtil()) {

    class ViewHolder private constructor(val binding: ListItemStaffLargeBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(staff: Staff, listener: AnimeStaffListener) {
            binding.staff = staff
            binding.staffListener = listener
            binding.executePendingBindings()

        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ListItemStaffLargeBinding.inflate(layoutInflater, parent, false)
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
        holder.bind(animeEntity, animeStaffListener)
    }


}
