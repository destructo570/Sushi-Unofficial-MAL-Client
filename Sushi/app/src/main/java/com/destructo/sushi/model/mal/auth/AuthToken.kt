package com.destructo.sushi.model.mal.auth

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.Json

@Entity(tableName = "user_authentication")
data class AuthToken(
    @PrimaryKey(autoGenerate = true)
    val id:Int,
    @Json(name="expires_in")
    val expiresIn:Int?=null,
    @Json(name="access_token")
    val accessToken:String?=null,
    @Json(name="token_type")
    val tokenType:String?=null,
    @Json(name="refresh_token")
    val refreshToken:String?=null
)