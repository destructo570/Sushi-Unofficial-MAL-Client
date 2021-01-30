package com.destructo.sushi.util

import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.databinding.BindingAdapter
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.destructo.sushi.ANIME_ID_ARG
import com.destructo.sushi.BOARD_ID_ARG
import com.destructo.sushi.R
import com.destructo.sushi.adapter.ForumBoardAdapter
import com.destructo.sushi.listener.MalIdListener
import com.destructo.sushi.model.jikan.common.Review
import com.destructo.sushi.model.jikan.manga.ReviewEntity
import com.destructo.sushi.model.jikan.season.AnimeSubEntity
import com.destructo.sushi.model.mal.anime.*
import com.destructo.sushi.model.mal.forum.Board
import com.destructo.sushi.model.mal.manga.Author
import com.destructo.sushi.model.mal.manga.Manga
import com.destructo.sushi.model.mal.manga.MyMangaListStatus
import com.destructo.sushi.model.mal.manga.Serialization
import com.destructo.sushi.ui.animeSchedule.ScheduleAdapter
import java.text.NumberFormat
import java.util.*

/**
 * Loading image into image view with Glide.
 */
@BindingAdapter("imageUrl")
fun bindImage(imgView: ImageView, imgUrl: String?) {
    imgUrl?.let {
        imgView.load(imgUrl){
            placeholder(R.drawable.test_img)
            crossfade(true)
            crossfade(300)
        }
    }
}

@BindingAdapter("animeButtonState")
fun TextView.setAnimeButtonState(data: MyAnimeListStatus?) {
    data?.let {
        if(data.status != null) {
            text = data.status.toString().toTitleCase()
            setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_check_fill, 0, 0, 0)
        }else{
            text = context.getString(R.string.edit_status)
            setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_check_fill, 0, 0, 0)
        }
    }
}

@BindingAdapter("mangaButtonState")
fun TextView.setMangaButtonState(data: MyMangaListStatus?) {
    data?.let {
        if(data.status != null) {
            text = data.status.toString().toTitleCase()
            setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_check_fill, 0, 0, 0)
        }else{
            text = context.getString(R.string.edit_status)
            setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_check_fill, 0, 0, 0)
        }
    }
}

