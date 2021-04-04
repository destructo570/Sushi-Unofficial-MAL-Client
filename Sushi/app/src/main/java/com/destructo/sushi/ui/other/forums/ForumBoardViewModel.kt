package com.destructo.sushi.ui.other.forums

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.destructo.sushi.model.mal.forum.ForumBoard
import com.destructo.sushi.network.Resource
import kotlinx.coroutines.launch

class ForumBoardViewModel
    @ViewModelInject
    constructor(
        @Assisted
        savedStateHandle: SavedStateHandle,
        private val forumRepo: ForumBoardRepository
    )
    :ViewModel() {

    private var _forumBoards: MutableLiveData<Resource<ForumBoard>> = MutableLiveData()
    val forumBoards: MutableLiveData<Resource<ForumBoard>>
        get() = _forumBoards

    fun getForumBoardList() {
       viewModelScope.launch { _forumBoards = forumRepo.getForumBoards() }
    }
}