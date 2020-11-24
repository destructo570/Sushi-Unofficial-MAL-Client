package com.destructo.sushi

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.destructo.sushi.model.database.UserInfoEntity
import com.destructo.sushi.model.mal.userInfo.UserInfo
import com.destructo.sushi.network.MalApi
import com.destructo.sushi.network.Resource
import com.destructo.sushi.room.UserInfoDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.lang.Exception

class MainViewModel
@ViewModelInject
constructor(
    @Assisted
    savedStateHandle: SavedStateHandle,
    private val userInfoDao: UserInfoDao,
    private val malApi: MalApi
):ViewModel() {

    val userInfoEntity: LiveData<UserInfoEntity?> = userInfoDao.getUserInfo(0)

    fun getUserInfo(fields:String) {
        viewModelScope.launch {
            val getUserMangaDeferred = malApi.getUserInfo(fields)
            try {
                val response = getUserMangaDeferred.await()
                val userInfoEntity = UserInfoEntity(userInfo = response, id=0)
                userInfoDao.insertUserInfo(userInfoEntity)

            }catch (e: Exception){
                Timber.e("Error: %s", e.message)
            }
        }
    }

}