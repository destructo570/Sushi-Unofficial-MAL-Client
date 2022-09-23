package com.destructo.sushi.ui.other.forums


import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.destructo.sushi.model.mal.forum.ForumBoard
import com.destructo.sushi.network.Resource
import kotlinx.coroutines.launch
import javax.inject.Inject

class ForumBoardViewModel
    @Inject
    constructor(

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