package com.destructo.sushi.ui.base

import androidx.core.net.toUri
import com.destructo.sushi.DEFAULT_USER_LIST_PAGE_LIMIT

open class BaseRepository {
    
    private fun calcOffset(nextPage: String?, prevPage:String?): String{
        var currentOffset = "0"
        if(!nextPage.isNullOrBlank()){
            val nextOffset = getOffset(nextPage)
            if (!nextOffset.isNullOrBlank()){
                val temp = nextOffset.toInt().minus(DEFAULT_USER_LIST_PAGE_LIMIT.toInt())
                if (temp>=0){
                    currentOffset = temp.toString()
                }
            }
            return currentOffset
        }else{
            val prevOffset = getOffset(prevPage)
            if (!prevOffset.isNullOrBlank()){
                val temp = prevOffset.toInt().plus(DEFAULT_USER_LIST_PAGE_LIMIT.toInt())
                if (temp>=0){
                    currentOffset = temp.toString()
                }
            }
            return currentOffset
        }

    }

    private fun getOffset(url: String?): String?{

        return if (!url.isNullOrBlank()){
            val uri = url.toUri()
            uri.getQueryParameter("offset").toString()
        }else{
            null
        }
    }

}