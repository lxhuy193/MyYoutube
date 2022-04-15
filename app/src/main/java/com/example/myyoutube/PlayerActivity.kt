package com.example.myyoutube

import android.os.Bundle
import android.transition.AutoTransition
import android.transition.TransitionManager
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.myyoutube.Data.ChannelStatistics
import com.example.myyoutube.Data.RelatedData
import com.example.myyoutube.Data.TrendFeed
import com.example.myyoutube.Data.VideoPlayerData
import com.example.myyoutube.Network.ServiceBuilder
import com.example.myyoutube.Network.YoutubeEndpoints
import com.google.android.youtube.player.YouTubeBaseActivity
import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.android.youtube.player.YouTubePlayer
import com.google.android.youtube.player.YouTubePlayerView
import com.skydoves.expandablelayout.ExpandableLayout
import com.skydoves.expandablelayout.expandableLayout
import de.hdodenhof.circleimageview.CircleImageView
import org.w3c.dom.Text
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.regex.Pattern

class PlayerActivity : YouTubeBaseActivity() {
    lateinit var utubePlayer: YouTubePlayerView
    lateinit var tv_videoTitlePlayer: TextView
    lateinit var civ_channelIhumbnailPlayer: CircleImageView
    lateinit var tv_channelTitlePlayer: TextView
    lateinit var tv_videoDescripPlayer: TextView
    lateinit var tv_videoViewPlayer: TextView
    lateinit var tv_videoLikePlayer: TextView
    lateinit var tv_channelSubscribes: TextView
//    lateinit var expandableVideo: ExpandableLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player)
//        expandableVideo = findViewById(R.id.expandableVideo)
//        expandableVideo.expand()
//        expandableVideo.collapse()

        //get intent
        val intent = intent
        val videoId: String = intent.getStringExtra("videoId").toString()

        tv_videoTitlePlayer = findViewById(R.id.tv_videoTitlePlayer)
        tv_videoTitlePlayer.text = intent.getStringExtra("videoTitle")

        tv_channelTitlePlayer = findViewById(R.id.tv_channelTitlePlayer)
        tv_channelTitlePlayer.text = intent.getStringExtra("channelTitle")

        tv_channelSubscribes = findViewById(R.id.tv_channelSubscribes)
        val channelId = intent.getStringExtra("channelId").toString()

        civ_channelIhumbnailPlayer = findViewById(R.id.civ_channelThumbnailPlayer)
        Glide.with(this).load(intent.getStringExtra("channelThumbnail"))
            .into(civ_channelIhumbnailPlayer)

        //call video description describ by ID
        tv_videoDescripPlayer = findViewById(R.id.tv_videoDescripPlayer)
        val requestDescrip = ServiceBuilder.buildService(YoutubeEndpoints::class.java)
        val callDescrip = requestDescrip.getVideoDetail(
            "snippet",
            videoId,
            "AIzaSyD9pbOdvK1sevSbG7GXmIRfwMmiHm4J23U"
        )
        callDescrip.enqueue(object : Callback<TrendFeed> {
            override fun onResponse(call: Call<TrendFeed>, response: Response<TrendFeed>) {
                if (response.isSuccessful) {
                    tv_videoDescripPlayer.text = response.body()!!.items[0].snippet.description
                }
            }

            override fun onFailure(call: Call<TrendFeed>, t: Throwable) {
                Toast.makeText(this@PlayerActivity, "Fail", Toast.LENGTH_SHORT).show()
            }

        }
        )

        //call channel subscribers by channelId
        val requestSubs = ServiceBuilder.buildService(YoutubeEndpoints::class.java)
        val callSubs = requestSubs.getChannelStatistics(
            "statistics",
            channelId,
            "AIzaSyD9pbOdvK1sevSbG7GXmIRfwMmiHm4J23U"
        )
        callSubs.enqueue(object : Callback<ChannelStatistics> {
            override fun onResponse(
                call: Call<ChannelStatistics>,
                response: Response<ChannelStatistics>
            ) {
                tv_channelSubscribes.text =
                    response.body()!!.items[0].statistics.subscriberCount + " Subscribers"
            }

            override fun onFailure(call: Call<ChannelStatistics>, t: Throwable) {
                Toast.makeText(this@PlayerActivity, "Fail", Toast.LENGTH_SHORT).show()
            }

        })

