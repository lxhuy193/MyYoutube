package com.example.myyoutube.Data

import android.graphics.pdf.PdfDocument


data class TrendFeed(
    var kind: String,
    var etag: String,
    var items: List<TrendItem>,
//    var nextPageToken: String,
//    var pageInfo: PdfDocument.PageInfo
)

//data class TrendPageInfo(
//    val totalResults: Int,
//    val resultsPerPage: Int
//)

data class TrendItem(
    var kind: String,
    var etag: String,
    var id: String,
    var snippet: TrendSnippet
)

data class TrendSnippet(
    var publishedAt: String,
    var channelId: String,
    var title: String,
    var description: String,
    var thumbnails: TrendThumbnail,
    var channelTitle: String,
//    var tags: List<String>,
    var categoryId: String,
    var liveBroadcastContent: String,
//    var localized: TrendLocalized,
//    var defaultAudioLanguage: String
)

data class TrendLocalized(
    val title: String,
    val description: String
)

data class TrendThumbnail(
    val default: TrendDefault,
    val medium: TrendMedium,
    val high: TrendHigh,
    val standard: TrendStandard,
    val maxres: TrendMaxres
)

data class TrendDefault(
    val url: String,
    val width: Int, val height: Int
)

data class TrendMedium(
    val url: String,
    val width: Int,
    val height: Int
)

data class TrendHigh(
    val url: String,
    val width: Int,
    val height: Int
)

data class TrendStandard(
    val url: String,
    val width: Int,
    val height: Int
)

data class TrendMaxres(
    val url: String,
    val width: Int,
    val height: Int
)

