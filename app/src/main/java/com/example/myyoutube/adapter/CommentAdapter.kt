package com.example.myyoutube.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.myyoutube.Data.CommentItem
import com.example.myyoutube.R
import de.hdodenhof.circleimageview.CircleImageView
import org.schabi.newpipe.extractor.ListInfo
import org.schabi.newpipe.extractor.comments.CommentsInfoItem

class CommentAdapter(val commentItems: List<CommentsInfoItem>, val context: Context) :
    RecyclerView.Adapter<CommentAdapter.VH>() {
    inner class VH(view: View) : RecyclerView.ViewHolder(view) {
        val civ_userThumnail = view.findViewById<CircleImageView>(R.id.civ_userThumnail)
        val tv_userName = view.findViewById<TextView>(R.id.tv_userName)
        val tv_cmtContent = view.findViewById<TextView>(R.id.tv_cmtContent)
        val tv_likeCount = view.findViewById<TextView>(R.id.tv_likeCount)
//        val tv_cmtCount = view.findViewById<TextView>(R.id.tv_cmtCount)
        val tv_cmtTime = view.findViewById<TextView>(R.id.tv_cmtTime)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        return VH(LayoutInflater.from(parent.context).inflate(R.layout.item_comment, parent, false))
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        Glide.with(holder.itemView.context)
            .load(commentItems[position].uploaderAvatarUrl)
            .into(holder.civ_userThumnail)
        holder.tv_userName.text =
            commentItems[position].uploaderName
        holder.tv_likeCount.text = commentItems[position].likeCount.toString()
//        holder.tv_cmtCount.text = commentItems[position].
        holder.tv_cmtContent.text = commentItems[position].commentText
        holder.tv_cmtTime.text = commentItems[position].uploadDate.toString()

    }

    override fun getItemCount(): Int {
        return commentItems.size
    }

}