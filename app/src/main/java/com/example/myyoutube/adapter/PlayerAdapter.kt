package com.example.myyoutube.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.myyoutube.Data.*
import com.example.myyoutube.Network.ServiceBuilder
import com.example.myyoutube.Network.YoutubeEndpoints
import com.example.myyoutube.activity.PlayerActivity
import com.example.myyoutube.R
import de.hdodenhof.circleimageview.CircleImageView
import org.schabi.newpipe.extractor.InfoItem
import org.schabi.newpipe.extractor.stream.StreamInfoItem
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PlayerAdapter(val relatedItem: List<InfoItem>, val context: Context) :
    RecyclerView.Adapter<PlayerAdapter.VH>() {
    inner class VH(view: View) : RecyclerView.ViewHolder(view) {
        val iv_videoThumbnailPlayerItem =
            view.findViewById<ImageView>(R.id.iv_videoThumbnailPlayerItem)
        val civ_channelImagePlayerItem =
            view.findViewById<CircleImageView>(R.id.civ_channelImagePlayerItem)
        val tv_videoTitlePlayerItem = view.findViewById<TextView>(R.id.tv_videoTitlePlayerItem)
        val tv_channelTitlePlayerItem = view.findViewById<TextView>(R.id.tv_channelTitlePlayerItem)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        return VH(LayoutInflater.from(parent.context).inflate(R.layout.item_related, parent, false))
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val item = relatedItem[position] as StreamInfoItem

        // video thumbnail + video title
        Glide.with(holder.itemView.context)
            .load(item.thumbnailUrl)
            .into(holder.iv_videoThumbnailPlayerItem)
        holder.tv_videoTitlePlayerItem.text = item.name

        // channel thumbnail + channel title
        Glide.with(holder.itemView.context)
            .load(item.uploaderAvatarUrl)
            .into(holder.civ_channelImagePlayerItem)
        holder.tv_channelTitlePlayerItem.text = item.uploaderName

        /*
        ITEM CLICK LISTENER
         */
//        holder.itemView.setOnClickListener { v : View ->
//            Unit
//            val intent = Intent(context, PlayerActivity::class.java)
//            intent.putExtra("videoPlayerUrl", item.url)
//            context.startActivity(intent)
//
//        }
    }

    override fun getItemCount(): Int {
        return relatedItem.size
    }
}