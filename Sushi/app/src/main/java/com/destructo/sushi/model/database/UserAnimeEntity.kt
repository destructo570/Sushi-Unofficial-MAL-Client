package com.destructo.sushi.model.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.destructo.sushi.model.mal.anime.MyAnimeListStatus
import com.destructo.sushi.model.mal.anime.StartSeason
import com.destructo.sushi.model.mal.common.MainPicture
import com.destructo.sushi.model.mal.updateUserAnimeList.UpdateUserAnime
import com.destructo.sushi.model.mal.userAnimeList.UserAnimeData

@Entity(tableName = "user_anime_list")
data class UserAnimeEntity (
    @PrimaryKey(autoGenerate = true)
    val id: Int?=null,
    val malId: Int?=null,
    val status: String?=null,
    val title: String?=null,
    val score: Double?=null,
    val myAnimeListStatus: MyAnimeListStatus?=null,
    var media_type: String?=null,
    var numEpisodes: Int?=null,
    var start_season: StartSeason?=null,
    val mainPicture: MainPicture?=null,

    ){

    companion object {
        private fun fromUpdateUserAnime(data: UserAnimeData): UserAnimeEntity {
            return UserAnimeEntity(
                malId = data.anime.id,
                status = data.anime.status,
                title = data.anime.title,
                media_type = data.anime.mediaType,
                start_season = data.anime.startSeason,
                mainPicture = data.anime.mainPicture,
                numEpisodes = data.anime.numEpisodes,
                score = data.anime.mean,
                myAnimeListStatus = data.anime.myAnimeListStatus
            )
        }

        fun fromListOfUpdateUserAnime(data: List<UserAnimeData?>): List<UserAnimeEntity> {
            val result = mutableListOf<UserAnimeEntity>()
            for (anime in data){
                anime?.let { result.add(fromUpdateUserAnime(it)) }
            }
            return result
        }

    }

    fun updateUserStatus(data: UpdateUserAnime){
        myAnimeListStatus?.comments = data.comments
        myAnimeListStatus?.isRewatching = data.isRewatching
        myAnimeListStatus?.numEpisodesWatched = data.numEpisodesWatched
        myAnimeListStatus?.numTimesRewatched = data.numTimesRewatched
        myAnimeListStatus?.priority = data.priority
        myAnimeListStatus?.rewatchValue = data.rewatchValue
        myAnimeListStatus?.score = data.score
        myAnimeListStatus?.status = data.status
        myAnimeListStatus?.tags = data.tags
        myAnimeListStatus?.updatedAt = data.updatedAt
    }



}