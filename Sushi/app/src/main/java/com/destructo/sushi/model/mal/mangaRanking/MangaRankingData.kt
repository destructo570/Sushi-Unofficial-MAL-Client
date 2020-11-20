package com.destructo.sushi.model.mal.mangaRanking


import android.os.Parcelable
import androidx.room.Embedded
import androidx.room.Entity
import com.destructo.sushi.model.mal.common.Ranking
import com.destructo.sushi.model.mal.manga.Manga
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "manga_ranking_list", primaryKeys = ["manga"])
data class MangaRankingData(
    @Json(name = "node")
    val manga: Manga,
    @Json(name = "ranking")
    @Embedded
    val ranking: Ranking?=null
):Parcelable