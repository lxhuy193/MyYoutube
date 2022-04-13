package com.example.myyoutube.Network

import com.example.myyoutube.Data.*
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface YoutubeEndpoints {
    @GET("videos")
    fun getTrendding(
        @Query("part") part: String,
        @Query("maxResults") maxResults: Int,
        @Query("chart") chart: String,
        @Query("key") key: String,
        @Query("regionCode") regionCode: String,
        @Query("videoCategoryId") videoCategoryId: Int
    ): Call<TrendFeed>

    @GET("videos")
    fun getVideoDetail(
        @Query("part") part: String,
        @Query("id") id: String,
        @Query("key") key: String
    ): Call<TrendFeed>

    @GET("videos")
    fun getVideoViewLikeCmt(
        @Query("part") part: String,
        @Query("id") id: String,
        @Query("key") key: String
    ):Call<VideoPlayerData>

    @GET("channels")
    fun getChannel(
        @Query("part") part: String,
        @Query("key") key: String,
        @Query("id") id: String
    ): Call<ChannelDetail>

    @GET("channels")
    fun getChannelStatistics(
        @Query("part") part: String,
        @Query("id") id: String,
        @Query("key") key: String
    ): Call<ChannelStatistics>

    @GET("search")
    fun getRelatedVideo(
        @Query("part") part: String,
        @Query("relatedToVideoId") relatedToVideoId : String,
        @Query("type") type : String,
        @Query("maxResults") maxResults : String,
        @Query("key") key: String
    ): Call<TrendFeed>
}