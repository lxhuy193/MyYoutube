package com.example.myyoutube

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.myyoutube.Data.CommentItem
import de.hdodenhof.circleimageview.CircleImageView

class CommentAdapter(val commentItems: List<CommentItem>, val context: Context) :
    RecyclerView.Adapter<CommentAdapter.VH>() {
    inner class VH(view: View) : RecyclerView.ViewHolder(view) {
        val civ_userThumnail = view.findViewById<CircleImageView>(R.id.civ_userThumnail)
        val tv_userName = view.findViewById<TextView>(R.id.tv_userName)
        val tv_cmtContent = view.findViewById<TextView>(R.id.tv_cmtContent)
        val tv_likeCount = view.findViewById<TextView>(R.id.tv_likeCount)
        val tv_cmtCount = view.findViewById<TextView>(R.id.tv_cmtCount)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentAdapter.VH {
        return VH(LayoutInflater.from(parent.context).inflate(R.layout.item_comment, parent, false))
    }

    override fun onBindViewHolder(holder: CommentAdapter.VH, position: Int) {
        Glide.with(holder.itemView.context)
            .load(commentItems[position].snippet.topLevelComment.snippet.authorChannelUrl)
            .into(holder.civ_userThumnail)
        holder.tv_userName.text =
            commentItems[position].snippet.topLevelComment.snippet.authorDisplayName
        holder.tv_likeCount.text = commentItems[position].snippet.topLevelComment.snippet.likeCount
        holder.tv_cmtCount.text = commentItems[position].snippet.totalReplyCount.toString()
        holder.tv_cmtContent.text = commentItems[position].snippet.topLevelComment.snippet.textDisplay

    }

    override fun getItemCount(): Int {
        return commentItems.size
    }

}