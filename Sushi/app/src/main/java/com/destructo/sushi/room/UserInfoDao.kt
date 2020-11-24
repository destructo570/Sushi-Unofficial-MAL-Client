package com.destructo.sushi.room

import androidx.lifecycle.LiveData
import androidx.room.*
import com.destructo.sushi.model.database.UserInfoEntity
import com.destructo.sushi.model.mal.userAnimeList.UserAnimeData
import com.destructo.sushi.model.mal.userInfo.UserInfo

@Dao
interface UserInfoDao {

    @Query("SELECT * FROM user_info WHERE id LIKE :userId")
    fun getUserInfo(userId: Int): LiveData<UserInfoEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertUserInfo(user: UserInfoEntity?)

    @Query("DELETE FROM user_info")
    fun clear()

    @Delete
    fun deleteUserInfo(user: UserInfoEntity)
}