        //call video views likes by ID
        tv_videoViewPlayer = findViewById(R.id.tv_videoViewPlayer)
        tv_videoLikePlayer = findViewById(R.id.tv_videoLikePlayer)
        val requestViewLike = ServiceBuilder.buildService(YoutubeEndpoints::class.java)
        val callViewLike = requestViewLike.getVideoViewLikeCmt(
            "statistics",
            videoId,
            "AIzaSyD9pbOdvK1sevSbG7GXmIRfwMmiHm4J23U"
        )
        callViewLike.enqueue(object : Callback<VideoPlayerData> {
            override fun onResponse(
                call: Call<VideoPlayerData>,
                response: Response<VideoPlayerData>
            ) {
                if (response.isSuccessful) {
                    tv_videoViewPlayer.text =
                        response.body()!!.items[0].statistics.viewCount.toString() + " Views"
                    tv_videoLikePlayer.text =
                        response.body()!!.items[0].statistics.likeCount.toString() + " Likes"
                }
            }

            override fun onFailure(call: Call<VideoPlayerData>, t: Throwable) {
                Toast.makeText(this@PlayerActivity, "Fail", Toast.LENGTH_SHORT).show()
            }

        })

        //call related video by videoId
        val recyclerView = findViewById<RecyclerView>(R.id.rcv_playerRelated)
        val requestRelated = ServiceBuilder.buildService(YoutubeEndpoints::class.java)
        val callRelated = requestRelated.getRelatedVideo("snippet", videoId, "video", 20, "AIzaSyD9pbOdvK1sevSbG7GXmIRfwMmiHm4J23U")
        callRelated.enqueue(object : Callback<RelatedData>{
            override fun onResponse(call: Call<RelatedData>, response: Response<RelatedData>) {
                recyclerView.apply {
                    setHasFixedSize(true)
                    layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false )
                    adapter = PlayerAdapter(response.body()!!.items, this@PlayerActivity)
                }
            }

            override fun onFailure(call: Call<RelatedData>, t: Throwable) {
                Toast.makeText(this@PlayerActivity, "Fail", Toast.LENGTH_SHORT).show()
            }

        })



        //youtube player
        utubePlayer = findViewById<YouTubePlayerView>(R.id.utubePlayer)
        if (videoId != null) {
            intilizePlayer(videoId)
        }

        //button expand
        val btn_expand = findViewById<Button>(R.id.btn_expand)
        val cardView = findViewById<CardView>(R.id.cardView)
        val expandableLayout = findViewById<LinearLayout>(R.id.expandableLayout)
        btn_expand.setOnClickListener {
            if (expandableLayout.visibility == View.GONE) {
                TransitionManager.beginDelayedTransition(cardView, AutoTransition())
                expandableLayout.visibility = View.VISIBLE
                btn_expand.text = "COLLAPSE"
            } else {
                TransitionManager.beginDelayedTransition(cardView, AutoTransition())
                expandableLayout.visibility = View.GONE
                btn_expand.text = "EXPAND"
            }
        }
    }

    private fun intilizePlayer(videoId: String) {
        utubePlayer.initialize(
            "AIzaSyD9pbOdvK1sevSbG7GXmIRfwMmiHm4J23U",
            object : YouTubePlayer.OnInitializedListener {
                override fun onInitializationSuccess(
                    p0: YouTubePlayer.Provider?,
                    p1: YouTubePlayer?,
                    p2: Boolean
                ) {
                    p1!!.loadVideo(videoId)
                    p1.play()
                }

                override fun onInitializationFailure(
                    p0: YouTubePlayer.Provider?,
                    p1: YouTubeInitializationResult?
                ) {
                    Toast.makeText(applicationContext, "Failure", Toast.LENGTH_SHORT).show()
                }
            })
    }


}