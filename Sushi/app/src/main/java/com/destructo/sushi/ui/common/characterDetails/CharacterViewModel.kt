package com.destructo.sushi.ui.common.characterDetails

import androidx.lifecycle.*
import com.destructo.sushi.model.jikan.character.Character
import com.destructo.sushi.network.JikanApi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CharacterViewModel
@Inject
constructor(

    savedStateHandle: SavedStateHandle,
    val jikanApi: JikanApi
) : ViewModel() {

    private val _character: MutableLiveData<Character> = MutableLiveData()
    val character: LiveData<Character>
        get() = _character

    fun getCharacterDetails(characterId: Int) {
        val charId = characterId.toString()

        viewModelScope.launch {
            try {
                _character.value = jikanApi.getCharacterDetailsAsync(charId)
            } catch (e: Exception) {
            }
        }
    }
}