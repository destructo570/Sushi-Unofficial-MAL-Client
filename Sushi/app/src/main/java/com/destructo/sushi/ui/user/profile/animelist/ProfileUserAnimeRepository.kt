package com.destructo.sushi.ui.user.profile.animelist

import androidx.lifecycle.MutableLiveData
import com.destructo.sushi.model.jikan.user.animeList.ProfileUserAnimeList
import com.destructo.sushi.network.JikanApi
import com.destructo.sushi.network.Resource
import com.destructo.sushi.room.ProfileAnimeListDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ProfileUserAnimeRepository
@Inject
    constructor(
    val jikanApi: JikanApi,
    private val profileAnimeListDao: ProfileAnimeListDao,
) {

    var userAnimeList: MutableLiveData<Resource<ProfileUserAnimeList>> =
        MutableLiveData()

    var nextAnimePage = 1

    suspend fun getUserAnimeList(userName: String, status: String) {
        userAnimeList.value = Resource.loading(null)
        val getUserMangaDeferred = jikanApi.getUserAnimeListAsync(userName,status,nextAnimePage)
        try {
            val response = getUserMangaDeferred.await()
            withContext(Dispatchers.Main) {
                userAnimeList.value = Resource.success(response)
                if (!response.anime.isNullOrEmpty()){
                    profileAnimeListDao.insertAnimeList(response.anime)
                    nextAnimePage++
                }
            }
        } catch (e: Exception) {
            withContext(Dispatchers.Main) {
                userAnimeList.value = Resource.error(e.message ?: "", null)
            }
        }
    }

}