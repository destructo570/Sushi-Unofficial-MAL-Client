package com.destructo.sushi.ui.manga

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.destructo.sushi.model.top.TopManga
import com.destructo.sushi.network.JikanApi
import kotlinx.coroutines.launch
import timber.log.Timber

class MangaViewModel
@ViewModelInject
constructor(
    @Assisted
    savedStateHandle: SavedStateHandle,
    private val jikanApi: JikanApi
):ViewModel(){


    private val _topManga:MutableLiveData<TopManga> = MutableLiveData()
    val topManga:LiveData<TopManga>
    get() = _topManga

    fun getTopMangaList(page:String,subtype:String){
        viewModelScope.launch{
            var getTopMangaDeferred = jikanApi.getTopMangaAsync(page,subtype)
        try {
            val topManga = getTopMangaDeferred.await()
            _topManga.value = topManga
        }catch (e:Exception){
            Timber.e("Error: ${e.message}")
        }
        }
    }
}