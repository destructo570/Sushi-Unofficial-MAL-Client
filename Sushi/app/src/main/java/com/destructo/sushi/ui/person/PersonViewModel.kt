package com.destructo.sushi.ui.person

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.destructo.sushi.model.jikan.person.PersonEntity
import com.destructo.sushi.network.Resource

class PersonViewModel
    @ViewModelInject
    constructor(
        private val personRepo: PersonRepository
    ): ViewModel() {


     val personData: MutableLiveData<Resource<PersonEntity>> = personRepo.personData

    fun getPersonData(malId:Int){
        personRepo.getPersonData(malId)
    }

}