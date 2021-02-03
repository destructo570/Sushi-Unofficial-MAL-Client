package com.destructo.sushi.util

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.destructo.sushi.FIRST_LIST_ITEM_SPACE_HEIGHT

class ListItemHorizontalDecor(private val spaceHeight: Int): RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        with(outRect){
            if (parent.getChildAdapterPosition(view)==0) left = FIRST_LIST_ITEM_SPACE_HEIGHT

            top = spaceHeight
            right = spaceHeight
            bottom = spaceHeight

        }
    }
}