package com.destructo.sushi.ui.anime.upcomingAnime

import androidx.lifecycle.MutableLiveData
import com.destructo.sushi.ALL_ANIME_FIELDS
import com.destructo.sushi.model.mal.animeRanking.AnimeRanking
import com.destructo.sushi.network.MalApi
import com.destructo.sushi.network.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.Exception
import javax.inject.Inject

class UpcomingAnimeRepository@Inject
constructor(private val malApi: MalApi)
{

    var upcomingAnime: MutableLiveData<Resource<AnimeRanking>> = MutableLiveData()

    fun getUpcomingAnime(offset:String?, limit:String?) {
        upcomingAnime.value = Resource.loading(null)

        GlobalScope.launch {
            val getUpcomingDeferred = malApi.getAnimeRankingAsync("upcoming",limit,offset,
                ALL_ANIME_FIELDS
            )
            try {
                val animeRanking = getUpcomingDeferred.await()
                withContext(Dispatchers.Main){
                    upcomingAnime.value = Resource.success(animeRanking)
                }
            }catch (e: Exception){
                withContext(Dispatchers.Main){
                    upcomingAnime.value = Resource.error(e.message ?: "", null)}
            }
        }
    }
}