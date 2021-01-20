package com.destructo.sushi.ui.anime

import androidx.lifecycle.MutableLiveData
import com.destructo.sushi.BASIC_ANIME_FIELDS
import com.destructo.sushi.model.mal.anime.Anime
import com.destructo.sushi.model.mal.animeRanking.AnimeRanking
import com.destructo.sushi.model.mal.animeRecom.SuggestedAnime
import com.destructo.sushi.model.mal.news.NewsItem
import com.destructo.sushi.model.mal.promotion.PromotionalItem
import com.destructo.sushi.model.mal.seasonalAnime.SeasonalAnime
import com.destructo.sushi.network.MalApi
import com.destructo.sushi.network.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.jsoup.Jsoup
import javax.inject.Inject

class AnimeRepository
@Inject
constructor(
    val malApi: MalApi
) {

    fun getTopAnime(
        ranking_type: String,
        offset: String?,
        limit: String?,
        nsfw: Boolean
    ): MutableLiveData<Resource<AnimeRanking>> {

        val result = MutableLiveData<Resource<AnimeRanking>>()
        result.value = Resource.loading(null)

        GlobalScope.launch {
            val topAnimeDeferred = malApi.getAnimeRankingAsync(
                ranking_type, limit, offset,
                BASIC_ANIME_FIELDS,
                nsfw
            )
            try {
                val topAnimeList = topAnimeDeferred.await()
                withContext(Dispatchers.Main) {
                    result.value = Resource.success(topAnimeList)
                }

            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    result.value = Resource.error(e.message ?: "", null)
                }
            }
        }
        return result
    }

    fun getSeasonalAnime(
        year: String,
        season: String,
        sort: String?,
        limit: String?,
        offset: String?
    ): MutableLiveData<Resource<SeasonalAnime>> {

        val result = MutableLiveData<Resource<SeasonalAnime>>()
        result.value = Resource.loading(null)

        GlobalScope.launch {
            val seasonalAnimeDeferred = malApi
                .getSeasonalAnimeAsync(year, season, sort, limit, offset, BASIC_ANIME_FIELDS)
            try {
                val seasonaAnime = seasonalAnimeDeferred.await()
                withContext(Dispatchers.Main) {
                    result.value = Resource.success(seasonaAnime)
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    result.value = Resource.error(e.message ?: "", null)
                }
            }
        }
        return result
    }


    fun getAnimeRecom(
        offset: String?,
        limit: String?,
        nsfw: Boolean
    ): MutableLiveData<Resource<List<Anime>>> {

        val result = MutableLiveData<Resource<List<Anime>>>()
        result.value = Resource.loading(null)

        GlobalScope.launch {
            val animeRecomDeferred = malApi
                .getAnimeRecomAsync(limit, offset, BASIC_ANIME_FIELDS, nsfw)
            try {
                val response = animeRecomDeferred.await()
                withContext(Dispatchers.Main) {
                    result.value = Resource.success(extractAnimeList(response))
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    result.value = Resource.error(e.message ?: "", null)
                }
            }
        }
        return result
    }


    private fun extractAnimeList(suggestedAnime: SuggestedAnime): List<Anime> {
        val animeList = mutableListOf<Anime>()
        suggestedAnime.data?.forEach { it?.anime?.let { anime ->
                animeList.add(anime) }
        }
        return animeList
    }


    fun getLatestNews(): MutableLiveData<Resource<MutableList<NewsItem>>> {

        val result = MutableLiveData<Resource<MutableList<NewsItem>>>()
        result.value = Resource.loading(null)
        val newsList = mutableListOf<NewsItem>()
        GlobalScope.launch{
            try {
                val doc = Jsoup.connect("https://myanimelist.net/news").get()

                val myanimelist = doc.getElementById("myanimelist")
                val wrapper = myanimelist.getElementsByClass("wrapper")
                val contentWrapper = wrapper[0].getElementById("contentWrapper")
                val newsContentBlock = contentWrapper.getElementById("content")
                val contentLeft = newsContentBlock.getElementsByClass("content-left")
                val scrollfix = contentLeft[0].getElementsByClass("js-scrollfix-bottom-rel")
                val newsListM = scrollfix[0].getElementsByClass("news-list mt16 mr8")
                val newslist = newsListM[0].getElementsByClass("news-unit clearfix rect")


                for ((index) in newslist.withIndex()) {

                    val a = newslist[index].getElementsByTag("a")
                    val imgUrl = a[0].getElementsByTag("img")[0].absUrl("src")
                    val first = imgUrl.substringBefore("r")
                    val second = imgUrl.substringAfter("/s/")
                    val imgFinal = first + "s/"+ second


                    val newsUnitRight = newslist[index].getElementsByClass("news-unit-right")

                    val titleClass = newsUnitRight[0].getElementsByClass("title")
                    val title = titleClass[0].getElementsByTag("a")[0].text()
                    val newsUrl = titleClass[0].getElementsByTag("a")[0].absUrl("href")
                    val description = newsUnitRight[0].getElementsByClass("text")[0].text()

                    val information = newsUnitRight[0].getElementsByClass("information")
                    val dateCreated = information[0].getElementsByClass("info di-ib")[0].text()

                    newsList.add(
                        NewsItem(
                            title = title,
                            img_url = imgFinal,
                            small_description = description,
                            date_created = dateCreated,
                            url = newsUrl
                        )
                    )

                }

                withContext(Dispatchers.Main) {
                    result.value = Resource.success(newsList)
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    result.value = Resource.error(e.message ?: "", null)
                }
            }
        }

        return result

    }



    fun getLatestPromotional(): MutableLiveData<Resource<MutableList<PromotionalItem>>> {

        val result = MutableLiveData<Resource<MutableList<PromotionalItem>>>()
        result.value = Resource.loading(null)
        val newsList = mutableListOf<PromotionalItem>()
        GlobalScope.launch{
            try {
                val doc = Jsoup.connect("https://myanimelist.net/watch/promotion").get()

                val myanimelist = doc.getElementById("myanimelist")
                val wrapper = myanimelist.getElementsByClass("wrapper")
                val contentWrapper = wrapper[0].getElementById("contentWrapper")
                val newsContentBlock = contentWrapper.getElementById("content")
                val clearFix = newsContentBlock.getElementsByClass("watch-anime-list watch-video ml12 clearfix")
                val videoBlock = clearFix[0].getElementsByClass("video-block")
                val videoList = videoBlock[0].getElementsByClass("video-list-outer-vertical")

                for ((index) in videoList.withIndex()) {

                    val ytLink = videoList[index].getElementsByTag("a")[0].absUrl("href")
                    val dataTitle = videoList[index].getElementsByTag("a")[0].absUrl("data-title")
                    val ytId = ytLink.substringAfter("embed/").substringBefore("?enable")
                    val baseUrl = "http://i.ytimg.com/vi/"
                    val baseUrlEnd = "/hqdefault.jpg"
                    val imgUrl = baseUrl + ytId + baseUrlEnd
                    val description = dataTitle.substringAfter("watch/")

                    val videoInfo = videoList[index].getElementsByClass("video-info-title")
                    val title = videoInfo[0].getElementsByClass("mr4")[0].text()

                    newsList.add(
                        PromotionalItem(
                            title = title,
                            img_url = imgUrl,
                            small_description = description,
                            url = ytLink
                        )
                    )

                }

                withContext(Dispatchers.Main) {
                    result.value = Resource.success(newsList)
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    result.value = Resource.error(e.message ?: "", null)
                }
            }
        }

        return result

    }


}