package com.destructo.sushi.ui.other.forums.topics

import androidx.lifecycle.MutableLiveData
import com.destructo.sushi.model.mal.forum.ForumTopics
import com.destructo.sushi.network.MalApi
import com.destructo.sushi.network.Resource
import javax.inject.Inject

class TopicsRepository
@Inject
constructor(val malApi: MalApi) {


    suspend fun getForumTopics(
        board_id: Int, subboard_id: Int?, limit: Int, offset: Int,
        sort: String, q: String?, topic_username: String?,
        username: String?
    ): MutableLiveData<Resource<ForumTopics>> {

        val result = MutableLiveData<Resource<ForumTopics>>()
        result.value = Resource.loading(null)

        val response = malApi.getForumTopicsAsync(
            board_id, subboard_id, limit, offset, sort, q, topic_username, username
        )

        try {
            val forumTopics = response.await()
            result.value = Resource.success(forumTopics)

        } catch (e: Exception) {
            result.value = Resource.error(e.message ?: "", null)
        }

        return result
    }


}