@BindingAdapter("formattedLineBreakText")
fun TextView.replaceLineBreak(data: String?) {
    data?.let {
            text = data.replace("\\n", "\n")
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

@BindingAdapter("formatAnimeStudios")
fun TextView.setStudiosList(data: List<Studio?>?) {
    data?.let {
        var formattedText = ""
        for (studio in data){
            formattedText = "${studio?.name}, "
        }
        text = formattedText
    }
}

@BindingAdapter("formatMangaSerialization")
fun TextView.setSerializationList(data: List<Serialization?>?) {
    data?.let {
        var formattedText = ""
        for (serialization in data){
            formattedText = "${serialization?.serializationInfo?.name}, "
        }
        text = formattedText
    }
}

@BindingAdapter("formatMangaAuthors")
fun TextView.setMangaAuthorList(data: List<Author?>?) {
    data?.let {
        var formattedText = ""
        for (author in data){
            formattedText = "${author?.authorInfo?.firstName} ${author?.authorInfo?.lastName}, "
        }
        text = formattedText
    }
}

@BindingAdapter("formatAnimeBroadcast")
fun TextView.setAnimeBroadcast(data: Broadcast?) {
    data?.let {
        val formattedText = "${data.dayOfTheWeek?.toTitleCase()}• ${data.startTime}"
        text = formattedText
    }
}

@BindingAdapter("formatAnimePremiered")
fun TextView.setAnimePremiered(data: Anime?) {
    data?.let {
        val formattedText = "${data.startSeason?.season?.toTitleCase()}${data.startSeason?.year.toString()}"
        text = formattedText
    }
}

@BindingAdapter("formatMangaPublished")
fun TextView.setMangaPublished(data: Manga?) {
    data?.let {
        val formattedText = "${data.startDate?.toTitleCase()} to ?"
        text = formattedText
    }
}

@BindingAdapter("formatAnimeAired")
fun TextView.setAnimeAired(data: Anime?) {
    data?.let {
        val formattedText = "${data.startDate?.toTitleCase()} to ${data.endDate?.toTitleCase() ?: "?"}"
        text = formattedText
    }
}

@BindingAdapter("formatAnimeAltTitle")
fun TextView.setAnimeAltTitles(data: Anime?) {
    data?.alternativeTitles?.synonyms?.let {synonym ->
        var formattedText = ""
        for (title in synonym){
            formattedText = "${title}, "
        }
        text = formattedText
    }
}

@BindingAdapter("formatMangaAltTitle")
fun TextView.setMangaAltTitles(data: Manga?) {
    data?.alternativeTitles?.synonyms?.let {synonym ->
        var formattedText = ""
        for (title in synonym){
            formattedText = "${title}, "
        }
        text = formattedText
    }
}

@BindingAdapter("formattedInteger")
fun TextView.formatNumber(num:Int?){
    text = if(num != null && num >= 0) {

        NumberFormat.getNumberInstance(Locale.US).format(num)
    }else{
        "N/A"
    }
}
@BindingAdapter("formattedRank")
fun TextView.formatRank(num:Int?){
    text = if(num != null && num >= 0) {
        val finalStr = NumberFormat.getNumberInstance(Locale.US).format(num)
        "#$finalStr"
    }else{
        "N/A"
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
        val str = String.format(context.getString(R.string.anime_review_subtitle),
            data.reviewer?.reviewScores?.overall, data.reviewer?.episodesSeen)
        text = str
    }
}

@BindingAdapter("formatReviewOverview")
fun TextView.setReviewOverview(data: ReviewEntity?) {
    data?.let {

        val str = String.format(this.context.getString(R.string.manga_review_subtitle),
            data.reviewer?.scores?.overall, data.reviewer?.chaptersRead)
        text = str
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
        val formattedText = String.format(this.context.getString(R.string.people_found_helpful),
            data.helpfulCount)
        text = formattedText
    }
}

@BindingAdapter("formatReviewHelpful")
fun TextView.setReviewHelpful(data: ReviewEntity?) {
    data?.let {
        val formattedText = String.format(this.context.getString(R.string.people_found_helpful),
            data.helpfulCount)
        text = formattedText
    }
}

@BindingAdapter("animeScore")
fun TextView.setAnimeScore(data: String?) {

    text = if (data == null || data == " " || data.isEmpty()) {
        "N/A"
    } else {
        data
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

    text = if (data != null ) {
        val finalStr = StringBuilder()
        data.forEach{ finalStr.append("$it ") }
        finalStr
    } else {
        "Not Available"
    }
}

@BindingAdapter("animeDescFormat")
fun TextView.formatAnimeDescString(data: String?) {
    data?.let {
        text = data.toTitleCase()
    }
}

@BindingAdapter("animeScheduleData")
fun bindScheduleRecycler(recyclerView: RecyclerView, data: List<AnimeSubEntity?>?) {
    recyclerView.setHasFixedSize(true)
    recyclerView.addItemDecoration(GridSpacingItemDeco(3,25,true))
    val adapter = ScheduleAdapter(MalIdListener { malId->
        malId?.let{recyclerView.findNavController().navigate(
            R.id.animeDetailFragment, bundleOf(Pair(ANIME_ID_ARG, it))
        )}
    })
    recyclerView.adapter = adapter
    adapter.submitList(data)
}

@BindingAdapter("forumCategoryData")
fun bindCategoryRecycler(recyclerView: RecyclerView, data: List<Board?>?) {
    val adapter = ForumBoardAdapter(MalIdListener {
        boardId-> recyclerView.findNavController().navigate(R.id.topicsFragment, bundleOf(
        Pair(BOARD_ID_ARG, boardId)))
    })
    recyclerView.adapter = adapter
    adapter.submitList(data)
}

private fun formatSmallTitleText(text: String): String {
    var title = if (text.length > 7) text.take(7) else return text

    title = if (title.takeLast(1) == " ") {
        "${title.take(7)}.."
    } else {
        "$title.."
    }
    return title
}

private fun secondToMinute(seconds: Int?): String {
    val result: String
    if (seconds != null) {
        result = "${seconds.div(60)}min"
        return result
    } else {
        result = "Unknown"
    }

    return result
}


private fun startSeasonFormatter(data: StartSeason?): String {
    val result: String
    if (data != null) {
        result = "${data.season?.capitalize(Locale.ROOT)} ${data.year}"
        return result
    } else {
        result = "Unknown"
    }

    return result
}
