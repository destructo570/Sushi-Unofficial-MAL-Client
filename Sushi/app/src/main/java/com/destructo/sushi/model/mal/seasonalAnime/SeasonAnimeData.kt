package com.destructo.sushi.model.mal.seasonalAnime


import android.os.Parcelable
import androidx.room.Entity
import com.destructo.sushi.model.mal.anime.Anime
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "seasonal_anime_list", primaryKeys = ["anime"])
data class SeasonAnimeData(
    @Json(name = "node")
    val anime: Anime
):Parcelable