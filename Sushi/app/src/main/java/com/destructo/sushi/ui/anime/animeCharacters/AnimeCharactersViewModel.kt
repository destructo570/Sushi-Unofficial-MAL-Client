package com.destructo.sushi.ui.anime.animeCharacters

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.destructo.sushi.CACHE_EXPIRE_TIME_LIMIT
import com.destructo.sushi.model.database.AnimeCharacterListEntity
import com.destructo.sushi.model.jikan.anime.core.AnimeCharacterAndStaff
import com.destructo.sushi.network.JikanApi
import com.destructo.sushi.network.Resource
import com.destructo.sushi.room.AnimeCharacterListDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AnimeCharactersViewModel
    @ViewModelInject
    constructor(
        @Assisted
        savedStateHandle: SavedStateHandle,
        private val animeCharacterListDao: AnimeCharacterListDao,
        private val jikanApi: JikanApi,

        ): ViewModel(){


    var animeCharacterAndStaff: MutableLiveData<Resource<AnimeCharacterAndStaff>> =
        MutableLiveData()


    fun getAnimeCharacters(malId: Int) {
        animeCharacterAndStaff.value = Resource.loading(null)
        viewModelScope.launch {
            val animeCharacterListCache = animeCharacterListDao.getAnimeCharactersById(malId)
            if (animeCharacterListCache != null){
                if((System.currentTimeMillis() - animeCharacterListCache.time) > CACHE_EXPIRE_TIME_LIMIT) {
                    animeCharacterCall(malId)
                }else{
                    val mAnime = animeCharacterListCache.characterAndStaffList
                    withContext(Dispatchers.Main) {
                        animeCharacterAndStaff.value = Resource.success(mAnime)
                    }
                }
            }else{
                animeCharacterCall(malId)
            }

        }
    }

    private suspend fun animeCharacterCall(malId:Int) {
        try {
            val animeCharactersAndStaffList = jikanApi.getCharacterAndStaffAsync(malId.toString())
            val animeCharacterListEntity = AnimeCharacterListEntity(
                characterAndStaffList = animeCharactersAndStaffList,
                time = System.currentTimeMillis(),
                id = malId
            )
            animeCharacterListDao.insertAnimeCharacters(animeCharacterListEntity)
                animeCharacterAndStaff
                    .value = Resource.success(animeCharacterListEntity.characterAndStaffList)
        } catch (e: Exception) {
                animeCharacterAndStaff.value = Resource.error(e.message ?: "", null)
        }
    }

}