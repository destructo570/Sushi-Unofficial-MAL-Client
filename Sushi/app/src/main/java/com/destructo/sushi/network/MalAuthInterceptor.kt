package com.destructo.sushi.network

import com.destructo.sushi.util.ACCESS_TOKEN
import com.destructo.sushi.util.SessionManager
import okhttp3.Interceptor
import okhttp3.Response
import timber.log.Timber

class MalAuthInterceptor(
    private val sessionManager: SessionManager
): Interceptor {

    private val currentSession = sessionManager.getSession()

    override fun intercept(chain: Interceptor.Chain): Response {

        val requestBuilder = chain.request().newBuilder()
            requestBuilder.addHeader("Authorization","Bearer ${currentSession[ACCESS_TOKEN]}")

        val response = chain.proceed(requestBuilder.build())

        if(!response.isSuccessful){
            when(response.code){
                429 -> {
                        Timber.e("Too many requests, Retrying in 3 seconds...")
                }
                401 -> {
                        Timber.e("Too many requests, Retrying in 3 seconds...")
                }

            }
            
        }

        return response
    }
}