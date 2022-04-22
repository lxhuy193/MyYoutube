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
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PlayerAdapter(val relatedItem: List<RelatedItem>, val context: Context) :
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
        if (relatedItem[position].snippet != null) {
            // video thumbnail + video title
            Glide.with(holder.itemView.context)
                .load(relatedItem[position].snippet.thumbnails.high.url)
                .into(holder.iv_videoThumbnailPlayerItem)
            holder.tv_videoTitlePlayerItem.text = relatedItem[position].snippet.title

            // channel thumbnail + channel title
            val request = ServiceBuilder.buildService(YoutubeEndpoints::class.java)
            val call = request.getChannel(
                "snippet",
                "AIzaSyAGPiwZJTlrJqeG5bET8YDEiCJ8zCJCQ_A",
                relatedItem[position].snippet.channelId
            )
            call.enqueue(object : Callback<ChannelDetail> {
                override fun onResponse(call: Call<ChannelDetail>, response: Response<ChannelDetail>) {
                    if (response.isSuccessful) {
                        Glide.with(holder.itemView.context)
                            .load(response.body()!!.items[0].snippet.thumbnails.high.url)
                            .into(holder.civ_channelImagePlayerItem)
                    }
                    holder.tv_channelTitlePlayerItem.text = response.body()!!.items[0].snippet.title
                }

                override fun onFailure(call: Call<ChannelDetail>, t: Throwable) {
                    println("bbbbbbbbbb" + t)
                }

            })

            //intent pass video title + channel thumbnail + channel title
            holder.itemView.setOnClickListener { v: View ->
                Unit
                val intent = Intent(context, PlayerActivity::class.java)
                //video id for youtube player
                intent.putExtra("videoId", relatedItem[position].id.videoId)
                intent.putExtra("videoTitle", relatedItem[position].snippet.title)

                //call channelThumbnail, channelTitle, channelSubscribes to put Intent
                val requestItent = ServiceBuilder.buildService(YoutubeEndpoints::class.java)
                val callItent = requestItent.getChannel(
                    "snippet",
                    "AIzaSyAGPiwZJTlrJqeG5bET8YDEiCJ8zCJCQ_A",
                    relatedItem[position].snippet.channelId
                )

                callItent.enqueue(object : Callback<ChannelDetail> {

                    override fun onResponse(
                        call: Call<ChannelDetail>,
                        response: Response<ChannelDetail>
                    ) {
                        if (response.isSuccessful) {
                            val body = response.body()
                            intent.putExtra(
                                "channelThumbnail",
                                body!!.items[0].snippet.thumbnails.high.url
                            )
                            intent.putExtra(
                                "channelTitle",
                                body!!.items[0].snippet.title
                            )
                            intent.putExtra(
                                "channelId",
                                body!!.items[0].id
                            )

                            context.startActivity(intent)
                        }
                    }

                    override fun onFailure(call: Call<ChannelDetail>, t: Throwable) {
                        println("aaaaa" + t)
                    }

                })

            }
        }

    }

    override fun getItemCount(): Int {
        return relatedItem.size
    }
}