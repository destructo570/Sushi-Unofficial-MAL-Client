package com.destructo.sushi.ui.user.profile

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.destructo.sushi.model.jikan.user.animeList.Anime
import com.destructo.sushi.network.JikanApi
import retrofit2.HttpException
import java.io.IOException

private const val JIKAN_ANIME_LIST_PAGE_INDEX = 1

class UserAnimeListPagingSource(
    private val jikanApi: JikanApi,
    private val userName: String,
    private val status: String,
): PagingSource<Int, Anime>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Anime> {
        val position = params.key ?: JIKAN_ANIME_LIST_PAGE_INDEX

        val getUserMangaDeferred = jikanApi.getUserAnimeListAsync(userName,status,position)
        return try {
            val response = getUserMangaDeferred.await()
            val animeList = response.anime!!
            LoadResult.Page(
                data = animeList,
                prevKey = if (position == JIKAN_ANIME_LIST_PAGE_INDEX) null else position - 1,
                nextKey = if (animeList.isEmpty()) null else position + 1
            )

        } catch (e: IOException) {
            LoadResult.Error(e)
        } catch (e: HttpException) {
            LoadResult.Error(e)
        }

    }

    override fun getRefreshKey(state: PagingState<Int, Anime>): Int? {
        return 0
    }
}