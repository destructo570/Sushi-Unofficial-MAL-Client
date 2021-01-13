package com.destructo.sushi.listener

class MalUrlListener(val clickListener: (malUrl: String?) -> Unit) {
    fun onClick(url: String) = clickListener(url)
}