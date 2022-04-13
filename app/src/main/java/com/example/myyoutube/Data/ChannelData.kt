package com.example.myyoutube.Data

data class ChannelDetail(
    val kind: String,
    val etag: String,
    val items: List<ChannelItem>
)

data class ChannelItem(
    val kind: String,
    val etag: String,
    val id: String,
    val snippet: ChannelSnippet
)

data class ChannelSnippet(
    val title: String,
    val description: String,
    val publishedAt: String,
    val thumbnails: ChannelThumbnail,
    val localized: Localized,
    val country: String
)

data class Localized(
    val title: String,
    val description: String
)

data class ChannelThumbnail(
    val default: ChannelDefault,
    val medium: ChannelMedium,
    val high: ChannelHigh,
)

data class ChannelDefault(
    val url: String,
    val width: Int, val height: Int
)

data class ChannelMedium(
    val url: String,
    val width: Int,
    val height: Int
)

data class ChannelHigh(
    val url: String,
    val width: Int,
    val height: Int
)

///////////////////////////////////////////////////
data class ChannelStatistics(
    val kind: String,
    val etag: String,
    val items: List<ChannelSubscribe>
)

data class ChannelSubscribe(
    val kind: String,
    val etag: String,
    val id: String,
    val statistics: StatisticsChannel
)

data class StatisticsChannel(
    val viewCount : String,
    val subscriberCount : String,
    val videoCount : String
)
