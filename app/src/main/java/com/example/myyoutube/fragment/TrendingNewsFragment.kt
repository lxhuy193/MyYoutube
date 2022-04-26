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
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TrendingNewsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_trending_news, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val progressBar = view.findViewById<ProgressBar>(R.id.progressBarNews)
        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerViewNews)

        val request = ServiceBuilder.buildService(YoutubeEndpoints::class.java)
        val call = request.getTrendding(
            "snippet",
            20,
            "mostPopular",
            "AIzaSyAGPiwZJTlrJqeG5bET8YDEiCJ8zCJCQ_A",
            "VN",
            25
        )

        call.enqueue(object : Callback<TrendFeed> {
            override fun onResponse(call: Call<TrendFeed>, response: Response<TrendFeed>) {
                if (response.isSuccessful) {
                    progressBar.visibility = View.GONE
                    recyclerView.apply {
                        setHasFixedSize(true)
                        layoutManager = LinearLayoutManager(activity)
//                        adapter = TrendingAdapter(response.body()!!.items, context)
                    }
                }
            }

            override fun onFailure(call: Call<TrendFeed>, t: Throwable) {
                Toast.makeText(activity, "Fail", Toast.LENGTH_SHORT).show()
            }

        })
    }
}