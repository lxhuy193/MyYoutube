package com.example.myyoutube.Data


data class TrendFeed(
    var kind: String,
    var etag: String,
    var items: List<Item>,
    var nextPageToken: String,
    var pageInfo: PageInfo
)

data class PageInfo(
    val totalResults: Int,
    val resultsPerPage: Int
)

data class Item(
    var kind: String,
    var etag: String,
    var id: String,
    var snippet: Snippet
)

data class Snippet(
    var publishedAt: String,
    var channelId: String,
    var title: String,
    var description: String,
    var thumbnails: Thumbnail,
    var channelTitle: String,
    var tags: List<String>,
    var categoryId: String,
    var liveBroadcastContent: String,
    var localized: Localized,
    var defaultAudioLanguage: String
)

data class Localized(
    val title: String,
    val description: String
)

data class Thumbnail(
    val default: Default,
    val medium: Medium,
    val high: High,
    val standard: Standard,
    val maxres: Maxres
)

data class Default(
    val url: String,
    val width: Int, val height: Int
)

data class Medium(
    val url: String,
    val width: Int,
    val height: Int
)

data class High(
    val url: String,
    val width: Int,
    val height: Int
)

data class Standard(
    val url: String,
    val width: Int,
    val height: Int
)

data class Maxres(
    val url: String,
    val width: Int,
    val height: Int
)

