package com.example.myyoutube

import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.android.youtube.player.YouTubeBaseActivity
import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.android.youtube.player.YouTubePlayer
import com.google.android.youtube.player.YouTubePlayerView
import de.hdodenhof.circleimageview.CircleImageView
import java.util.regex.Pattern

class PlayerActivity : YouTubeBaseActivity(){
    lateinit var utubePlayer : YouTubePlayerView
    lateinit var tv_videoTitlePlayer : TextView
    lateinit var civ_channelIhumbnailPlayer : CircleImageView
    lateinit var tv_channelTitlePlayer : TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player)

        //get intent
        val intent = intent
        val videoId : String? = intent.getStringExtra("videoId")
        tv_videoTitlePlayer = findViewById(R.id.tv_videoTitlePlayer)
        tv_videoTitlePlayer.text = intent.getStringExtra("videoTitle")
        tv_channelTitlePlayer = findViewById(R.id.tv_channelTitlePlayer)
        tv_channelTitlePlayer.text = intent.getStringExtra("channelTitle")
        civ_channelIhumbnailPlayer = findViewById(R.id.civ_channelThumbnailPlayer)
//        println("bbbbbbbb" + intent.getStringExtra("channelTitle"))

        Glide.with(this).load(intent.getStringExtra("channelThumbnail")).into(civ_channelIhumbnailPlayer)


        utubePlayer = findViewById<YouTubePlayerView>(R.id.utubePlayer)

        if (videoId != null) {
            intilizePlayer(videoId)
        }
    }

    private fun intilizePlayer(videoId : String) {
        utubePlayer.initialize("AIzaSyD9pbOdvK1sevSbG7GXmIRfwMmiHm4J23U", object : YouTubePlayer.OnInitializedListener{
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