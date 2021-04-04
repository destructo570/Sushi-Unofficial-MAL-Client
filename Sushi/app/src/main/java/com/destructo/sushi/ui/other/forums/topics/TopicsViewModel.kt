package com.destructo.sushi.ui.other.forums.topics

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.destructo.sushi.model.mal.forum.ForumTopics
import com.destructo.sushi.network.Resource
import kotlinx.coroutines.launch

class TopicsViewModel
@ViewModelInject
constructor(
    @Assisted
    savedStateHandle: SavedStateHandle,
    private val topicsRepository: TopicsRepository
) :ViewModel() {


    private var _forumTopics: MutableLiveData<Resource<ForumTopics>> = MutableLiveData()
    val forumTopics: MutableLiveData<Resource<ForumTopics>>
        get() = _forumTopics

    fun getTopicList(borad_id: Int, subbuard_id:Int?, limit:Int, offset:Int,
                     sort:String, q:String?, topic_username:String?,
                     username:String?) {

        viewModelScope.launch { _forumTopics = topicsRepository.getForumTopics(
            borad_id, subbuard_id, limit, offset, sort, q, topic_username, username
        ) }

    }


}