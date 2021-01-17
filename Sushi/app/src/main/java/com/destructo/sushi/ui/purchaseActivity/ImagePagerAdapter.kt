package com.destructo.sushi.ui.purchaseActivity

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.destructo.sushi.R
import kotlinx.android.synthetic.main.list_item_purchase_promo.view.*

class ImagePagerAdapter(
    private val imageList: List<Int>
) :RecyclerView.Adapter<ImagePagerAdapter.ViewPagerViewHolder>(){
    inner class ViewPagerViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewPagerViewHolder {
        val view  = LayoutInflater.from(parent.context).inflate(R.layout.list_item_purchase_promo, parent,false)
        return ViewPagerViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewPagerViewHolder, position: Int) {
        val currentImage = imageList[position]
        holder.itemView.promo_image.setImageResource(currentImage)
    }

    override fun getItemCount(): Int {
        return imageList.size
    }
}