package com.destructo.sushi.ui.user.animeList

import androidx.lifecycle.MutableLiveData
import com.destructo.sushi.BASIC_ANIME_FIELDS
import com.destructo.sushi.DEFAULT_USER_LIST_PAGE_LIMIT
import com.destructo.sushi.model.database.UserAnimeEntity
import com.destructo.sushi.model.mal.updateUserAnimeList.UpdateUserAnime
import com.destructo.sushi.model.mal.userAnimeList.UserAnimeList
import com.destructo.sushi.network.MalApi
import com.destructo.sushi.network.Resource
import com.destructo.sushi.room.UserAnimeDao
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MyAnimeListRepository
@Inject
constructor(
    private val malApi: MalApi,
    private val userAnimeListDao: UserAnimeDao
) {

    var userAnimeList: MutableLiveData<Resource<List<UserAnimeEntity>>> = MutableLiveData()

    var userAnimeStatus: MutableLiveData<Resource<UpdateUserAnime>> = MutableLiveData()

    var nextPage: MutableLiveData<String> = MutableLiveData()

    suspend fun getUserAnimeList(sortType: String) {
        userAnimeList.postValue(Resource.loading(null))
        val cache = userAnimeListDao.getAllUserAnime().firstOrNull()
        if (cache != null && !cache.isCacheExpired()) {
            userAnimeList.postValue(Resource.success(userAnimeListDao.getUserAnimeList().value))
        } else getUserAnimeListCall(sortType)

    }

    private suspend fun getUserAnimeListCall(sortType: String){
        try {
            val userAnime = malApi.getUserAnimeListAsync(
                "@me", DEFAULT_USER_LIST_PAGE_LIMIT,
                null, sortType, "", BASIC_ANIME_FIELDS, true
            )
            val listOfUserAnime = UserAnimeEntity.fromListOfUpdateUserAnime(userAnime.data!!)
            userAnimeListDao.clear()
            userAnimeListDao.insertUserAnimeList(listOfUserAnime)
            setNextPage(userAnime)
            userAnimeList.postValue(Resource.success(listOfUserAnime))
        } catch (e: Exception) {
            userAnimeList.postValue(Resource.error(e.message ?: "", null))

        }
    }

    suspend fun addEpisode(animeId: String, numberOfEp: Int?, status: String?) {
        userAnimeStatus.postValue(Resource.loading(null))

        try {
            val animeStatus = malApi.updateUserAnime(
                animeId,
                status, null, null, numberOfEp,
                null, null, null, null, null,
                null, null
            )
            updateCachedUserAnime(animeId, animeStatus)
            userAnimeStatus.postValue(Resource.success(animeStatus))

        } catch (e: Exception) {
            userAnimeStatus.postValue(Resource.error(e.message ?: "", null))
        }
    }

    suspend fun getNextPage() {
        if (!nextPage.value.isNullOrEmpty()) {
            userAnimeList.postValue(Resource.loading(null))

            try {
                val userAnime = malApi.getUserAnimeNextAsync(nextPage.value!!)
                val listOfUserAnime = UserAnimeEntity.fromListOfUpdateUserAnime(userAnime.data!!)
                userAnimeListDao.insertUserAnimeList(listOfUserAnime)
                setNextPage(userAnime)
                userAnimeList.postValue(Resource.success(listOfUserAnime))

            } catch (e: Exception) {
                userAnimeList.postValue(Resource.error(e.message ?: "", null))

            }
        }
    }

    private suspend fun updateCachedUserAnime(
        animeId: String,
        animeStatus: UpdateUserAnime
    ) {
        val anime = userAnimeListDao.getUserAnimeById(animeId.toInt())
        anime.updateUserStatus(animeStatus)
        userAnimeListDao.deleteUserAnimeById(animeId.toInt())
        userAnimeListDao.insertUserAnime(anime)
    }

    private fun setNextPage(userAnime: UserAnimeList) {
        if (!userAnime.paging?.next.isNullOrEmpty()
            && userAnime.paging?.next != nextPage.value
        ) {
            nextPage.postValue(userAnime.paging?.next)
        } else  nextPage.postValue(null)
    }

}