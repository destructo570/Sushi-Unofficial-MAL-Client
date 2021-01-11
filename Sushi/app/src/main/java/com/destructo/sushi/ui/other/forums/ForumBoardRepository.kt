package com.destructo.sushi.ui.other.forums

import androidx.lifecycle.MutableLiveData
import com.destructo.sushi.model.mal.forum.ForumBoard
import com.destructo.sushi.network.MalApi
import com.destructo.sushi.network.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject


class ForumBoardRepository
@Inject
constructor(
    val malApi: MalApi
) {

    fun getForumBoards(): MutableLiveData<Resource<ForumBoard>> {

        val result = MutableLiveData<Resource<ForumBoard>>()
        result.value = Resource.loading(null)

        GlobalScope.launch {
            val response = malApi.getForumBoardsAsync()
            try {
                val forumBoards = response.await()
                withContext(Dispatchers.Main) {
                    result.value = Resource.success(forumBoards)
                }

            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    result.value = Resource.error(e.message ?: "", null)
                }
            }
        }
        return result
    }
}
