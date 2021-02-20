package com.destructo.sushi.ui.user.profile

import androidx.lifecycle.MutableLiveData
import com.destructo.sushi.model.jikan.user.animeList.ProfileUserAnimeList
import com.destructo.sushi.model.mal.userInfo.UserInfo
import com.destructo.sushi.network.JikanApi
import com.destructo.sushi.network.MalApi
import com.destructo.sushi.network.Resource
import com.destructo.sushi.room.ProfileAnimeListDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ProfileRepository
@Inject
constructor(
    val jikanApi: JikanApi,
    val malApi: MalApi,
    val profileAnimeListDao: ProfileAnimeListDao
) {

    var userInfo: MutableLiveData<Resource<com.destructo.sushi.model.jikan.user.UserInfo>> =
        MutableLiveData()

    var userAnimeList: MutableLiveData<Resource<ProfileUserAnimeList>> =
        MutableLiveData()

    var userInfoMalApi: MutableLiveData<Resource<UserInfo>> =
        MutableLiveData()

    var nextPage = 1

    suspend fun getUserInfo(userName: String) {
        userInfo.value = Resource.loading(null)
        val getUserMangaDeferred = jikanApi.getUserDetailsAsync(userName)
        try {
            val response = getUserMangaDeferred.await()
            withContext(Dispatchers.Main) {
                userInfo.value = Resource.success(response)
            }
        } catch (e: Exception) {
            withContext(Dispatchers.Main) {
                userInfo.value = Resource.error(e.message ?: "", null)
            }
        }
    }

    suspend fun getUserAnimeList(userName: String, status: String) {
        userAnimeList.value = Resource.loading(null)
        val getUserMangaDeferred = jikanApi.getUserAnimeListAsync(userName,status,nextPage)
        try {
            val response = getUserMangaDeferred.await()
            withContext(Dispatchers.Main) {
                userAnimeList.value = Resource.success(response)
                profileAnimeListDao.insertAnimeList(response.anime)
                nextPage++
            }
        } catch (e: Exception) {
            withContext(Dispatchers.Main) {
                userAnimeList.value = Resource.error(e.message ?: "", null)
            }
        }
    }



    suspend fun getUserInfoFromMalApi(userName: String) {

        userInfoMalApi.value = Resource.loading(null)
        val getUserMangaDeferred = malApi.getUserInfo(userName)
        try {
            val response = getUserMangaDeferred.await()
            withContext(Dispatchers.Main) {
                userInfoMalApi.value = Resource.success(response)
            }
        } catch (e: Exception) {
            withContext(Dispatchers.Main) {
                userInfoMalApi.value = Resource.error(e.message ?: "", null)
            }
        }
    }




}