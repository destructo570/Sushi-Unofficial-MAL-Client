package com.destructo.sushi.ui.user.profile.mangalist

import androidx.lifecycle.MutableLiveData
import com.destructo.sushi.model.jikan.user.mangaList.ProfileUserMangaList
import com.destructo.sushi.network.JikanApi
import com.destructo.sushi.network.Resource
import com.destructo.sushi.room.ProfileMangaListDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ProfileUserMangaRepository
@Inject
constructor(
    val jikanApi: JikanApi,
    private val profileMangaListDao: ProfileMangaListDao,
){

    var userMangaList: MutableLiveData<Resource<ProfileUserMangaList>> =
        MutableLiveData()

    var nextMangaPage = 1

    suspend fun getUserMangaList(userName: String, status: String) {
        userMangaList.value = Resource.loading(null)
        val getUserMangaDeferred = jikanApi.getUserMangaListAsync(userName,status,nextMangaPage)
        try {
            val response = getUserMangaDeferred.await()
            withContext(Dispatchers.Main) {
                userMangaList.value = Resource.success(response)
                if (!response.manga.isNullOrEmpty()){
                    profileMangaListDao.insertMangaList(response.manga)
                    nextMangaPage++
                }
            }
        } catch (e: Exception) {
            withContext(Dispatchers.Main) {
                userMangaList.value = Resource.error(e.message ?: "", null)
            }
        }
    }
}