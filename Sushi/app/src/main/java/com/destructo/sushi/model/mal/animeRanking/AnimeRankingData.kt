package com.destructo.sushi.model.mal.animeRanking


import android.os.Parcelable
import androidx.room.Embedded
import androidx.room.Entity
import com.destructo.sushi.model.mal.anime.Anime
import com.destructo.sushi.model.mal.common.Ranking
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "anime_ranking_list", primaryKeys = ["anime"])
data class AnimeRankingData(
    @Json(name = "node")
    val anime: Anime,
    @Json(name = "ranking")
    @Embedded
    val ranking: Ranking?=null,
):Parcelable