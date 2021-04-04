package com.destructo.sushi.ui.person

import androidx.lifecycle.MutableLiveData
import com.destructo.sushi.model.jikan.person.PersonEntity
import com.destructo.sushi.network.JikanApi
import com.destructo.sushi.network.Resource
import javax.inject.Inject

class PersonRepository
@Inject
constructor(
    private val jikanApi: JikanApi,
) {

    var personData: MutableLiveData<Resource<PersonEntity>> = MutableLiveData()

    suspend fun getPersonData(malId: Int) {
        personData.value = Resource.loading(null)
        try {
            val personDetails = jikanApi.getPersonDetailsAsync(malId.toString())
            personData.value = Resource.success(personDetails)
        } catch (e: Exception) {
            personData.value = Resource.error(e.message ?: "", null)
        }
    }
}