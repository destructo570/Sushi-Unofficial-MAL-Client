package com.destructo.sushi.room

import androidx.room.TypeConverter
import com.destructo.sushi.model.jikan.anime.core.AnimeCharacterAndStaff
import com.destructo.sushi.model.jikan.anime.core.AnimeReviews
import com.destructo.sushi.model.jikan.anime.core.AnimeVideo
import com.destructo.sushi.model.jikan.manga.MangaReview
import com.destructo.sushi.model.jikan.manga.character.MangaCharacter
import com.destructo.sushi.model.mal.anime.*
import com.destructo.sushi.model.mal.animeRanking.AnimeRankingData
import com.destructo.sushi.model.mal.common.Genre
import com.destructo.sushi.model.mal.common.MainPicture
import com.destructo.sushi.model.mal.common.Picture
import com.destructo.sushi.model.mal.common.Ranking
import com.destructo.sushi.model.mal.manga.Manga
import com.destructo.sushi.model.mal.manga.RelatedManga
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type

class TypeConverters {

    private val gson = Gson()

    @TypeConverter
    fun stringToAnime(data: String?): Anime?{
        if(data == null) return null

        val type: Type = object: TypeToken<Anime?>(){}.type
        return  gson.fromJson(data, type)
    }

    @TypeConverter
    fun animeToString(data: Anime?): String?{
        return gson.toJson(data)
    }

    @TypeConverter
    fun stringToAnimeCharacterAndStaff(data: String?): AnimeCharacterAndStaff?{
        if(data == null) return null

        val type: Type = object: TypeToken<AnimeCharacterAndStaff?>(){}.type
        return  gson.fromJson(data, type)
    }

    @TypeConverter
    fun animeCharacterAndStaffToString(data: AnimeCharacterAndStaff?): String?{
        return gson.toJson(data)
    }

    @TypeConverter
    fun stringToAnimeVideoAndEpisodes(data: String?): AnimeVideo?{
        if(data == null) return null

        val type: Type = object: TypeToken<AnimeVideo?>(){}.type
        return  gson.fromJson(data, type)
    }

    @TypeConverter
    fun animeVideoAndEpisodesToString(data: AnimeVideo?): String?{
        return gson.toJson(data)
    }

    @TypeConverter
    fun stringToAnimeReviews(data: String?): AnimeReviews?{
        if(data == null) return null

        val type: Type = object: TypeToken<AnimeReviews?>(){}.type
        return  gson.fromJson(data, type)
    }

    @TypeConverter
    fun animeReviewsToString(data: AnimeReviews?): String?{
        return gson.toJson(data)
    }

    @TypeConverter
    fun stringToMangaDetails(data: String?): Manga?{
        if(data == null) return null

        val type: Type = object: TypeToken<Manga?>(){}.type
        return  gson.fromJson(data, type)
    }

    @TypeConverter
    fun mangaDetailsToString(data: Manga?): String?{
        return gson.toJson(data)
    }

    @TypeConverter
    fun stringToMangaCharacterList(data: String?): MangaCharacter?{
        if(data == null) return null

        val type: Type = object: TypeToken<MangaCharacter?>(){}.type
        return  gson.fromJson(data, type)
    }

    @TypeConverter
    fun mangaCharacterListToString(data: MangaCharacter?): String?{
        return gson.toJson(data)
    }

    @TypeConverter
    fun stringToMangaReviewList(data: String?): MangaReview?{
        if(data == null) return null

        val type: Type = object: TypeToken<MangaReview?>(){}.type
        return  gson.fromJson(data, type)
    }

    @TypeConverter
    fun mangaReviewListToString(data: MangaReview?): String?{
        return gson.toJson(data)
    }

    @TypeConverter
    fun stringToRankingist(data: String?): Ranking?{
        if(data == null) return null

        val type: Type = object: TypeToken<Ranking?>(){}.type
        return  gson.fromJson(data, type)
    }

    @TypeConverter
    fun rankingListToString(data: Ranking?): String?{
        return gson.toJson(data)
    }

    @TypeConverter
    fun stringToAnimeRankingData(data: String?): AnimeRankingData?{
        if(data == null) return null

        val type: Type = object: TypeToken<AnimeRankingData?>(){}.type
        return  gson.fromJson(data, type)
    }

    @TypeConverter
    fun animeRankingDataToString(data: AnimeRankingData?): String?{
        return gson.toJson(data)
    }

}