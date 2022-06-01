package com.example.myyoutube.fragment

import android.os.Bundle
import android.support.annotation.NonNull
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import com.example.myyoutube.R
import com.example.myyoutube.adapter.TrendingAdapter
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView

class PlayerTempFragment : Fragment() {
    lateinit var youTubePlayerView : YouTubePlayerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_player_temp, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val videoUrl = TrendingAdapter.videoUrl

        youTubePlayerView = view.findViewById(R.id.utubePlayer)
        lifecycle.addObserver(youTubePlayerView)

        val videoId = videoUrl?.substringAfterLast("=")
        youTubePlayerView.addYouTubePlayerListener(object : AbstractYouTubePlayerListener() {
            override fun onReady(@NonNull youTubePlayer: YouTubePlayer) {
                val id = videoId
                youTubePlayer.loadVideo(id!!, 0f)
                youTubePlayer.play()
            }
        })
    }


}