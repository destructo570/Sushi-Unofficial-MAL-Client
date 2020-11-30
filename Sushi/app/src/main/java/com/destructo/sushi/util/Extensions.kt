package com.destructo.sushi.util

import android.content.Context
import android.text.Editable
import android.util.TypedValue
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.annotation.AttrRes
import androidx.annotation.ColorInt
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

@ColorInt
fun Context.getColorFromAttr(
    @AttrRes attrColor: Int,
    typedValue: TypedValue = TypedValue(),
    resolveRefs: Boolean = true
): Int {
    theme.resolveAttribute(attrColor, typedValue, resolveRefs)
    return typedValue.data
}

fun View.showSoftKeyboard() {
    post {
        if (this.requestFocus()) {
            val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm?.showSoftInput(this, InputMethodManager.SHOW_IMPLICIT)
        }
    }
}

fun View.hideSoftKeyboard() {
    post {
        if (this.requestFocus()) {
            val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm?.showSoftInput(this, InputMethodManager.HIDE_IMPLICIT_ONLY)
        }
    }
}

