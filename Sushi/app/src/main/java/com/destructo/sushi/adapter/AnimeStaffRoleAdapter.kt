package com.destructo.sushi.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.destructo.sushi.databinding.ListItemPersonAnimeStaffRoleBinding
import com.destructo.sushi.listener.MalIdListener
import com.destructo.sushi.model.jikan.person.AnimeStaffPosition


class AnimeStaffRoleAdapter (
    val malIdListener: MalIdListener
):
    ListAdapter<AnimeStaffPosition, AnimeStaffRoleAdapter.ViewHolder>(AnimeStaffPositionDiffUtil()) {

    class ViewHolder private constructor(val binding: ListItemPersonAnimeStaffRoleBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(AnimeStaffPosition: AnimeStaffPosition, malIdListener: MalIdListener) {
            binding.animeStaff = AnimeStaffPosition
            binding.listener = malIdListener
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ListItemPersonAnimeStaffRoleBinding.inflate(layoutInflater, parent, false)
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
        val person = getItem(position)
        holder.bind(person,malIdListener)
    }

}

class AnimeStaffPositionDiffUtil : DiffUtil.ItemCallback<AnimeStaffPosition>() {
    override fun areItemsTheSame(oldItem: AnimeStaffPosition, newItem: AnimeStaffPosition): Boolean {
        return oldItem.anime?.malId == newItem.anime?.malId
    }

    override fun areContentsTheSame(oldItem: AnimeStaffPosition, newItem: AnimeStaffPosition): Boolean {
        return oldItem.anime == newItem.anime
    }
}