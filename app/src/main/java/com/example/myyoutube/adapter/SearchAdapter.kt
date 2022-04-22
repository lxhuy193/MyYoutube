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
import com.example.myyoutube.Data.ChannelDetail
import com.example.myyoutube.Data.SearchItem
import com.example.myyoutube.Data.TrendItem
import com.example.myyoutube.Network.ServiceBuilder
import com.example.myyoutube.Network.YoutubeEndpoints
import com.example.myyoutube.activity.PlayerActivity
import com.example.myyoutube.R
import de.hdodenhof.circleimageview.CircleImageView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class SearchAdapter(val searchItem: List<SearchItem>, val context: Context) :
    RecyclerView.Adapter<SearchAdapter.VH>() {
    var onItemClick: ((TrendItem) -> Unit)? = null

    class VH(view: View) : RecyclerView.ViewHolder(view) {
        val iv_thumbNail = view.findViewById<ImageView>(R.id.iv_thumbnail)
        val tv_videoTitle = view.findViewById<TextView>(R.id.tv_videoTitle)
        val civ_channelImage = view.findViewById<CircleImageView>(R.id.civ_channelImage)
        val tv_channelTitle = view.findViewById<TextView>(R.id.tv_channelTitle)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        return VH(
            LayoutInflater.from(parent.context).inflate(R.layout.item_trending, parent, false)
        )
    }

    override fun onBindViewHolder(holder: VH, position: Int) {

        //channel thumbnail + channel title
        val request = ServiceBuilder.buildService(YoutubeEndpoints::class.java)
        val call = request.getChannel(
            "snippet",
            "AIzaSyAGPiwZJTlrJqeG5bET8YDEiCJ8zCJCQ_A",
            searchItem[position].snippet.channelId
        )
        call.enqueue(object : Callback<ChannelDetail> {
            override fun onResponse(
                call: Call<ChannelDetail>,
                response: Response<ChannelDetail>
            ) {
                if (response.isSuccessful) {
                    val body = response.body()
                    Glide.with(holder.itemView.context)
                        .load(body!!.items[0].snippet.thumbnails.high.url)
                        .into(holder.civ_channelImage)
                    holder.tv_channelTitle.text = body!!.items[0].snippet.title
                }
            }

            override fun onFailure(call: Call<ChannelDetail>, t: Throwable) {
                println("aaaaa" + t)
            }

        })

        //video title + video thumbnail main activity
        Glide.with(holder.itemView.context).load(searchItem[position].snippet.thumbnails.high.url)
            .into(holder.iv_thumbNail)
        holder.tv_videoTitle.text = searchItem[position].snippet.title

        //intent pass video title + channel thumbnail + channel title
        holder.itemView.setOnClickListener { v: View ->
            Unit
            val intent = Intent(context, PlayerActivity::class.java)
            //video id for youtube player
            intent.putExtra("videoId", searchItem[position].id.videoId)
            intent.putExtra("videoTitle", searchItem[position].snippet.title)

            //call channelThumbnail, channelTitle, channelSubscribes to put Intent
            val requestItent = ServiceBuilder.buildService(YoutubeEndpoints::class.java)
            val callItent = requestItent.getChannel(
                "snippet",
                "AIzaSyAGPiwZJTlrJqeG5bET8YDEiCJ8zCJCQ_A",
                searchItem[position].snippet.channelId
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

    override fun getItemCount(): Int {
        return searchItem.size
    }

}