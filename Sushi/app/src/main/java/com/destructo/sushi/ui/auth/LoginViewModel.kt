package com.destructo.sushi.ui.auth

import android.net.Uri
import android.util.Log
import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.destructo.sushi.*
import com.destructo.sushi.network.AuthApi
import com.destructo.sushi.network.MalAuthApi
import com.destructo.sushi.util.PKCE
import com.destructo.sushi.util.SessionManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import timber.log.Timber
import java.lang.Exception

class LoginViewModel
@ViewModelInject
constructor(
    @Assisted
    savedStateHandle: SavedStateHandle,
    private val malAuthApi: MalAuthApi,
    private val sessionManager: SessionManager
    ): ViewModel(){

    private val _authenticationCode: MutableLiveData<String> = MutableLiveData()
    val authenticationCode: LiveData<String>
        get() = _authenticationCode

    private val _authComplete: MutableLiveData<String?> = MutableLiveData()
    val authComplete: LiveData<String?>
        get() = _authComplete


    private val job = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + job)

    private fun getAuthToken(
        authCode:String){
        uiScope.launch {
            val currentSession = sessionManager.getSession()
            val pkce = currentSession[PKCE]
            val getAuthTokenDeferred = malAuthApi.getAuthTokenAsync(
                CLIENT_ID,authCode, pkce!!, AC_GRANT_CODE)
            try {
                val authToken = getAuthTokenDeferred.await()
                sessionManager.createSession(authToken)
                _authComplete.value = authToken.accessToken

            }catch (e: Exception){
                Timber.e("Error: %s", e.message)
            }
        }
    }

    fun onAuthentication(authCode:String){
        getAuthToken(authCode)
    }

    fun onAuthenticationCompleted(){
        _authenticationCode.value = null
        _authComplete.value = null
    }

    fun extractAuthCode(uri: Uri){
        if (uri.toString().startsWith(REDIRECT_URL)) {
            val authCode = uri.getQueryParameter("code")
            _authenticationCode.value = authCode
        }
    }



}