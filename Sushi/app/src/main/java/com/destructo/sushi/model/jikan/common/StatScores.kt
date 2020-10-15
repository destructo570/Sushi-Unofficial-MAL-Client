package com.destructo.jikanplay.model.common


import com.squareup.moshi.Json

data class StatScores(
    @Json(name = "1")
    val statScore1: StatScoreData?=null,
    @Json(name = "2")
    val statScore2: StatScoreData?=null,
    @Json(name = "3")
    val statScore3: StatScoreData?=null,
    @Json(name = "4")
    val statScore4: StatScoreData?=null,
    @Json(name = "5")
    val statScore5: StatScoreData?=null,
    @Json(name = "6")
    val statScore6: StatScoreData?=null,
    @Json(name = "7")
    val statScore7: StatScoreData?=null,
    @Json(name = "8")
    val statScore8: StatScoreData?=null,
    @Json(name = "9")
    val statScore9: StatScoreData?=null,
    @Json(name = "10")
    val statScore10: StatScoreData?=null,
)