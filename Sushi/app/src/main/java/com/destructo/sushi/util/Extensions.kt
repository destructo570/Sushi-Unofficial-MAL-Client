package com.destructo.sushi.util

import android.content.Context
import android.text.Editable
import androidx.annotation.DimenRes
import java.util.*


fun String.toEditable(): Editable =  Editable.Factory.getInstance().newEditable(this)

fun String.toTitleCase():String{
    val str = this.replace("_", " ", true)
    val words = str.split(" ")
    var finalString = ""
    words.forEach {
        finalString += it.capitalize(Locale.ROOT) + " "
    }
    return finalString
}

