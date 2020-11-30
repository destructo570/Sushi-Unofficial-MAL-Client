package com.destructo.sushi.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.destructo.sushi.databinding.ListItemStaffBinding
import com.destructo.sushi.model.jikan.anime.support.Staff
import com.destructo.sushi.listener.AnimeStaffListener


class AnimeStaffListAdapter(private val animeStaffListener: AnimeStaffListener) :
    ListAdapter<Staff, AnimeStaffListAdapter.ViewHolder>(AnimeStaffDiffUtil()) {

    class ViewHolder private constructor(val binding: ListItemStaffBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(staff: Staff, listener: AnimeStaffListener) {
            binding.staff = staff
            binding.staffListener = listener
            binding.executePendingBindings()

        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ListItemStaffBinding.inflate(layoutInflater, parent, false)
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

class AnimeStaffDiffUtil : DiffUtil.ItemCallback<Staff>() {
    override fun areItemsTheSame(oldItem: Staff, newItem: Staff): Boolean {
        return oldItem.malId == newItem.malId
    }

    override fun areContentsTheSame(oldItem: Staff, newItem: Staff): Boolean {
        return oldItem.malId == newItem.malId
                && oldItem.name == newItem.name
    }

}