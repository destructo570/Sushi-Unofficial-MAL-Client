package com.destructo.sushi.ui.person

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.destructo.sushi.model.jikan.person.PersonEntity
import com.destructo.sushi.network.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PersonViewModel
    @Inject
    constructor(
        private val personRepo: PersonRepository
    ): ViewModel() {

     val personData: MutableLiveData<Resource<PersonEntity>> = personRepo.personData

    fun getPersonData(malId:Int){
        viewModelScope.launch { personRepo.getPersonData(malId) }
    }

}