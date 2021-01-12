package com.destructo.sushi.adapter.pagerAdapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.destructo.sushi.databinding.ListItemForumCategoryBinding
import com.destructo.sushi.model.mal.forum.Category

class ForumCategoryAdapter(
    private val categoryList:List<Category?>
) :
    RecyclerView.Adapter<ForumCategoryAdapter.ViewHolder>() {

    class ViewHolder private constructor(val binding: ListItemForumCategoryBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(category: Category) {
            binding.category = category
            binding.executePendingBindings()

        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ListItemForumCategoryBinding.inflate(layoutInflater, parent, false)
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
        val category = categoryList[position]
        if (category != null) {
            holder.bind(category)
        }
    }

    override fun getItemCount(): Int {
        return categoryList.size
    }


}

