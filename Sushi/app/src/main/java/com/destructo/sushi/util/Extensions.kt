package com.destructo.sushi.util

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.text.Editable
import android.util.TypedValue
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.annotation.AttrRes
import androidx.annotation.ColorInt
import androidx.browser.customtabs.CustomTabsIntent
import com.destructo.sushi.R
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

fun Context.makeShortToast(message: String){
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

fun Context.makeLongToast(message: String){
    Toast.makeText(this, message, Toast.LENGTH_LONG).show()
}

fun Context.copyToClipboard(content: String){
    val clipboard =
        this.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    val clipData = ClipData.newPlainText("text", content)
    clipboard.setPrimaryClip(clipData)
}

fun Context.shareText(data: String){
    val intent = Intent(Intent.ACTION_SEND)
    intent.type = "text/plain"
    intent.putExtra(Intent.EXTRA_TEXT, data)
    startActivity(Intent.createChooser(intent, getString(R.string.share_using)))
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

fun View.show(){
    visibility = View.VISIBLE
}

fun View.hide(){
    visibility = View.GONE
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

