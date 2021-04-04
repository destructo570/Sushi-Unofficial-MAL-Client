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

    var userAnimeList: MutableLiveData<Resource<UserAnimeList>> = MutableLiveData()

    var userAnimeStatus: MutableLiveData<Resource<UpdateUserAnime>> = MutableLiveData()

    var nextPage: MutableLiveData<String> = MutableLiveData()

    suspend fun getUserAnimeList(sortType: String) {
        userAnimeList.value = Resource.loading(null)

        try {
            val userAnime = malApi.getUserAnimeListAsync(
                "@me", DEFAULT_USER_LIST_PAGE_LIMIT,
                null, sortType, "", BASIC_ANIME_FIELDS, true
            )
            userAnimeListDao.insertUserAnimeList(UserAnimeEntity.fromListOfUpdateUserAnime(userAnime.data!!))
            setNextPage(userAnime)
            userAnimeList.value = Resource.success(userAnime)
        } catch (e: Exception) {
            userAnimeList.value = Resource.error(e.message ?: "", null)

        }
    }

    suspend fun addEpisode(animeId: String, numberOfEp: Int?, status: String?) {
        userAnimeStatus.value = Resource.loading(null)

        try {
            val animeStatus = malApi.updateUserAnime(
                animeId,
                status, null, null, numberOfEp,
                null, null, null, null, null,
                null, null
            )
            updateCachedUserAnime(animeId, animeStatus)
            userAnimeStatus.value = Resource.success(animeStatus)

        } catch (e: Exception) {
            userAnimeStatus.value = Resource.error(e.message ?: "", null)
        }
    }

    suspend fun getNextPage() {
        if (!nextPage.value.isNullOrEmpty()) {
            userAnimeList.value = Resource.loading(null)

            try {
                val userAnime = malApi.getUserAnimeNextAsync(nextPage.value!!)
                userAnimeListDao.insertUserAnimeList(
                    UserAnimeEntity.fromListOfUpdateUserAnime(
                        userAnime.data!!
                    )
                )
                setNextPage(userAnime)
                userAnimeList.value = Resource.success(userAnime)

            } catch (e: Exception) {
                userAnimeList.value = Resource.error(e.message ?: "", null)

            }
        }
    }

    private fun updateCachedUserAnime(
        animeId: String,
        animeStatus: UpdateUserAnime
    ) {
        val anime = userAnimeListDao.getUserAnimeById(animeId.toInt())
        anime.updateUserStatus(animeStatus)
        userAnimeListDao.deleteUserAnimeById(animeId.toInt())
        userAnimeListDao.insertUserAnime(anime)
    }

    private fun setNextPage(userAnime: UserAnimeList) {
        nextPage.value = if (!userAnime.paging?.next.isNullOrEmpty()
            && userAnime.paging?.next != nextPage.value
        ) {
            userAnime.paging?.next
        } else null
    }

}