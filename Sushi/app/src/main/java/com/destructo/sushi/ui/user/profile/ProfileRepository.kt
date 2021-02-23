package com.destructo.sushi.ui.user.profile

import androidx.lifecycle.MutableLiveData
import com.destructo.sushi.model.jikan.user.friends.UserFriends
import com.destructo.sushi.model.mal.userInfo.UserInfo
import com.destructo.sushi.network.JikanApi
import com.destructo.sushi.network.MalApi
import com.destructo.sushi.network.Resource
import com.destructo.sushi.room.ProfileAnimeListDao
import com.destructo.sushi.room.ProfileMangaListDao
import com.destructo.sushi.room.ProfileUserFriendListDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ProfileRepository
@Inject
constructor(
    val jikanApi: JikanApi,
    val malApi: MalApi,
    private val profileAnimeListDao: ProfileAnimeListDao,
    private val profileUserFriendListDao: ProfileUserFriendListDao,
    private val profileUserMangaListDao: ProfileMangaListDao
) {

    var userInfo: MutableLiveData<Resource<com.destructo.sushi.model.jikan.user.UserInfo>> =
        MutableLiveData()

    var userFriendList: MutableLiveData<Resource<UserFriends>> =
        MutableLiveData()

    var userInfoMalApi: MutableLiveData<Resource<UserInfo>> =
        MutableLiveData()

    var nextFriendPage = 1

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


    suspend fun getUserFriendList(userName: String) {

        val getFriendsListDeferred = jikanApi.getUserFriendListAsync(userName,nextFriendPage)
        try {
            val response = getFriendsListDeferred.await()
            withContext(Dispatchers.Main) {
                userFriendList.value = Resource.success(response)
                if (!response.friends.isNullOrEmpty()){
                    response.friends.forEach { it?.setFriendsWithUser(userName) }
                    profileUserFriendListDao.insertFriendList(response.friends)
                    nextFriendPage++
                }
            }
        } catch (e: Exception) {
            withContext(Dispatchers.Main) {
                userFriendList.value = Resource.error(e.message ?: "", null)
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