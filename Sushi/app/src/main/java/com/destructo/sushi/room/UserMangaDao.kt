package com.destructo.sushi.room

import androidx.lifecycle.LiveData
import androidx.room.*
import com.destructo.sushi.model.database.UserMangaEntity

@Dao
interface UserMangaDao {
    @Query("SELECT * FROM user_manga_list")
    fun getUserMangaList(): LiveData<List<UserMangaEntity>>

    @Query("SELECT * FROM user_manga_list WHERE myStatus = :status")
    fun getUserMangaListByStatus(status: String): LiveData<List<UserMangaEntity>>

    @Query("SELECT * FROM user_manga_list")
    fun getAllUserManga(): List<UserMangaEntity>

    @Query("SELECT * FROM user_manga_list WHERE malId LIKE :malId")
    fun getUserMangaById(malId: Int): UserMangaEntity

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUserMangaList(mangaList: List<UserMangaEntity>?)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUserManga(manga: UserMangaEntity?)

    @Query("DELETE FROM user_manga_list")
    fun clear()

    @Query("DELETE FROM user_manga_list WHERE malId LIKE :id")
    fun deleteUserMangaById(id: Int)

    @Delete
    fun deleteUserManga(manga: UserMangaEntity)
}