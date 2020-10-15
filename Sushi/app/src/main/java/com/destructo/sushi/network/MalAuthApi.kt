package com.destructo.sushi.network

import com.destructo.sushi.model.mal.auth.AuthToken
import kotlinx.coroutines.Deferred
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface MalAuthApi {


    @POST("v1/oauth2/token")
    @FormUrlEncoded
    fun getAuthTokenAsync(
        @Field("client_id") clientId:String,
        @Field("code") code:String?,
        @Field("code_verifier") code_verifier:String,
        @Field("grant_type") grant_type:String
    ): Deferred<AuthToken>

    @POST("v1/oauth2/token")
    @FormUrlEncoded
    fun refreshAuthTokenAsync(
        @Field("client_id") clientId:String,
        @Field("refresh_token") refreshToken:String,
        @Field("grant_type") grantType:String
    ): Deferred<AuthToken>

}