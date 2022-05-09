package com.example.myyoutube.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myyoutube.Data.TrendFeed
import com.example.myyoutube.Network.ServiceBuilder
import com.example.myyoutube.Network.YoutubeEndpoints
import com.example.myyoutube.R
import com.example.myyoutube.adapter.TrendingAdapter
import com.example.myyoutube.newpipeExtracter.ExtractorHelper
//import com.google.android.youtube.player.internal.t
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.schedulers.Schedulers
import org.schabi.newpipe.extractor.kiosk.KioskInfo
import org.schabi.newpipe.extractor.stream.StreamInfo
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TrendingMusicFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_trending_music, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val progressBar = view.findViewById<ProgressBar>(R.id.progressBarMusic)
        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerViewMusic)

        ExtractorHelper.getKioskInfo(0, "https://www.youtube.com/feed/trending", false)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ result: KioskInfo ->
                    progressBar.visibility = View.GONE
                recyclerView.apply {
                    setHasFixedSize(true)
                    layoutManager = LinearLayoutManager(activity)
                    adapter = TrendingAdapter(result.relatedItems, context)
                }
            }) { throwable: Throwable ->
            }

//        val request = ServiceBuilder.buildService(YoutubeEndpoints::class.java)
//        val call = request.getTrendding(
//            "snippet",
//            20,
//            "mostPopular",
//            "AIzaSyAGPiwZJTlrJqeG5bET8YDEiCJ8zCJCQ_A",
//            "VN",
//            10
//        )
//
//        call.enqueue(object : Callback<TrendFeed> {
//            override fun onResponse(call: Call<TrendFeed>, response: Response<TrendFeed>) {
//                if (response.isSuccessful) {
//                    progressBar.visibility = View.GONE
//                    recyclerView.apply {
//                        setHasFixedSize(true)
//                        layoutManager = LinearLayoutManager(activity)
//                        adapter = TrendingAdapter(response.body()!!.items, context)
//                    }
//                }
//            }
//
//            override fun onFailure(call: Call<TrendFeed>, t: Throwable) {
//                Toast.makeText(activity, "Fail", Toast.LENGTH_SHORT).show()
//            }
//
//        })

    }

}