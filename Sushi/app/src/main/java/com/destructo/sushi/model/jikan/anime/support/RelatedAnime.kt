package com.destructo.sushi.model.jikan.anime.support


import com.destructo.sushi.model.jikan.MALEntity
import com.squareup.moshi.Json

data class RelatedAnime(

    @Json(name="Adaptation")
    val adaptation: List<MALEntity?>? = null,

    @Json(name="Alternative setting")
    val alternativeSetting: List<MALEntity?>? = null,

    @Json(name="Alternative version")
    val alternativeVersion: List<MALEntity?>? = null,

    @Json(name="Character")
    val character: List<MALEntity?>? = null,

    @Json(name="Full story")
    val fullStory: List<MALEntity?>? = null,

    @Json(name="Other")
    val other: List<MALEntity?>? = null,

    @Json(name="Parent story")
    val parentStory: List<MALEntity?>? = null,

    @Json(name="Prequel")
    val prequel: List<MALEntity?>? = null,

    @Json(name="Sequel")
    val sequel: List<MALEntity?>? = null,

    @Json(name="Side story")
    val sideStory: List<MALEntity?>? = null,

    @Json(name="Spin-off")
    val spinOff: List<MALEntity?>? = null,

    @Json(name="Summary")
    val summary: List<MALEntity?>? = null,

    )