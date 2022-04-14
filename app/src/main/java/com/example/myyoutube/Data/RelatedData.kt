package com.example.myyoutube.Data

data class RelatedData(
    val items: List<RelatedItem>
)

data class RelatedItem(
    val snippet: RelatedSnippet,
    val id: Id
)

data class Id(
    val videoId: String
)

data class RelatedSnippet(
    val publishedAt: String,
    val channelId: String,
    val title: String,
    val description: String,
    val thumbnails: TrendThumbnail,
    val channelTitle: String,
    val publishTime: String
)

