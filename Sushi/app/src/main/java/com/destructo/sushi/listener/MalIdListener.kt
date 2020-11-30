package com.destructo.sushi.listener

class MalIdListener(val clickListener: (animeMalId: Int?) -> Unit) {
    fun onClick(id: Int) = clickListener(id)
}