package com.destructo.sushi.listener

import com.destructo.sushi.model.jikan.anime.support.Staff

class AnimeStaffListener(val clickListener: (staffMalId: Staff?) -> Unit) {
    fun onClick(staff: Staff) = clickListener(staff)
}