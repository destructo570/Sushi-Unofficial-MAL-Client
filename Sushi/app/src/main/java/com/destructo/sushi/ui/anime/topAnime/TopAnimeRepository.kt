package com.destructo.sushi.ui.anime.topAnime

import androidx.lifecycle.MutableLiveData
import com.destructo.sushi.ALL_ANIME_FIELDS
import com.destructo.sushi.BASIC_ANIME_FIELDS
import com.destructo.sushi.model.mal.animeRanking.AnimeRanking
import com.destructo.sushi.network.MalApi
import com.destructo.sushi.network.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.lang.Exception
import javax.inject.Inject

class TopAnimeRepository
@Inject
constructor(val malApi: MalApi){
    fun getTopAnime(ranking_type:String,offset:String?, limit:String?)
    :MutableLiveData<Resource<AnimeRanking>>{

        val result = MutableLiveData<Resource<AnimeRanking>>()
        result.value = Resource.loading(null)

        GlobalScope.launch {
            try {
                val getTopAnimeDeferred = malApi.getAnimeRankingAsync(ranking_type,limit,offset,
                    BASIC_ANIME_FIELDS)
                val animeRanking = getTopAnimeDeferred.await()
                withContext(Dispatchers.Main){
                    result.value = Resource.success(animeRanking)
                }
            }catch (e:Exception){
                withContext(Dispatchers.Main){
                    result.value = Resource.error(e.message ?: "",null)
                }
            }
        }
    return  result
    }

}