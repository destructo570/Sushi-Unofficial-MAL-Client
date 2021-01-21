package com.destructo.sushi.model.database

import com.destructo.sushi.DETAILS_CACHE_EXPIRE_TIME_LIMIT

interface BaseDatabaseEntity{
    val time: Long

    fun isCacheExpired(): Boolean
    {
        return (System.currentTimeMillis() - time) > DETAILS_CACHE_EXPIRE_TIME_LIMIT
    }

}