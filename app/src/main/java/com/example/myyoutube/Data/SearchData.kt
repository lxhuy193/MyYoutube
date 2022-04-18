package com.example.myyoutube.Data

data class SearchData(
    val kind: String,
    val etag: String,
    val items: List<SearchItem>
)

data class SearchItem(
    val kind : String,
    val etag: String,
    val id : SearchId,
    val snippet: SearchSnippet
)

data class SearchId(
    val kind : String,
    val videoId : String
)

data class SearchSnippet(
    val publishedAt : String,
    val channelId : String,
    val title : String,
    val description : String,
    val thumbnails : ChannelThumbnail,
    val channelTitle : String,
    val publishTime : String
)