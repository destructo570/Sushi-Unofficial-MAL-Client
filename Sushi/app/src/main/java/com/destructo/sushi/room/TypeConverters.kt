package com.destructo.sushi.room

import androidx.room.TypeConverter
import com.destructo.sushi.model.jikan.MALEntity
import com.destructo.sushi.model.jikan.anime.core.AnimeCharacterAndStaff
import com.destructo.sushi.model.jikan.anime.core.AnimeReviews
import com.destructo.sushi.model.jikan.anime.core.AnimeVideo
import com.destructo.sushi.model.jikan.manga.MangaReview
import com.destructo.sushi.model.jikan.manga.character.MangaCharacter
import com.destructo.sushi.model.mal.anime.Anime
import com.destructo.sushi.model.mal.manga.Manga
import com.destructo.sushi.model.mal.userAnimeList.AnimeListStatus
import com.destructo.sushi.model.mal.userInfo.UserInfo
import com.destructo.sushi.model.mal.userMangaList.MangaListStatus
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
    fun stringToAnimeListStatus(data: String?): AnimeListStatus?{
        if(data == null) return null

        val type: Type = object: TypeToken<AnimeListStatus?>(){}.type
        return  gson.fromJson(data, type)
    }

    @TypeConverter
    fun animeListStatusToString(data: AnimeListStatus?): String?{
        return gson.toJson(data)
    }

    @TypeConverter
    fun stringToMangaListStatus(data: String?): MangaListStatus?{
        if(data == null) return null

        val type: Type = object: TypeToken<MangaListStatus?>(){}.type
        return  gson.fromJson(data, type)
    }

    @TypeConverter
    fun mangaListStatusToString(data: MangaListStatus?): String?{
        return gson.toJson(data)
    }

    @TypeConverter
    fun stringToUserInfo(data: String?): UserInfo?{
        if(data == null) return null

        val type: Type = object: TypeToken<UserInfo?>(){}.type
        return  gson.fromJson(data, type)
    }

    @TypeConverter
    fun userInfoToString(data: UserInfo?): String?{
        return gson.toJson(data)
    }

    @TypeConverter
    fun stringToListOfString(data: String?): List<String?>?{
        if(data == null) return null

        val type: Type = object: TypeToken<List<String?>?>(){}.type
        return  gson.fromJson(data, type)
    }

    @TypeConverter
    fun listOfStringToString(data: List<String?>?): String?{
        return gson.toJson(data)
    }

    @TypeConverter
    fun stringToListOfMalEntity(data: String?): List<MALEntity?>?{
        if(data == null) return null

        val type: Type = object: TypeToken<List<MALEntity?>?>(){}.type
        return  gson.fromJson(data, type)
    }

    @TypeConverter
    fun listOfMalEntityToString(data: List<MALEntity?>?): String?{
        return gson.toJson(data)
    }

}