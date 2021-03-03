package com.destructo.sushi.util

import androidx.core.net.toUri

object OffSet {

     fun calculateOffset(nextPage: String?, prevPage:String?, limit: String): String{
        var currentOffset = "0"
        if(!nextPage.isNullOrBlank()){
            val nextOffset = getOffset(nextPage)
            if (!nextOffset.isNullOrBlank()){
                val temp = nextOffset.toInt().minus(limit.toInt())
                if (temp>=0){
                    currentOffset = temp.toString()
                }
            }
            return currentOffset
        }else{
            val prevOffset = getOffset(prevPage)
            if (!prevOffset.isNullOrBlank()){
                val temp = prevOffset.toInt().plus(limit.toInt())
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