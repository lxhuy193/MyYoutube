package com.example.myyoutube.Network

import com.example.myyoutube.Data.ChannelDetail
import com.example.myyoutube.Data.TrendFeed
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface YoutubeEndpoints {
    @GET("videos")
    fun getTrendding (@Query("part") part: String,
                      @Query("maxResults") maxResults: Int,
                      @Query("chart") chart: String,
                      @Query("key") key: String,
                      @Query("regionCode") regionCode: String,
                      @Query("videoCategoryId") videoCategoryId: Int
    ): Call<TrendFeed>

    @GET("channels")
    fun getChannel (@Query("part") part: String,
                    @Query("key") key : String,
                    @Query("id") id: String
    ): Call<ChannelDetail>
}