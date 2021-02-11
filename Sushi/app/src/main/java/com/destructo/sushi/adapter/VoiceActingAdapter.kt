package com.destructo.sushi.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.destructo.sushi.databinding.ListItemPersonVoiceActingBinding
import com.destructo.sushi.listener.MalIdListener
import com.destructo.sushi.model.jikan.person.VoiceActingRole

class VoiceActingAdapter (
    val animeListener: MalIdListener,
    val characterListener: MalIdListener
):
    ListAdapter<VoiceActingRole, VoiceActingAdapter.ViewHolder>(VoiceActingRoleDiffUtil()) {

    class ViewHolder private constructor(val binding: ListItemPersonVoiceActingBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(voiceActingRole: VoiceActingRole, animeListener: MalIdListener, characterListener: MalIdListener) {
            binding.voiceActing = voiceActingRole
            binding.animeListener = animeListener
            binding.characterListener = characterListener
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ListItemPersonVoiceActingBinding.inflate(layoutInflater, parent, false)
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
        holder.bind(person,animeListener,characterListener)
    }

}

class VoiceActingRoleDiffUtil : DiffUtil.ItemCallback<VoiceActingRole>() {
    override fun areItemsTheSame(oldItem: VoiceActingRole, newItem: VoiceActingRole): Boolean {
        return oldItem.anime?.malId == newItem.anime?.malId
    }

    override fun areContentsTheSame(oldItem: VoiceActingRole, newItem: VoiceActingRole): Boolean {
        return oldItem.anime == newItem.anime
                && oldItem.character == newItem.character
    }
}