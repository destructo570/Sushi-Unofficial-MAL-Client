package com.destructo.sushi


//Jikan Constants

const val BASE_JIKAN_URL = "https://api.jikan.moe/v3/"

//MyAnimeList Constants

const val BASE_MAL_AUTH_URL = "https://myanimelist.net/"
const val BASE_MAL_API_URL = "https://api.myanimelist.net/v2/"

const val REDIRECT_URL ="com.destructo.sushi://oauth"
const val AUTH_CODE_URL = "https://myanimelist.net/v1/oauth2/authorize"
const val AC_GRANT_CODE = "authorization_code"
const val AR_GRANT_CODE = "refresh_token"

const val ALL_ANIME_FIELDS ="id,title,main_picture,alternative_titles,start_date,end_date,synopsis,mean,rank,popularity,num_list_users,num_scoring_users,nsfw,created_at,updated_at,media_type,status,genres,my_list_status{start_date,finish_date},num_episodes,start_season,broadcast,source,average_episode_duration,rating,pictures,background,related_anime,related_manga,recommendations,studios,statistics"
const val BASIC_ANIME_FIELDS ="id,title,main_picture,mean,media_type,rank,nsfw"
const val ALL_MANGA_FIELDS = "id,title,main_picture,alternative_titles,start_date,end_date,synopsis,mean,rank,popularity,num_list_users,num_scoring_users,nsfw,created_at,updated_at,media_type,status,genres,my_list_status{start_date,finish_date},num_volumes,num_chapters,authors{first_name,last_name},pictures,background,related_anime,related_manga,recommendations,serialization{name}"
const val BASIC_MANGA_FIELDS ="id,title,main_picture,mean,rank,media_type,nsfw"

const val USER_INFO_FIELDS = "anime_statistics,time_zone,is_supporter"

const val DEFAULT_PAGE_LIMIT = "60"
const val DEFAULT_USER_LIST_PAGE_LIMIT = "20"
const val DETAILS_CACHE_EXPIRE_TIME_LIMIT = 120000
const val CACHE_EXPIRE_TIME_LIMIT = 60000

//Shared Pref Key
const val CURRENT_THEME = "current_theme"
const val NSFW_TAG = "nsfw_tag"
const val IS_PRO_USER = "is_pro_user"

//ProductId
const val PRODUCT_ID = "sushi_pro_key"
const val DONATE_COOKIE_ID = "donate_cookie"
const val DONATE_COKE_ID = "donate_coke"
const val DONATE_COFFEE_ID = "donate_coffee"
const val DONATE_BEER_ID = "donate_beer"
const val DONATE_LUNCH_ID = "donate_lunch"
const val DONATE_GIFT_ID = "donate_gift"

const val PREF_ID_THEME = "selected_theme"


const val NSFW_WHITE = "white"

//1 Day in milliseconds = 86400000
//5 Days in milliseconds = 432000000
const val REFRESH_TOKEN_LIMIT = 432000000

const val MANGA_ID_ARG = "mangaId"
const val ANIME_ID_ARG = "animeId"
const val CHARACTER_ID_ARG = "characterId"
const val PERSON_ID_ARG = "personId"
const val BOARD_ID_ARG = "boardId"
const val MAL_ID_ARG = "malId"
const val USERNAME_ARG = "username"
const val STATUS_ARG = "status"
const val CATEGORY_ARG = "category"
const val FAV_ARG = "favorites"

const val BASE_MAL_ANIME_URL = "https://myanimelist.net/anime/"
const val BASE_MAL_MANGA_URL = "https://myanimelist.net/manga/"
const val BASE_MAL_CHARACTER_URL = "https://myanimelist.net/character/"
const val BASE_MAL_PEOPLE_URL = "https://myanimelist.net/people/"

const val LIST_SPACE_HEIGHT = 26
const val FIRST_LIST_ITEM_SPACE_HEIGHT = 32






