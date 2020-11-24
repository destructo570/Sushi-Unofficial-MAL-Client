package com.destructo.sushi.model.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.destructo.sushi.model.mal.userInfo.UserInfo

@Entity(tableName = "user_info")
data class UserInfoEntity(
    @PrimaryKey(autoGenerate = false)
    val id: Int,
    val userInfo: UserInfo
) {

}