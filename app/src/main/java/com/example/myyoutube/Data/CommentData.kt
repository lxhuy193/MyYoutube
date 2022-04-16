package com.example.myyoutube.Data

data class CommentData(
    val kind : String,
    val etag : String,
    val nextPageToken : String,
    val items : List<CommentItem>
)

data class CommentItem(
    val kind : String,
    val etag : String,
    val id : String,
    val snippet : CommentSnippet
)

data class CommentSnippet(
    val videoId : String,
    val topLevelComment : CmtTopLevelComment,
    val canReply : Boolean,
    val totalReplyCount : Int,
    val isPublic : Boolean
)

data class CmtTopLevelComment(
    val kind : String,
    val etag: String,
    val id: String,
    val snippet: CommentSnippet2
)

data class CommentSnippet2(
    val videoId : String,
    val textDisplay : String,
    val textOriginal : String,
    val authorDisplayName : String,
    val authorProfileImageUrl : String,
    val authorChannelUrl : String,
    val authorChannelId : AuthorChannelId,
    val canRate : Boolean,
    val likeCount : String,
    val publishedAt : String,
    val updatedAt : String
)

data class AuthorChannelId(
    val value : String
)


