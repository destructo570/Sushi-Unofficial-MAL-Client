package com.destructo.sushi.ui.manga

import androidx.lifecycle.MutableLiveData
import com.destructo.sushi.ALL_MANGA_FIELDS
import com.destructo.sushi.BASIC_MANGA_FIELDS
import com.destructo.sushi.model.mal.mangaRanking.MangaRanking
import com.destructo.sushi.network.MalApi
import com.destructo.sushi.network.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.Exception
import javax.inject.Inject

class MangaRepository
@Inject
constructor(private val malApi: MalApi){

    fun getTopManga(ranking_type:String,limit:String?,offset:String?)
            : MutableLiveData<Resource<MangaRanking>>{

        val result = MutableLiveData<Resource<MangaRanking>>()
        result.value = Resource.loading(null)

        GlobalScope.launch {
            val getTopMangaDeferred =
                malApi.getMangaRankingAsync(ranking_type, limit, offset, BASIC_MANGA_FIELDS)
                try {
                    val mangaRanking = getTopMangaDeferred.await()
                    withContext(Dispatchers.Main){
                        result.value = Resource.success(mangaRanking)
                    }
                }catch (e:Exception){
                    withContext(Dispatchers.Main){
                        result.value = Resource.error(e.message ?: "", null)
                    }
                }

        }
        return result
    }
}