package com.destructo.sushi.util

import android.content.Context
import android.content.SharedPreferences
import android.content.SharedPreferences.Editor
import com.destructo.sushi.AR_GRANT_CODE
import com.destructo.sushi.CLIENT_ID
import com.destructo.sushi.model.mal.auth.AuthToken
import com.destructo.sushi.network.MalAuthApi
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

private const val SHARED_PREF_NAME = "SushiLoginPref"
const val LOGGED_IN = "IsLoggedIn"
const val EXPIRES_IN = "ExpiresIn"
const val ACCESS_TOKEN = "AccessToken"
const val TOKEN_TYPE = "TokenType"
const val REFRESH_TOKEN = "RefreshToken"
const val PKCE = "CodeVerifier"

@Singleton
class SessionManager
@Inject
constructor(
    @ApplicationContext
    private val context:Context,
    private val malAuthApi: MalAuthApi
){
    private var sharedPreferences: SharedPreferences
    private var editor: Editor
    private var PRIVATE_MODE = 0

    init {
        sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, PRIVATE_MODE)
        editor = sharedPreferences.edit()
        generatePkce()
    }

    fun refreshToken(refreshToken: String?){
        if(!refreshToken.isNullOrBlank()) {
            GlobalScope.launch{
            val refreshTokenDeferred = malAuthApi.refreshAuthTokenAsync(CLIENT_ID, refreshToken, AR_GRANT_CODE)
            try {
                val newAuthToken = refreshTokenDeferred.await()
                createSession(newAuthToken)
            }catch (e: Exception){
                Timber.e("Error: Failed to refresh token")
            }
        }
        }

    }

    fun createSession(authToken: AuthToken){
        editor.putBoolean(LOGGED_IN, true)
        editor.putInt(EXPIRES_IN, authToken.expiresIn)
        editor.putString(ACCESS_TOKEN, authToken.accessToken)
        editor.putString(TOKEN_TYPE, authToken.tokenType)
        editor.putString(REFRESH_TOKEN, authToken.refreshToken)
        editor.commit()

    }

    fun getSession(): HashMap<String, String>{
//        Timber.e("Access Token: %s",sharedPreferences.getString(ACCESS_TOKEN, null))
//        Timber.e("Token Type: %s",sharedPreferences.getString(TOKEN_TYPE, null))
//        Timber.e("Refresh Token: %s",sharedPreferences.getString(REFRESH_TOKEN, null))
//        Timber.e("PKCE: %s",sharedPreferences.getString(PKCE, null))

        val currentSession = HashMap<String, String>()
        currentSession[EXPIRES_IN] = sharedPreferences.getInt(EXPIRES_IN, 0).toString()
        currentSession[ACCESS_TOKEN] = sharedPreferences.getString(ACCESS_TOKEN, null) ?: ""
        currentSession[TOKEN_TYPE] = sharedPreferences.getString(TOKEN_TYPE, null) ?: ""
        currentSession[REFRESH_TOKEN] = sharedPreferences.getString(REFRESH_TOKEN, null) ?: ""
        currentSession[PKCE] = sharedPreferences.getString(PKCE, null) ?: ""

        return currentSession
    }

    fun getLatestToken(): String?{
        return sharedPreferences.getString(ACCESS_TOKEN, null)
    }

    fun generatePkce(){
        editor.putString(PKCE, Pkce.generate())
        editor.commit()

    }

    fun getPkce(): String?{
       return sharedPreferences.getString(PKCE, null)
    }

    fun clearSession(){
        editor.clear()
        editor.commit()
    }

    fun checkLogin(): Boolean {
        return this.isLoggedIn()
    }

    private fun isLoggedIn(): Boolean {
        return sharedPreferences.getBoolean(LOGGED_IN, false)
    }

}