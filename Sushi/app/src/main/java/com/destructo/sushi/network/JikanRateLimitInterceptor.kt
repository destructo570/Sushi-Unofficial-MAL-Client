package com.destructo.sushi.network

import android.widget.Toast
import okhttp3.Interceptor
import okhttp3.Response
import timber.log.Timber

class JikanRateLimitInterceptor: Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        var response = chain.proceed(chain.request())

        if(!response.isSuccessful || response.code == 429){
            try {
                Timber.e("You are being rate limited by Jikan, Retrying in 3 seconds.")
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