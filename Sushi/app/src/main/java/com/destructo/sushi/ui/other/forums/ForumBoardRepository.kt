package com.destructo.sushi.ui.other.forums

import androidx.lifecycle.MutableLiveData
import com.destructo.sushi.model.mal.forum.ForumBoard
import com.destructo.sushi.network.MalApi
import com.destructo.sushi.network.Resource
import javax.inject.Inject


class ForumBoardRepository
@Inject
constructor(
    val malApi: MalApi
) {

    suspend fun getForumBoards(

    ): MutableLiveData<Resource<ForumBoard>> {

        val result = MutableLiveData<Resource<ForumBoard>>()
        result.value = Resource.loading(null)

        val response = malApi.getForumBoardsAsync()
        try {
            val forumBoards = response.await()
            result.value = Resource.success(forumBoards)

        } catch (e: Exception) {
            result.value = Resource.error(e.message ?: "", null)
        }
        return result
    }
}
