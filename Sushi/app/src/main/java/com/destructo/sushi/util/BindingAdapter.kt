package com.destructo.sushi.util

import android.text.format.DateUtils
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.net.toUri
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.destructo.sushi.R
import com.destructo.sushi.model.jikan.common.Review
import com.destructo.sushi.model.jikan.manga.ReviewEntity
import com.destructo.sushi.model.jikan.season.AnimeSubEntity
import com.destructo.sushi.model.mal.anime.Anime
import com.destructo.sushi.model.mal.anime.StartSeason
import com.destructo.sushi.model.mal.userAnimeList.UserAnimeData
import com.destructo.sushi.ui.animeSchedule.ScheduleAdapter
import com.destructo.sushi.ui.user.animeList.UserAnimeListAdapter
import com.destructo.sushi.ui.user.animeList.UserAnimePagerAdapter
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import timber.log.Timber
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.format.DateTimeFormatter
import java.time.temporal.TemporalAccessor
import java.util.*


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
                    .placeholder(R.drawable.test_img)
            )
            .into(imgView)
    }
}

@BindingAdapter("animeTitle")
fun TextView.setAnimeTitle(data: String?) {
    data?.let {
        text = formatTitleText(data)
    }
}


@BindingAdapter("smallTitleFormatted")
fun TextView.setCharacterTitle(data: String?) {
    data?.let {
        text = formatSmallTitleText(data)
    }
}


@BindingAdapter("formatEpisodeDuration")
fun TextView.setEpisodeDetail(data: Int?) {
    data?.let {
        val formattedText = secondToMinute(data)
        text = formattedText
    }
}


@BindingAdapter("formatStartSeason")
fun TextView.setEpisodeDetail(data: StartSeason?) {
    data?.let {
        val formattedText = startSeasonFormatter(data)
        text = formattedText
    }
}

@BindingAdapter("formatReviewOverview")
fun TextView.setReviewOverview(data: Review?) {
    data?.let {
        val formattedText = "Overall score: ${data.reviewer?.reviewScores?.overall} " +
                "• ${data.reviewer?.episodesSeen} Episodes Seen"
        text = formattedText
    }
}

@BindingAdapter("formatReviewOverview")
fun TextView.setReviewOverview(data: ReviewEntity?) {
    data?.let {
        val formattedText = "Overall score: ${data.reviewer?.scores?.overall} " +
                "• ${data.reviewer?.chaptersRead} Chapters Read"
        text = formattedText
    }
}


@BindingAdapter("formatAnimeRank")
fun TextView.setAnimeRank(data: Int?) {
    data?.let {rank->
        val formattedText = "#${rank}"
        text = formattedText
    }
}


@BindingAdapter("formatMangaRank")
fun TextView.setMangaRank(data: Int?) {
    data?.let {rank->
        val formattedText = "#${rank}"
        text = formattedText
    }
}

@BindingAdapter("formatBoolean")
fun TextView.covertBoolToString(data: Boolean?) {
    data?.let {
        text = if (data) "Yes" else "No"
    }

}

@BindingAdapter("formatReviewHelpful")
fun TextView.setReviewHelpful(data: Review?) {
    data?.let {
        val formattedText = "${data.helpfulCount} People found this helpful"
        text = formattedText
    }
}

@BindingAdapter("formatReviewHelpful")
fun TextView.setReviewHelpful(data: ReviewEntity?) {
    data?.let {
        val formattedText = "${data.helpfulCount} People found this helpful"
        text = formattedText
    }
}

@BindingAdapter("animeScore")
fun TextView.setAnimeScore(data: String?) {

    if (data == null || data == " " || data.isEmpty()) {
        text = "N/A"
    } else {
        text = data
    }
}

@BindingAdapter("userAnimeSubtitle")
fun TextView.setUserAnimeSubtitle(data: Anime?) {
    if (data != null) {
        val finalStr = "${data.mediaType}, ${startSeasonFormatter(data.startSeason)} "
        text = finalStr
    }
}

@BindingAdapter(value = ["watchedEp", "totalEp"])
fun setAnimeProgress(progressBar: ProgressBar,watchedEp: Int?, totalEp:Int?) {

    if (watchedEp != null && totalEp!=null) {
        progressBar.max = totalEp
        progressBar.progress = watchedEp
    }
}

@BindingAdapter(value = ["readCh", "totalCh"])
fun setMangaProgress(progressBar: ProgressBar,readCh: Int?, totalCh:Int?) {

    if (readCh != null && totalCh!=null) {
        progressBar.max = totalCh
        progressBar.progress = readCh
    }
}

@BindingAdapter(value = ["watched", "total"])
fun setAnimeEpisodes(textView: TextView,watched: String?, total:String?) {

    if (watched == null || watched == " " || watched.isEmpty()) {
        textView.text = "N/A"
    } else {
        total?.let{totalEp->
            val finalStr = "Ep: $watched/$totalEp"
            textView.text = finalStr
        }
    }
}


@BindingAdapter(value = ["chapter", "total"])
fun setMangaChapter(textView: TextView,chapter: String?, total:String?) {

    if (chapter == null || chapter == " " || chapter.isEmpty()) {
        textView.text = "N/A"
    } else {
        total?.let{totalEp->
            val finalStr = "Ch: $chapter/$totalEp"
            textView.text = finalStr
        }
    }
}

@BindingAdapter(value = ["volume", "total"])
fun setMangaVolume(textView: TextView,volume: String?, total:String?) {

    if (volume == null || volume == " " || volume.isEmpty()) {
        textView.text = "N/A"
    } else {
        total?.let{totalEp->
            val finalStr = "Vol: $volume/$totalEp"
            textView.text = finalStr
        }
    }
}

@BindingAdapter("characterNickName")
fun TextView.formatCharacterNickName(data: List<String?>?) {

    if (data != null ) {
        val finalStr = StringBuilder()
        data.forEach{ finalStr.append("$it ") }
        text = finalStr
    } else {
        text = "Not Available"
    }
}


@BindingAdapter("animeDescFormat")
fun TextView.formatAnimeDescString(data: String?) {
    data?.let {
        val str = data.replace("_", " ", true)
        val words = str.split(" ")
        var newStr = ""
        words.forEach {
            newStr += it.capitalize(Locale.ROOT) + " "
        }

        text = newStr
    }
}

@BindingAdapter("animeScheduleData")
fun bindScheduleRecycler(recyclerView: RecyclerView, data: List<AnimeSubEntity?>?) {
    recyclerView.setHasFixedSize(true)
    recyclerView.addItemDecoration(GridSpacingItemDeco(3,25,true))
    val adapter = ScheduleAdapter()
    recyclerView.adapter = adapter
    adapter.submitList(data)
}


private fun formatTitleText(text: String): String {
    var title = if (text.length > 12) text.take(12) else return text

    if (title.takeLast(1) == " ") {
        title = "${title.take(11)}..."
    } else {
        title = "$title..."
    }
    return title
}


private fun formatSmallTitleText(text: String): String {
    var title = if (text.length > 7) text.take(7) else return text

    if (title.takeLast(1) == " ") {
        title = "${title.take(7)}.."
    } else {
        title = "$title.."
    }
    return title
}

private fun secondToMinute(seconds: Int?): String {
    var result = ""
    if (seconds != null) {
        result = "${seconds.div(60)}min"
        return result
    } else {
        result = "Unknown"
    }

    return result
}


private fun startSeasonFormatter(data: StartSeason?): String {
    var result = ""
    if (data != null) {
        result = "${data.season?.capitalize(Locale.ROOT)} ${data.year}"
        return result
    } else {
        result = "Unknown"
    }

    return result
}