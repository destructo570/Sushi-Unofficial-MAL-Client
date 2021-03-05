package com.destructo.sushi.model.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.destructo.sushi.model.mal.common.MainPicture
import com.destructo.sushi.model.mal.manga.MyMangaListStatus
import com.destructo.sushi.model.mal.updateUserMangaList.UpdateUserManga
import com.destructo.sushi.model.mal.userMangaList.UserMangaData

@Entity(tableName = "user_manga_list")
data class UserMangaEntity (
    @PrimaryKey(autoGenerate = true)
    val id: Int?=null,
    val malId: Int?=null,
    val status: String?=null,
    val title: String?=null,
    val score: Double?=null,
    val myMangaListStatus: MyMangaListStatus?=null,
    var media_type: String?=null,
    var numChapters: Int?=null,
    val mainPicture: MainPicture?=null,

    ){

    companion object {
        private fun fromUpdateUserManga(data: UserMangaData): UserMangaEntity {
            return UserMangaEntity(
                malId = data.manga.id,
                status = data.manga.status,
                title = data.manga.title,
                media_type = data.manga.mediaType,
                mainPicture = data.manga.mainPicture,
                numChapters = data.manga.numChapters,
                score = data.manga.mean,
                myMangaListStatus = data.manga.myMangaListStatus
            )
        }

        fun fromListOfUserMangaData(data: List<UserMangaData?>): List<UserMangaEntity> {
            val result = mutableListOf<UserMangaEntity>()
            for (anime in data){
                anime?.let { result.add(fromUpdateUserManga(it)) }
            }
            return result
        }

    }

    fun updateUserStatus(data: UpdateUserManga){
        myMangaListStatus?.comments = data.comments
        myMangaListStatus?.isRereading = data.isRereading
        myMangaListStatus?.numChaptersRead = data.numChaptersRead
        myMangaListStatus?.numVolumesRead = data.numVolumesRead
        myMangaListStatus?.num_times_reread = data.numTimesReread
        myMangaListStatus?.priority = data.priority
        myMangaListStatus?.reread_value = data.rereadValue
        myMangaListStatus?.score = data.score
        myMangaListStatus?.status = data.status
        myMangaListStatus?.tags = data.tags
        myMangaListStatus?.updatedAt = data.updatedAt
    }



}