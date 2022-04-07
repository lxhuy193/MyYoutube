package com.example.myyoutube

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.youtube.player.YouTubeBaseActivity
import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.android.youtube.player.YouTubePlayer
import com.google.android.youtube.player.YouTubePlayerView
import java.util.regex.Pattern

class PlayerActivity : YouTubeBaseActivity(){
    lateinit var utubePlayer : YouTubePlayerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player)

        //get intent
        val intent = intent
        val videoId : String? = intent.getStringExtra("videoId")

        utubePlayer = findViewById<YouTubePlayerView>(R.id.utubePlayer)
//        if (videoId != null) {
//            getYoutubeIdVideoFromUrl("https://www.youtube.com/watch?v="+videoId)?.let {
//                intilizePlayer(
//                    it
//                )
//            }
//            Toast.makeText(this, videoId, Toast.LENGTH_SHORT).show()
//        }
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

//    fun getYoutubeIdVideoFromUrl(inUrl : String) : String?{
//        if (inUrl.toLowerCase().contains("youtu.be")){
//            return inUrl.substring(inUrl.lastIndexOf("/")+1)
//        }
//        val pattern = "(?<=watch\\?v=|/videos/|embed\\/)[^#\\&\\?]*"
//        val compiledPattern = Pattern.compile(pattern)
//        val matcher = compiledPattern.matcher(inUrl)
//        return if (matcher.find()){
//            matcher.group()
//        }else null
//    }
}