package com.destructo.sushi.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.destructo.sushi.model.jikan.user.friends.Friend

@Dao
interface ProfileUserFriendListDao {
    @Query("SELECT * FROM jikan_user_friend_list ")
    fun getFriendList(): LiveData<List<Friend>>

    @Query("SELECT * FROM jikan_user_friend_list WHERE friendsWith LIKE :username")
    fun getFriendListByUsername(username: String): LiveData<List<Friend>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertFriendList(friendList: List<Friend?>?)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertFriend(friend: Friend?)

    @Query("DELETE FROM jikan_user_friend_list")
    fun deleteAllFriends()

    @Query("DELETE FROM jikan_user_friend_list WHERE friendsWith LIKE :username")
    fun deleteAllFriendsByUsername(username: String)
}