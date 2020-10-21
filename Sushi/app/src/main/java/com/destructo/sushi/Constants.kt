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
const val BASIC_ANIME_FIELDS ="id,title,main_picture"
const val ALL_MANGA_FIELDS = "id,title,main_picture,alternative_titles,start_date,end_date,synopsis,mean,rank,popularity,num_list_users,num_scoring_users,nsfw,created_at,updated_at,media_type,status,genres,my_list_status,num_volumes,num_chapters,authors{first_name,last_name},pictures,background,related_anime,related_manga,recommendations,serialization{name}"
const val BASIC_MANGA_FIELDS ="id,title,main_picture"

const val DEBUG_ACCESS_TOKEN = "eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiIsImp0aSI6ImQ3YTdkMjgyNTVlMmFkNWVmNTIyY2EzMTIxMzJlMDUxOGQ4MjI5YmJmNzRiNWNhMGMxOTYyZTc0NzFkYjYwMzAwYzViYTRkNmY2M2YzNDc3In0.eyJhdWQiOiJiMzY4ODJmNWFiYmYyNWMxNTc1NzBjOTIxZTk0NmIwOCIsImp0aSI6ImQ3YTdkMjgyNTVlMmFkNWVmNTIyY2EzMTIxMzJlMDUxOGQ4MjI5YmJmNzRiNWNhMGMxOTYyZTc0NzFkYjYwMzAwYzViYTRkNmY2M2YzNDc3IiwiaWF0IjoxNjAyNzc1Nzc4LCJuYmYiOjE2MDI3NzU3NzgsImV4cCI6MTYwNTQ1Nzc3OCwic3ViIjoiMTAzNTcyODMiLCJzY29wZXMiOltdfQ.AGn7D5JYyzasXny0XdG-KxXe-OBO3I2MLNmrH4sk8LWas0XEHZ9tsXuxhGBF-i4AKBBNIcnoAU1-frTc7_MoJfFHX05f4ewbK2cQkDG0Dh5OufeMhuOyNJSz5BJM8nZqAnye52Y6ofu75rE_OsouTlSyg7beoMBlKQtalS31iKRJ5ww5pAG9hTQT-1tS7HqcFvKgxymSVmjRX7K7uhRWE5s9VxZdjZ4X41_9W9vCWoBnIfNMVnP5yjaV8GPiYAhvLrjg_-jxgpyChxMPm7Q7UmXguIqE1Tb0ccwC3zCNPhuONiZ_f7PJ4C-fLcDFu6D3r_VRiPcd3U8p7ie01PhmSA"

const val USER_INFO_FIELDS = "anime_statistics,time_zone,is_supporter"