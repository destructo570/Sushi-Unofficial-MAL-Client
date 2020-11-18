package com.destructo.sushi.network

import android.content.Context
import android.util.Log
import com.destructo.sushi.DEBUG_ACCESS_TOKEN
import okhttp3.Interceptor
import okhttp3.Response
import timber.log.Timber

class MalAuthInterceptor(
): Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val requestBuilder = chain.request().newBuilder()
            requestBuilder.addHeader("Authorization","Bearer $DEBUG_ACCESS_TOKEN")

        var response = chain.proceed(requestBuilder.build())

        if(!response.isSuccessful || response.code == 429){
            try {
                Timber.e("You are being rate limited by MAL, Retrying in 3 seconds.")
                Thread.sleep(3000L)
            }catch (e:InterruptedException){

            }finally {
                response.close()
            }
            response = chain.proceed(chain.request())
        }

        return response
    }
}