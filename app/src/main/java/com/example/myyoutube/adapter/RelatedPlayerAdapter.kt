package com.example.myyoutube.adapter

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.myyoutube.activity.PlayerActivity
import com.example.myyoutube.R
import com.example.myyoutube.activity.MainActivity
import com.example.myyoutube.fragment.PlayerFragment
import org.schabi.newpipe.extractor.InfoItem
import org.schabi.newpipe.extractor.stream.StreamInfoItem

class RelatedPlayerAdapter(val relatedItem: List<InfoItem>, val context: Context) :
    RecyclerView.Adapter<RelatedPlayerAdapter.VH>() {
    companion object {
        var videoUrl: String? = null
    }

    inner class VH(view: View) : RecyclerView.ViewHolder(view) {
        val iv_videoThumbnailPlayerItem =
            view.findViewById<ImageView>(R.id.iv_videoThumbnailRelated)
//        val civ_channelImagePlayerItem =
//            view.findViewById<CircleImageView>(R.id.civ_channelImagePlayerItem)
        val tv_videoTitlePlayerItem = view.findViewById<TextView>(R.id.tv_videoTitleRelated)
        val tv_channelTitlePlayerItem = view.findViewById<TextView>(R.id.tv_channelTitleRelated)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        return VH(LayoutInflater.from(parent.context).inflate(R.layout.item_related, parent, false))
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val item = relatedItem[position] as StreamInfoItem

        // video thumbnail + video title
        holder.tv_videoTitlePlayerItem.text = item.name

        Glide.with(holder.itemView.context)
            .load(item.thumbnailUrl)
            .into(holder.iv_videoThumbnailPlayerItem)

        // channel thumbnail + channel title
        holder.tv_channelTitlePlayerItem.text = item.uploaderName

//        Glide.with(holder.itemView.context)
//            .load(item.uploaderAvatarUrl)
//            .into(holder.civ_channelImagePlayerItem)

        /*
        ITEM CLICK LISTENER
         */
        holder.itemView.setOnClickListener { v : View ->
            Unit
            videoUrl = relatedItem[position].url
            (context as MainActivity).supportFragmentManager.beginTransaction()
                .replace(R.id.container, PlayerFragment()).commit()

//            val intent = Intent(context, PlayerActivity::class.java)
//            intent.putExtra("videoUrl", item.url)
//            context.startActivity(intent)
//            (context as PlayerActivity).finish()
        }
    }

    override fun getItemCount(): Int {
        return relatedItem.size
    }
}