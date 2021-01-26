package com.destructo.sushi.ui.manga.mangaCharacters

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.destructo.sushi.CACHE_EXPIRE_TIME_LIMIT
import com.destructo.sushi.model.database.MangaCharacterListEntity
import com.destructo.sushi.model.jikan.manga.character.MangaCharacter
import com.destructo.sushi.network.JikanApi
import com.destructo.sushi.network.Resource
import com.destructo.sushi.room.MangaCharacterListDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MangaCharactersViewModel
    @ViewModelInject
    constructor(
        private val mangaCharacterListDao: MangaCharacterListDao,
        private val jikanApi: JikanApi
        ):ViewModel()
{

    var mangaCharacter: MutableLiveData<Resource<MangaCharacter>> = MutableLiveData()

    fun getMangaCharacters(malId: Int) {
        mangaCharacter.value = Resource.loading(null)

        viewModelScope.launch {
            val mangaCharacterListCache = mangaCharacterListDao.getMangaCharacterListById(malId)

            if (mangaCharacterListCache != null){

                if((System.currentTimeMillis() - mangaCharacterListCache.time) > CACHE_EXPIRE_TIME_LIMIT) {
                    mangaCharactersCall(malId)
                }else{
                    val mangaCharacterList = mangaCharacterListCache.mangaCharacterList
                    withContext(Dispatchers.Main) {
                        mangaCharacter.value = Resource.success(mangaCharacterList)
                    }
                }
            }else{
                mangaCharactersCall(malId)
            }
        }
    }


    private suspend fun mangaCharactersCall(malId:Int) {
        val mangaId: String = malId.toString()
        val getMangaCharactersDeferred = jikanApi.getMangaCharactersAsync(mangaId)
        try {
            val mangaCharacterList = getMangaCharactersDeferred.await()
            val mangaCharacterRequest = MangaCharacterListEntity(
                mangaCharacterList = mangaCharacterList,
                id = malId,
                time = System.currentTimeMillis()
            )
            mangaCharacterListDao.insertMangaCharacterList(mangaCharacterRequest)
            withContext(Dispatchers.Main) {
                mangaCharacter.value = Resource.success(mangaCharacterRequest.mangaCharacterList)
            }
        } catch (e: Exception) {
            withContext(Dispatchers.Main) {
                mangaCharacter.value = Resource.error(e.message ?: "", null)

            }
        }
    }
}