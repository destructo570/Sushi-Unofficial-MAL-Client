package com.destructo.sushi.model.jikan.user.friends


import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.Json

@Entity(tableName = "jikan_user_friend_list")
data class Friend(

    @PrimaryKey(autoGenerate = true)
    val id: Int?=null,
    @Json(name = "friends_since")
    val friendsSince: String?=null,
    @Json(name = "image_url")
    val imageUrl: String?=null,
    @Json(name = "last_online")
    val lastOnline: String?=null,
    @Json(name = "url")
    val url: String?=null,
    @Json(name = "username")
    val username: String?=null,

)