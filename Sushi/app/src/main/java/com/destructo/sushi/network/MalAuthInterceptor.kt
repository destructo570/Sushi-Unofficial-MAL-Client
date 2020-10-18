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

        return chain.proceed(requestBuilder.build())
    }
}