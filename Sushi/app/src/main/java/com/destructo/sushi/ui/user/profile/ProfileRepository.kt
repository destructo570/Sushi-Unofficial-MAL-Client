package com.destructo.sushi.ui.user.profile

import androidx.lifecycle.MutableLiveData
import com.destructo.sushi.ALL_MANGA_FIELDS
import com.destructo.sushi.enum.mal.UserMangaSort
import com.destructo.sushi.enum.mal.UserMangaStatus
import com.destructo.sushi.model.database.UserInfoEntity
import com.destructo.sushi.model.mal.userInfo.UserInfo
import com.destructo.sushi.model.mal.userMangaList.UserMangaList
import com.destructo.sushi.network.MalApi
import com.destructo.sushi.network.Resource
import com.destructo.sushi.room.UserInfoDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.Exception
import javax.inject.Inject

class ProfileRepository
@Inject
constructor(
    val malApi: MalApi,
    private val userInfoDao: UserInfoDao
){

    var userInfo: MutableLiveData<Resource<UserInfo>> = MutableLiveData()

    fun getUserInfo(fields:String) {

        userInfo.value = Resource.loading(null)

        GlobalScope.launch {
            val getUserMangaDeferred = malApi.getUserInfo(fields)
            try {
                val response = getUserMangaDeferred.await()
                val userInfoEntity = UserInfoEntity(userInfo = response, id=0)
                userInfoDao.insertUserInfo(userInfoEntity)
                withContext(Dispatchers.Main){
                    userInfo.value = Resource.success(userInfoEntity.userInfo)
                }
            }catch (e: Exception){
                withContext(Dispatchers.Main){
                    userInfo.value = Resource.error(e.message ?: "", null)}
            }
        }
    }

}