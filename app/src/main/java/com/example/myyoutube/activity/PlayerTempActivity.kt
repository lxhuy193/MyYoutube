package com.example.myyoutube.activity

import android.content.pm.PackageManager
import android.content.res.Configuration
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.support.annotation.NonNull
import android.view.View
import android.view.Window
import android.widget.Button
import androidx.appcompat.app.AlertDialog
import com.example.myyoutube.R
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.utils.loadOrCueVideo
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView

class PlayerTempActivity : AppCompatActivity() {
    lateinit var youTubePlayerView : YouTubePlayerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.requestFeature(Window.FEATURE_ACTION_BAR);
        supportActionBar?.hide()
        setContentView(R.layout.activity_player_temp)
        initYouTubePlayerView()
    }

    private fun initYouTubePlayerView() {
        youTubePlayerView = findViewById(R.id.youtube_player_view)
        lifecycle.addObserver(youTubePlayerView)
        initPictureInPicture()
//        youTubePlayerView.addYouTubePlayerListener(object : AbstractYouTubePlayerListener() {
//            override fun onReady(youTubePlayer: YouTubePlayer) {
//                youTubePlayer.loadOrCueVideo(lifecycle, VideoIdsProvider.getNextVideoId(), 0f)
//            }
//        })
        val intent = intent
        val videoId: String = intent.getStringExtra("videoId").toString()
        youTubePlayerView.addYouTubePlayerListener(object : AbstractYouTubePlayerListener() {
            override fun onReady(@NonNull youTubePlayer: YouTubePlayer) {
                val id = videoId
                youTubePlayer.loadVideo(id, 0f)
                youTubePlayer.play()
            }
        })
    }

    private fun initPictureInPicture() {
        val enterPipMode = findViewById<Button>(R.id.enter_pip)
        enterPipMode.setOnClickListener { view: View? ->
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                val supportsPIP =
                    packageManager.hasSystemFeature(PackageManager.FEATURE_PICTURE_IN_PICTURE)
                if (supportsPIP) enterPictureInPictureMode()
            } else {
                AlertDialog.Builder(this)
                    .setTitle("Can't enter picture in picture mode")
                    .setMessage("In order to enter picture in picture mode you need a SDK version >= N.")
                    .show()
            }
        }
    }

    override fun onPictureInPictureModeChanged(
        isInPictureInPictureMode: Boolean,
        newConfig: Configuration?
    ) {
        super.onPictureInPictureModeChanged(isInPictureInPictureMode, newConfig)
        if (isInPictureInPictureMode) {
            youTubePlayerView!!.enterFullScreen()
        } else {
            youTubePlayerView!!.exitFullScreen()
        }
    }
}