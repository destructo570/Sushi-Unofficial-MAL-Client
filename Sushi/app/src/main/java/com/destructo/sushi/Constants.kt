package com.destructo.sushi


//Jikan Constants

const val BASE_JIKAN_URL = "https://api.jikan.moe/v3/"

//MyAnimeList Constants

const val BASE_MAL_AUTH_URL = "https://myanimelist.net/"
const val BASE_MAL_API_URL = "https://api.myanimelist.net/v2/"

const val CLIENT_ID = "b36882f5abbf25c157570c921e946b08"
const val REDIRECT_URL ="com.destructo.animeplay://oauth"
const val AUTH_CODE_URL = "https://myanimelist.net/v1/oauth2/authorize"
const val AC_GRANT_CODE = "authorization_code"
const val AR_GRANT_CODE = "refresh_token"
const val CODE_VERIFIER = "NklUDX_CzS8qrMGWaDzgKs6VqrinuVFHa0xnpWPDy7_fggtM6kAar4jnTwOgzK7nPYfE9n60rsY4fhDExWzr5bf7sEvMMmSXcT2hWkCstFGIJKoaimoq5GvAEQD8NZ8g"

const val ALL_ANIME_FIELDS ="id,title,main_picture,alternative_titles,start_date,end_date,synopsis,mean,rank,popularity,num_list_users,num_scoring_users,nsfw,created_at,updated_at,media_type,status,genres,my_list_status,num_episodes,start_season,broadcast,source,average_episode_duration,rating,pictures,background,related_anime,related_manga,recommendations,studios,statistics,my_list_status"
const val BASIC_ANIME_FIELDS ="id,title,main_picture,mean,media_type,rank,nsfw"
const val ALL_MANGA_FIELDS = "id,title,main_picture,alternative_titles,start_date,end_date,synopsis,mean,rank,popularity,num_list_users,num_scoring_users,nsfw,created_at,updated_at,media_type,status,genres,my_list_status,num_volumes,num_chapters,authors{first_name,last_name},pictures,background,related_anime,related_manga,recommendations,serialization{name}"
const val BASIC_MANGA_FIELDS ="id,title,main_picture,mean,rank,media_type,nsfw"

const val DEBUG_ACCESS_TOKEN = "eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiIsImp0aSI6Ijg0NzVlNzkwMjUzMzA0MWQ2MWI2YTI4MTllNGRkM2FjM2U4MzQ1NGIzM2EyY2I1YmM3NTg0ZDkyMWNjMDg4MTgwN2ZjZmQzOTYwNGQ5OWVkIn0.eyJhdWQiOiJiMzY4ODJmNWFiYmYyNWMxNTc1NzBjOTIxZTk0NmIwOCIsImp0aSI6Ijg0NzVlNzkwMjUzMzA0MWQ2MWI2YTI4MTllNGRkM2FjM2U4MzQ1NGIzM2EyY2I1YmM3NTg0ZDkyMWNjMDg4MTgwN2ZjZmQzOTYwNGQ5OWVkIiwiaWF0IjoxNjA0MzI3Mzg4LCJuYmYiOjE2MDQzMjczODgsImV4cCI6MTYwNjkxOTM4OCwic3ViIjoiMTAzNTcyODMiLCJzY29wZXMiOltdfQ.b8l-NQFbMAgZ0qc82mznKcH_w-zoZ6AxFmTR5SQNcXFi1JGW2rDevKdxwC4uzLu12_GGRo9CQUSW2z8hG70_LK_LVzuQppZLwMoX8ZLTevzMevBu-LenY9cHWFk3YYpNzZ085qQXKrJhLMqFTorzwKorjUcq3eBMSgstOhIsTYWCh_cU_IDQJW7DekY3041M3IqftLy6KmvEaSGEUDvodxivSFnd9YlP_JmzqzB2BEuIsnXcFIrbHTJq8XOxpDgv8fXN9AfZ3DSCIZKS6OsHkxwI8O_FxT1U4551NmyEOHthBd7qHdxus1Go0nIkbCk2aiwTT4zMrBbz_O2m46UUVg"

const val USER_INFO_FIELDS = "anime_statistics,time_zone,is_supporter"