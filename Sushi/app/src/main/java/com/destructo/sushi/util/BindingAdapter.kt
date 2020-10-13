package com.destructo.sushi.util

import android.widget.ImageView
import android.widget.TextView
import androidx.core.net.toUri
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.destructo.sushi.R


/**
 * Loading image into image view with Glide.
 */
@BindingAdapter("imageUrl")
fun bindImage(imgView: ImageView, imgUrl: String?) {
    imgUrl?.let {
        val imgUri = imgUrl.toUri().buildUpon().scheme("https").build()
        Glide.with(imgView.context)
            .load(imgUri)
            .apply(
                RequestOptions()
                .placeholder(R.drawable.test_img))
            .into(imgView)
    }
}

@BindingAdapter("animeTitle")
fun TextView.setAnimeTitle(data:String?){
    data?.let{
        text = formatTitleText(data)
    }
}


@BindingAdapter("animeScore")
fun TextView.setAnimeScore(data:String?){

    if(data == null || data == " " || data.isEmpty() ){
        text = "N/A"
    }else{
        text = data
    }
}

private fun formatTitleText(text:String): String{
    var title = if(text.length > 12) text.take( 12) else return text

    if(title.takeLast(1) == " "){
       title = "${title.take(11)}..."
    }else{
        title = "$title..."
    }
    return title
}