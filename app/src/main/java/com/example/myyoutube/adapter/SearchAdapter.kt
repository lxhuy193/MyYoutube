package com.example.myyoutube.adapter

import android.content.Context
import android.content.Intent
import android.util.Log
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
import com.example.myyoutube.newpipeExtracter.ExtractorHelper
import de.hdodenhof.circleimageview.CircleImageView
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import org.schabi.newpipe.extractor.InfoItem
import org.schabi.newpipe.extractor.playlist.PlaylistInfo
import org.schabi.newpipe.extractor.search.SearchInfo
import org.schabi.newpipe.extractor.stream.StreamInfo
import org.schabi.newpipe.extractor.stream.StreamInfoItem
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class SearchAdapter(val searchItem: List<InfoItem>, val context: Context) :
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
        val item = searchItem[position] as StreamInfoItem

        Glide.with(holder.itemView.context).load(searchItem[position].thumbnailUrl).into(holder.iv_thumbNail)
        Glide.with(holder.itemView.context).load(item.uploaderUrl).into(holder.civ_channelImage)
        holder.tv_videoTitle.text = searchItem[position].name
        holder.tv_channelTitle.text = item.uploaderName

        //get VideoId + videoView + channelThumbnail + channelName
        val videoId = searchItem[position].url.substringAfterLast("=")
        val videoView = item.viewCount
        val videoDate = item.textualUploadDate
        val videoTitle = item.name
        val channelThumbnail = item.uploaderAvatarUrl
        val channelName = item.uploaderName


        holder.itemView.setOnClickListener { v: View ->
            Unit
            //short description used for demo description before expand
//            Log.i("heyhey", searchItem[position].shortDescription)

            val intent = Intent(context, PlayerActivity::class.java)
            intent.putExtra("videoId", videoId)
            intent.putExtra("videoView", videoView)
            intent.putExtra("videoTitle", videoTitle)
            intent.putExtra("videoDate", videoDate)
            intent.putExtra("channelThumbnail", channelThumbnail)
            intent.putExtra("channelName", channelName)

            ExtractorHelper.getStreamInfo(0, searchItem[position].url, false)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ result: StreamInfo ->
                    Log.i("huyhuy ", result.description.content)
                    intent.putExtra("videoDescrip", result.description.content)
                    intent.putExtra("videoLike", result.likeCount.toString())

                    context.startActivity(intent)

                }) { throwable: Throwable ->
                    println("erorrrr call description failed")
                }

            ExtractorHelper.getPlaylistInfo(0, searchItem[position].url, false)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ results: PlaylistInfo ->
                    Log.i("huyhuy2 ", results.relatedItems[0].name)
//                    intent.putExtra("videoDescrip", result.description.content)
//                    intent.putExtra("videoLike", result.likeCount.toString())
//                    context.startActivity(intent)
                }) { throwable: Throwable ->
                    println("erorrrr call playlist failed")
                }

        }
//        //channel thumbnail + channel title
//        val request = ServiceBuilder.buildService(YoutubeEndpoints::class.java)
//        val call = request.getChannel(
//            "snippet",
//            "AIzaSyAGPiwZJTlrJqeG5bET8YDEiCJ8zCJCQ_A",
//            searchItem[position].snippet.channelId
//        )
//        call.enqueue(object : Callback<ChannelDetail> {
//            override fun onResponse(
//                call: Call<ChannelDetail>,
//                response: Response<ChannelDetail>
//            ) {
//                if (response.isSuccessful) {
//                    val body = response.body()
//                    Glide.with(holder.itemView.context)
//                        .load(body!!.items[0].snippet.thumbnails.high.url)
//                        .into(holder.civ_channelImage)
//                    holder.tv_channelTitle.text = body!!.items[0].snippet.title
//                }
//            }
//
//            override fun onFailure(call: Call<ChannelDetail>, t: Throwable) {
//                println("aaaaa" + t)
//            }
//
//        })
//
//        //video title + video thumbnail main activity
//        Glide.with(holder.itemView.context).load(searchItem[position].snippet.thumbnails.high.url)
//            .into(holder.iv_thumbNail)
//        holder.tv_videoTitle.text = searchItem[position].snippet.title
//
//        //intent pass video title + channel thumbnail + channel title
//        holder.itemView.setOnClickListener { v: View ->
//            Unit
//            val intent = Intent(context, PlayerActivity::class.java)
//            //video id for youtube player
//            intent.putExtra("videoId", searchItem[position].id.videoId)
//            intent.putExtra("videoTitle", searchItem[position].snippet.title)
//
//            //call channelThumbnail, channelTitle, channelSubscribes to put Intent
//            val requestItent = ServiceBuilder.buildService(YoutubeEndpoints::class.java)
//            val callItent = requestItent.getChannel(
//                "snippet",
//                "AIzaSyAGPiwZJTlrJqeG5bET8YDEiCJ8zCJCQ_A",
//                searchItem[position].snippet.channelId
//            )
//
//            callItent.enqueue(object : Callback<ChannelDetail> {
//
//                override fun onResponse(
//                    call: Call<ChannelDetail>,
//                    response: Response<ChannelDetail>
//                ) {
//                    if (response.isSuccessful) {
//                        val body = response.body()
//                        intent.putExtra(
//                            "channelThumbnail",
//                            body!!.items[0].snippet.thumbnails.high.url
//                        )
//                        intent.putExtra(
//                            "channelTitle",
//                            body!!.items[0].snippet.title
//                        )
//                        intent.putExtra(
//                            "channelId",
//                            body!!.items[0].id
//                        )
//
//                        context.startActivity(intent)
//                    }
//                }
//
//                override fun onFailure(call: Call<ChannelDetail>, t: Throwable) {
//                    println("aaaaa" + t)
//                }
//
//            })
//
//        }

    }

    override fun getItemCount(): Int {
        return searchItem.size
    }

}