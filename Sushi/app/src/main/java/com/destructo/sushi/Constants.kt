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
const val INVALID_TOKEN = "eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiIsImp0aSI6IjBhY2ViYWQyZDRhYjk3YTllMTQ2MDJmYzJjNzM1Zjg2NGZlZjc3NmQ5ZGJkZjkxZDEzMzY5ZTU2MGNjZjZlYTQ0NmY5Y2ZkMjEzMDQzMzU3In0.eyJhdWQiOiJiMzY4ODJmNWFiYmYyNWMxNTc1NzBjOTIxZTk0NmIwOCIsImp0aSI6IjBhY2ViYWQyZDRhYjk3YTllMTQ2MDJmYzJjNzM1Zjg2NGZlZjc3NmQ5ZGJkZjkxZDEzMzY5ZTU2MGNjZjZlYTQ0NmY5Y2ZkMjEzMDQzMzU3IiwiaWF0IjoxNjAxNDAwMjUxLCJuYmYiOjE2MDE0MDAyNTEsImV4cCI6MTYwMzk5MjI1MSwic3ViIjoiMTAzNTcyODMiLCJzY29wZXMiOltdfQ.FtS95L5LFSNrHykU84i8smvwFfTzNXPRAgwalp3-xQCur5-GuPI2fb8Euon5bxqKKAX388wD2qwhK3K3VvRBoPDQxSWhkNmzUO_eD2F8UQdo_MhmgoEiX-fsNaPI6FUYzR55V-J-0Q_5z4XI4rGgZPOVmVCU4CuULgtHkXMXJOYG_5FaCT7x702CrXjUSw-chOp7saO_43zSGfNVOPWoAv2lnJbRdnEStHO3WcaoK9dZ2qDO_Ih-OvI-LQw4Z0KnFiml4afbZfA3d43zuBuwByg9r1O-Y3nTQ8yrCqR33EnChUffHbN0FD1HYRylM3Xc3P_CUovtcATptkKj2YLl5g"

const val ALL_ANIME_FIELDS ="id,title,main_picture,alternative_titles,start_date,end_date,synopsis,mean,rank,popularity,num_list_users,num_scoring_users,nsfw,created_at,updated_at,media_type,status,genres,my_list_status,num_episodes,start_season,broadcast,source,average_episode_duration,rating,pictures,background,related_anime,related_manga,recommendations,studios,statistics,my_list_status"
const val BASIC_ANIME_FIELDS ="id,title,main_picture,mean,media_type,rank,nsfw"
const val ALL_MANGA_FIELDS = "id,title,main_picture,alternative_titles,start_date,end_date,synopsis,mean,rank,popularity,num_list_users,num_scoring_users,nsfw,created_at,updated_at,media_type,status,genres,my_list_status,num_volumes,num_chapters,authors{first_name,last_name},pictures,background,related_anime,related_manga,recommendations,serialization{name}"
const val BASIC_MANGA_FIELDS ="id,title,main_picture,mean,rank,media_type,nsfw"

const val USER_INFO_FIELDS = "anime_statistics,time_zone,is_supporter"

const val DEFAULT_PAGE_LIMIT = "60"
const val DEFAULT_USER_LIST_PAGE_LIMIT = "10"