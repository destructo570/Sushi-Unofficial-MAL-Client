package com.destructo.sushi.ui.anime.adapter

import com.destructo.sushi.model.jikan.common.Review
import com.destructo.sushi.model.jikan.anime.support.Staff

class AnimeStaffListener(val clickListener: (staffMalId: Int?) -> Unit) {
    fun onClick(staff: Staff) = clickListener(staff.malId)
}