package com.example.myyoutube.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.TextureView
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.myyoutube.R
import com.example.myyoutube.activity.PlayerActivity

class DescriptionFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_description, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val tv_videoViewPlayer = view.findViewById<TextView>(R.id.tv_videoViewPlayer)
        val tv_videoLikePlayer = view. findViewById<TextView>(R.id.tv_videoLikePlayer)
        val tv_videoDatePlayer = view.findViewById<TextView>(R.id.tv_videoDatePlayer)
        val tv_videoDescripPlayer = view.findViewById<TextView>(R.id.tv_videoDescripPlayer)

        tv_videoViewPlayer.text = PlayerActivity.videoView
        tv_videoLikePlayer.text = PlayerActivity.videoLike
        tv_videoDatePlayer.text = PlayerActivity.videoDate
        tv_videoDescripPlayer.text = PlayerActivity.videoDescrip

    }
}