package com.destructo.sushi.room

import androidx.lifecycle.LiveData
import androidx.room.*
import com.destructo.sushi.model.mal.userAnimeList.UserAnimeData
import com.destructo.sushi.model.mal.userMangaList.UserMangaData

@Dao
interface UserMangaListDao {

    @Query("SELECT * FROM user_manga_list ORDER BY title ASC")
    fun getUserMangaList(): LiveData<List<UserMangaData>>

    @Query("SELECT * FROM user_manga_list WHERE status LIKE :status ORDER BY title ASC")
    fun getUserMangaListByStatus(status: String): LiveData<List<UserMangaData>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertUseMangaList(mangaList: List<UserMangaData?>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertUseManga(manga: UserMangaData?)

    @Query("DELETE FROM user_manga_list")
    fun clear()

    @Delete
    fun deleteUseManga(manga: UserMangaData)
}