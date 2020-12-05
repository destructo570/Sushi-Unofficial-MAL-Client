package com.destructo.sushi.ui.person

import androidx.lifecycle.MutableLiveData
import com.destructo.sushi.model.jikan.person.PersonEntity
import com.destructo.sushi.network.JikanApi
import com.destructo.sushi.network.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class PersonRepository
@Inject
constructor(
    private val jikanApi: JikanApi,
    ){

    var personData: MutableLiveData<Resource<PersonEntity>> = MutableLiveData()

    fun getPersonData(malId:Int){
        personData.value = Resource.loading(null)
        GlobalScope.launch {
            val personId: String = malId.toString()
            val getPersonByIdDeferred = jikanApi.getPersonDetailsAsync(personId)
            try {
                val personDetails = getPersonByIdDeferred.await()
                withContext(Dispatchers.Main) {
                    personData.value = Resource.success(personDetails)
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    personData.value = Resource.error(e.message ?: "", null)
                }
            }
        }

    }
}