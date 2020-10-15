package com.destructo.sushi.model.mal.auth

import com.squareup.moshi.Json

data class AuthToken(
    @Json(name="expires_in")
    val expiresIn:Int?=null,
    @Json(name="access_token")
    val accessToken:String?=null,
    @Json(name="token_type")
    val tokenType:String?=null,
    @Json(name="refresh_token")
    val refreshToken:String?=null
)