package com.example.myyoutube.activity

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.Glide.with
import com.example.myyoutube.R
import com.example.myyoutube.adapter.ChannelVideoAdapter
import com.example.myyoutube.adapter.SearchAdapter
import com.example.myyoutube.newpipeExtracter.ExtractorHelper
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import org.schabi.newpipe.extractor.channel.ChannelInfo
import androidx.core.app.ActivityCompat.startActivityForResult




class ChannelDetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_channel_detail)

        val intent = intent
        val channelUrl = intent.getStringExtra("channelUrl")
        val rcv_channelVideo = findViewById<RecyclerView>(R.id.rcv_channelVideo)
        val iv_channelBanner = findViewById<ImageView>(R.id.iv_channelBanner)
        val civ_channelAvatar = findViewById<CircleImageView>(R.id.civ_channelAvatar)
        val tv_channelName = findViewById<TextView>(R.id.tv_channelName)
        val tv_channelSubscribers = findViewById<TextView>(R.id.tv_channelSubscribers)
        val progressBarChannel = findViewById<ProgressBar>(R.id.progressBarChannel)

        ExtractorHelper.getChannelInfo(0, channelUrl, false)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ result: ChannelInfo ->
                progressBarChannel.visibility = View.GONE

                Glide.with(this).load(result.bannerUrl).into(iv_channelBanner)
                Glide.with(this).load(result.avatarUrl).into(civ_channelAvatar)
                tv_channelName.text = result.name
                supportActionBar?.setTitle(result.name)
                tv_channelSubscribers.text = result.subscriberCount.toString()

                rcv_channelVideo.apply {
                    setHasFixedSize(true)
                    layoutManager = LinearLayoutManager(context)
                    adapter = ChannelVideoAdapter(result.relatedItems, context)
                    println("Macbook " + result.relatedItems)
                }
            }) { throwable: Throwable ->
                println("erorrrr call getChannelInfo failed" + throwable)
            }

        //BACK NAVIGATION
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

}