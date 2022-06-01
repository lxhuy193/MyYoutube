package com.example.myyoutube.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myyoutube.R
import com.example.myyoutube.activity.PlayerActivity
import com.example.myyoutube.adapter.RelatedPlayerAdapter
import com.example.myyoutube.additionClass.RelatedItemInfo
import com.example.myyoutube.newpipeExtracter.ExtractorHelper
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import org.schabi.newpipe.extractor.stream.StreamInfo


class RelatedVideoFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_related_video, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val videoUrRelated = PlayerFragment.videoUrl

        val progressBar = view.findViewById<ProgressBar>(R.id.progressBarRelated)
        val rcv_relatedVideo = view.findViewById<RecyclerView>(R.id.rcv_relatedVideo)

        ExtractorHelper.getStreamInfo(0, videoUrRelated, true)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { streamInfo: StreamInfo ->
                    progressBar.visibility = View.GONE
                    rcv_relatedVideo.apply {
                        setHasFixedSize(true)
                        layoutManager = LinearLayoutManager(context)
                        adapter =
                            RelatedPlayerAdapter(RelatedItemInfo.getInfo(streamInfo).relatedItems, context)
                    }
                }
            ) { exception: Throwable? ->
                println("error")
            }
    }

}