package com.example.myyoutube.Data

data class VideoPlayerData(
    val kind: String,
    val etag: String,
    val items : List<VideoViewLikeCmt>
    )

data class VideoViewLikeCmt(
    val kind: String,
    val etag: String,
    val id : String,
    val statistics : StatisticsViewLikeCmt
)

data class StatisticsViewLikeCmt(
    val viewCount : String,
    val likeCount : String,
    val commentCount : String
)