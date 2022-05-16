package com.example.myyoutube.fragment

import android.os.Bundle
import android.provider.SyncStateContract
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myyoutube.R
import com.example.myyoutube.activity.MainActivity
import com.example.myyoutube.activity.PlayerActivity
import com.example.myyoutube.adapter.CommentAdapter
import com.example.myyoutube.adapter.TrendingAdapter
import com.example.myyoutube.newpipeExtracter.ExtractorHelper
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import org.schabi.newpipe.extractor.comments.CommentsInfo
import org.schabi.newpipe.extractor.kiosk.KioskInfo


class CommentFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_comment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val videoUrlCmt = PlayerActivity.videoUrl

        val progressBar = view.findViewById<ProgressBar>(R.id.progressBarCmt)
        val rcv_commentPlayer = view.findViewById<RecyclerView>(R.id.rcv_commentPlayer)

        ExtractorHelper.getCommentsInfo(0, videoUrlCmt, false)
            .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
            .subscribe({ result: CommentsInfo ->
                progressBar.visibility = View.GONE
                rcv_commentPlayer.apply {
                    setHasFixedSize(true)
                    layoutManager = LinearLayoutManager(context)
                    adapter = CommentAdapter(result.relatedItems, context)
                }
            }) { throwable: Throwable ->
//                    println("erorrrr call description failed")
            }

    }
}