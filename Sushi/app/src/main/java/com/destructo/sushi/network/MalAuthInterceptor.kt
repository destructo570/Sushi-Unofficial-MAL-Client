package com.destructo.sushi.network

import com.destructo.sushi.util.ACCESS_TOKEN
import com.destructo.sushi.util.REFRESH_TOKEN
import com.destructo.sushi.util.SessionManager
import okhttp3.Interceptor
import okhttp3.Response
import timber.log.Timber

class MalAuthInterceptor(
    private val sessionManager: SessionManager
): Interceptor {

    private val currentSession = sessionManager.getSession()

    override fun intercept(chain: Interceptor.Chain): Response {

        var requestBuilder = chain.request().newBuilder()
            requestBuilder.addHeader("Authorization","Bearer ${currentSession[ACCESS_TOKEN]}")

        var response = chain.proceed(requestBuilder.build())

        if(!response.isSuccessful){
            when(response.code){
                429 -> {
                    try {
                        Timber.e("You are being rate limited by MAL, Retrying in 3 seconds...")
                        Thread.sleep(3000L)
                        response.close()
                        requestBuilder = chain.request().newBuilder()
                        requestBuilder.addHeader("Authorization","Bearer ${currentSession[ACCESS_TOKEN]}")
                    }catch (e:InterruptedException){

                    }
                }
                401 -> {
                    try {
                        Timber.e("Refreshing Token...")
                        Timber.e("Inteceptor current token: ${sessionManager.getLatestToken()?.takeLast(4)}")
                        sessionManager.refreshToken(currentSession[REFRESH_TOKEN])
                        Timber.e("Inteceptor new token: ${sessionManager.getLatestToken()?.takeLast(4)}")
                        response.close()
                        requestBuilder = chain.request().newBuilder()
                        requestBuilder.addHeader("Authorization","Bearer ${sessionManager.getLatestToken()}")
                        Timber.e("Inteceptor new new token: ${sessionManager.getLatestToken()?.takeLast(4)}")

                    }catch (e:InterruptedException){

                    }
                }

            }
            response = chain.proceed(requestBuilder.build())
        }

        return response
    }
}