package com.destructo.sushi


//Jikan Constants

const val BASE_JIKAN_URL = "https://api.jikan.moe/v3/"

//MyAnimeList Constants

const val BASE_MAL_AUTH_URL = "https://myanimelist.net/"
const val BASE_MAL_API_URL = "https://api.myanimelist.net/v2/"

const val CLIENT_ID = "b36882f5abbf25c157570c921e946b08"
const val REDIRECT_URL ="com.destructo.sushi://oauth"
const val AUTH_CODE_URL = "https://myanimelist.net/v1/oauth2/authorize"
const val AC_GRANT_CODE = "authorization_code"
const val AR_GRANT_CODE = "refresh_token"

const val ALL_ANIME_FIELDS ="id,title,main_picture,alternative_titles,start_date,end_date,synopsis,mean,rank,popularity,num_list_users,num_scoring_users,nsfw,created_at,updated_at,media_type,status,genres,my_list_status,num_episodes,start_season,broadcast,source,average_episode_duration,rating,pictures,background,related_anime,related_manga,recommendations,studios,statistics,my_list_status"
const val BASIC_ANIME_FIELDS ="id,title,main_picture,mean,media_type,rank,nsfw"
const val ALL_MANGA_FIELDS = "id,title,main_picture,alternative_titles,start_date,end_date,synopsis,mean,rank,popularity,num_list_users,num_scoring_users,nsfw,created_at,updated_at,media_type,status,genres,my_list_status,num_volumes,num_chapters,authors{first_name,last_name},pictures,background,related_anime,related_manga,recommendations,serialization{name}"
const val BASIC_MANGA_FIELDS ="id,title,main_picture,mean,rank,media_type,nsfw"

const val USER_INFO_FIELDS = "anime_statistics,time_zone,is_supporter"

const val DEFAULT_PAGE_LIMIT = "60"
const val DEFAULT_USER_LIST_PAGE_LIMIT = "20"

//Shared Pref Key
const val CURRENT_THEME = "current_theme"
const val NSFW_TAG = "nsfw_tag"
const val IS_PRO_USER = "is_pro_user"

//ProductId
const val PRODUCT_ID = "sushi_pro_key"

const val NSFW_WHITE = "white"

//1 Days in milliseconds = 86400000
//5 Days in milliseconds = 432000000
const val REFRESH_TOKEN_LIMIT = 432000000







