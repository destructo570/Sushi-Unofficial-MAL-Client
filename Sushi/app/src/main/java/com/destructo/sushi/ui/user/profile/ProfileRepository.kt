package com.destructo.sushi.ui.user.profile

import androidx.lifecycle.MutableLiveData
import com.destructo.sushi.network.JikanApi
import com.destructo.sushi.network.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ProfileRepository
@Inject
constructor(
    val jikanApi: JikanApi,
) {

    var userInfo: MutableLiveData<Resource<com.destructo.sushi.model.jikan.user.UserInfo>> =
        MutableLiveData()

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

}