package com.destructo.sushi.util

import android.content.Context
import android.net.Uri
import android.text.Editable
import android.util.TypedValue
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.annotation.AttrRes
import androidx.annotation.ColorInt
import androidx.browser.customtabs.CustomTabsIntent
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
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

fun Context.openUrl(url: String){
    val builder = CustomTabsIntent.Builder()
    val customTabIntent = builder.build()
    customTabIntent.launchUrl(this, Uri.parse(url))
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
            imm.showSoftInput(this, InputMethodManager.SHOW_IMPLICIT)
        }
    }
}

fun View.hideSoftKeyboard() {
    post {
        if (this.requestFocus()) {
            val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.showSoftInput(this, InputMethodManager.HIDE_IMPLICIT_ONLY)
        }
    }
}


fun <T> Fragment.getNavigationResultLiveData(key: String = "result") =
    findNavController().currentBackStackEntry?.savedStateHandle?.getLiveData<T>(key)

fun <T> Fragment.setNavigationResult(result: T, key: String = "result") {
    findNavController().previousBackStackEntry?.savedStateHandle?.set(key, result)
}

