package com.destructo.sushi.ui.anime.characterDetails

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.destructo.sushi.databinding.ListItemVoiceActorBinding
import com.destructo.sushi.listener.MalIdListener
import com.destructo.sushi.model.jikan.anime.support.VoiceActor

class VoiceActorAdapter (
    val malIdListener: MalIdListener
        ):
    ListAdapter<VoiceActor, VoiceActorAdapter.ViewHolder>(VoiceActorDiffUtil()) {

    class ViewHolder private constructor(val binding: ListItemVoiceActorBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(voiceActor: VoiceActor, malIdListener: MalIdListener) {
            binding.voiceActor = voiceActor
            binding.listener = malIdListener
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ListItemVoiceActorBinding.inflate(layoutInflater, parent, false)
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

class VoiceActorDiffUtil : DiffUtil.ItemCallback<VoiceActor>() {
    override fun areItemsTheSame(oldItem: VoiceActor, newItem: VoiceActor): Boolean {
        return oldItem.malId == newItem.malId
    }

    override fun areContentsTheSame(oldItem: VoiceActor, newItem: VoiceActor): Boolean {
        return oldItem.malId == newItem.malId
                && oldItem.name == newItem.name
    }

}