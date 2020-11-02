package com.destructo.sushi.ui.anime.currentlyAiring

import androidx.lifecycle.MutableLiveData
import com.destructo.sushi.ALL_ANIME_FIELDS
import com.destructo.sushi.ALL_MANGA_FIELDS
import com.destructo.sushi.enum.mal.UserMangaSort
import com.destructo.sushi.model.mal.animeRanking.AnimeRanking
import com.destructo.sushi.model.mal.userMangaList.UserMangaList
import com.destructo.sushi.network.MalApi
import com.destructo.sushi.network.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.Exception
import javax.inject.Inject

class CurrentlyAiringRepository
@Inject
constructor(private val malApi: MalApi)
{

    var currentlyAiring: MutableLiveData<Resource<AnimeRanking>> = MutableLiveData()

    fun getCurrentlyAiring(offset:String?, limit:String?) {
        currentlyAiring.value = Resource.loading(null)

        GlobalScope.launch {
            val getAiringDeferred = malApi.getAnimeRankingAsync("airing",limit,offset,
                ALL_ANIME_FIELDS
            )
            try {
                val animeRanking = getAiringDeferred.await()
                withContext(Dispatchers.Main){
                    currentlyAiring.value = Resource.success(animeRanking)
                }
            }catch (e: Exception){
                withContext(Dispatchers.Main){
                    currentlyAiring.value = Resource.error(e.message ?: "", null)}
            }
        }
    }
}