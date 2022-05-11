package com.example.myyoutube.activity

import androidx.core.app.ComponentActivity
import android.app.PictureInPictureParams
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import android.support.annotation.NonNull
import android.transition.AutoTransition
import android.transition.TransitionManager
import android.util.Log
import android.util.Rational
import android.view.View
import android.view.Window
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.myyoutube.Data.*
import com.example.myyoutube.Network.ServiceBuilder
import com.example.myyoutube.Network.YoutubeEndpoints
import com.example.myyoutube.R
import com.example.myyoutube.adapter.CommentAdapter
import com.example.myyoutube.adapter.PlayerAdapter
import com.example.myyoutube.adapter.TrendingAdapter
import com.example.myyoutube.additionClass.RelatedItemInfo
import com.example.myyoutube.newpipeExtracter.ExtractorHelper
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.PlayerConstants.PlaybackRate
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.utils.loadOrCueVideo
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView
import de.hdodenhof.circleimageview.CircleImageView
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.schedulers.Schedulers
import org.schabi.newpipe.extractor.comments.CommentsInfo
import org.schabi.newpipe.extractor.kiosk.KioskInfo
import org.schabi.newpipe.extractor.stream.StreamInfo
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.IllegalStateException


class PlayerActivity : AppCompatActivity() {

    private val TAG = "PLAYER_ACTIVITY"

    lateinit var utubePlayer: YouTubePlayerView
    lateinit var tv_videoTitlePlayer: TextView
    lateinit var civ_channelIhumbnailPlayer: CircleImageView
    lateinit var tv_channelTitlePlayer: TextView
    lateinit var tv_videoDatePlayer: TextView
    lateinit var tv_videoDescripPlayer: TextView
    lateinit var tv_videoViewPlayer: TextView
    lateinit var tv_videoLikePlayer: TextView
    lateinit var tv_channelSubscribes: TextView

    /*
    INTENT WITH "FINISH_ACTIVITY" TO KILL PREVIOUS ACTIVITY WHEN CLICK ANOTHER VIDEO IN TRENDING LIST
    */
    lateinit var imgBtn_pip: ImageButton
    private val mReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            intent?.let { intent ->
                if (intent.action == "FINISH_ACTIVITY")
                    finish()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.requestFeature(Window.FEATURE_ACTION_BAR);
        supportActionBar?.hide()
        setContentView(R.layout.activity_player)


        /*
        Get data through intent
         */
        val intent = intent
        val videoId: String = intent.getStringExtra("videoId").toString()

        tv_videoTitlePlayer = findViewById(R.id.tv_videoTitlePlayer)
        tv_videoTitlePlayer.text = intent.getStringExtra("videoTitle")

        tv_videoViewPlayer = findViewById(R.id.tv_videoViewPlayer)
        tv_videoViewPlayer.text = intent.getStringExtra("videoView").toString()

        tv_videoDatePlayer = findViewById(R.id.tv_videoDatePlayer)
        tv_videoDatePlayer.text = intent.getStringExtra("videoDate")

        tv_videoDescripPlayer = findViewById(R.id.tv_videoDescripPlayer)
        tv_videoDescripPlayer.text = intent.getStringExtra("videoDescrip")

        tv_videoLikePlayer = findViewById(R.id.tv_videoLikePlayer)
        tv_videoLikePlayer.text = intent.getStringExtra("videoLike")

        tv_channelTitlePlayer = findViewById(R.id.tv_channelTitlePlayer)
        tv_channelTitlePlayer.text = intent.getStringExtra("channelName")

//        tv_channelSubscribes = findViewById(R.id.tv_channelSubscribes)
//        val channelId = intent.getStringExtra("channelId").toString()
        civ_channelIhumbnailPlayer = findViewById(R.id.civ_channelThumbnailPlayer)
        Glide.with(this).load(intent.getStringExtra("channelThumbnail"))
            .into(civ_channelIhumbnailPlayer)

        /*
        INTENT WITH "FINISH_ACTIVITY" TO KILL PREVIOUS ACTIVITY WHEN CLICK ANOTHER VIDEO IN TRENDING LIST
         */
        LocalBroadcastManager.getInstance(this)
            .registerReceiver(mReceiver, IntentFilter("FINISH_ACTIVITY"))

        /*
        Button expandable
         */
        val btn_expand = findViewById<ImageButton>(R.id.btn_expand)
        val cardView = findViewById<CardView>(R.id.cardView)
        val expandableLayout = findViewById<LinearLayout>(R.id.expandableLayout)
        btn_expand.setOnClickListener {
            if (expandableLayout.visibility == View.GONE) {
                TransitionManager.beginDelayedTransition(cardView, AutoTransition())
                expandableLayout.visibility = View.VISIBLE
                btn_expand.setImageResource(R.drawable.ic_arrow_up)
            } else {
                TransitionManager.beginDelayedTransition(cardView, AutoTransition())
                expandableLayout.visibility = View.GONE
//                btn_expand.text = "EXPAND"
                btn_expand.setImageResource(R.drawable.ic_arrow_down)
            }
        }

        /*
        YoutubePlayerView from Git
         */
        utubePlayer = findViewById<YouTubePlayerView>(R.id.utubePlayer)
        lifecycle.addObserver(utubePlayer)

        utubePlayer.addYouTubePlayerListener(object : AbstractYouTubePlayerListener() {
            override fun onReady(@NonNull youTubePlayer: YouTubePlayer) {
                val id = videoId
                youTubePlayer.loadVideo(id, 0f)
                youTubePlayer.play()

                setPlaybackSpeedButtonsClickListeners(youTubePlayer)
            }
        })

        /*
        PiP using YouTubePlayer Git
         */
        initPictureInPicture()


        /*
        COMMENT VIDEO
         */
        val rcv_comment = findViewById<RecyclerView>(R.id.rcv_comment)
        val videoUrl = intent.getStringExtra("videoUrl")
        ExtractorHelper.getCommentsInfo(0, videoUrl, false)
            .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
            .subscribe({ result: CommentsInfo ->
                rcv_comment.apply {
                    setHasFixedSize(true)
                    layoutManager = LinearLayoutManager(context)
                    adapter = CommentAdapter(result.relatedItems, context)
                }
            }) { throwable: Throwable ->
//                    println("erorrrr call description failed")
            }

        /*
        RELATED VIDEO
         */
        val rcv_playerRelated = findViewById<RecyclerView>(R.id.rcv_playerRelated)
        ExtractorHelper.getStreamInfo(0, videoUrl, true)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { streamInfo: StreamInfo ->
                    println("relatedItem: " + RelatedItemInfo.getInfo(streamInfo).relatedItems)
                    rcv_playerRelated.apply {
                        setHasFixedSize(true)
                        layoutManager = LinearLayoutManager(context)
                        adapter = PlayerAdapter(RelatedItemInfo.getInfo(streamInfo).relatedItems, context)
                    }
                }
            ) { exception: Throwable? ->
                println("error")
            }
    }

    private fun initPictureInPicture() {
        imgBtn_pip = findViewById(R.id.imgBtn_pip)
        imgBtn_pip.setOnClickListener(View.OnClickListener { view: View? ->
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                val supportsPIP =
                    packageManager.hasSystemFeature(PackageManager.FEATURE_PICTURE_IN_PICTURE)
                if (supportsPIP) {
                    Toast.makeText(this, TAG, Toast.LENGTH_SHORT).show()
                    enterPictureInPictureMode()
                }
            } else {
                AlertDialog.Builder(this)
                    .setTitle("Can't enter picture in picture mode")
                    .setMessage("In order to enter picture in picture mode you need a SDK version >= N.")
                    .show()
            }
        })
    }

    var isInPipMode: Boolean = false
    override fun onPictureInPictureModeChanged(
        isInPictureInPictureMode: Boolean,
        newConfig: Configuration?
    ) {
        super.onPictureInPictureModeChanged(isInPictureInPictureMode, newConfig)
        isInPipMode = isInPictureInPictureMode
    }

    override fun onStop() {
        super.onStop()
        if (isInPipMode) {
            finishAndRemoveTask()
        }
    }

    private fun setPlaybackSpeedButtonsClickListeners(youTubePlayer: YouTubePlayer) {
        val playbackSpeed_0_25 = findViewById<Button>(R.id.playback_speed_0_25)
        val playbackSpeed_0_5 = findViewById<Button>(R.id.playback_speed_0_5)
        val playbackSpeed_1 = findViewById<Button>(R.id.playback_speed_1)
        val playbackSpeed_1_5 = findViewById<Button>(R.id.playback_speed_1_5)
        val playbackSpeed_2 = findViewById<Button>(R.id.playback_speed_2)

        playbackSpeed_0_25.setOnClickListener { view: View? ->
            youTubePlayer.setPlaybackRate(
                PlaybackRate.RATE_0_25
            )
        }

        playbackSpeed_0_5.setOnClickListener { view: View? ->
            youTubePlayer.setPlaybackRate(
                PlaybackRate.RATE_0_5
            )
        }

        playbackSpeed_1.setOnClickListener { view: View? ->
            youTubePlayer.setPlaybackRate(
                PlaybackRate.RATE_1
            )
        }

        playbackSpeed_1_5.setOnClickListener { view: View? ->
            youTubePlayer.setPlaybackRate(
                PlaybackRate.RATE_1_5
            )
        }

        playbackSpeed_2.setOnClickListener { view: View? ->
            youTubePlayer.setPlaybackRate(
                PlaybackRate.RATE_2
            )
        }
    }
}

        /*
        Picture in picture
         */
/*
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            pictureInPictureParamsBuilder = PictureInPictureParams.Builder()
        }

        //handle pic in pic click
        imgBtn_pip = findViewById(R.id.imgBtn_pip)
        imgBtn_pip.setOnClickListener {
            pictureInPictureMode()
        }
*/

/*
Another solution for PiP - FAILED
 */
/*
        val tv_relatedVideo = findViewById<TextView>(R.id.tv_relatedVideo)
        val rcv_playerRelated = findViewById<RecyclerView>(R.id.rcv_playerRelated)
        val tv_comments = findViewById<TextView>(R.id.tv_comments)
        val rcv_comment = findViewById<RecyclerView>(R.id.rcv_comment)
        imgBtn_pip.setOnClickListener(View.OnClickListener {
            if (Build.VERSION.SDK_INT >= 26) {
                //Trigger PiP mode
                try {
//                    cardView.visibility = View.GONE
//                    tv_relatedVideo.visibility = View.GONE
//                    rcv_playerRelated.visibility = View.GONE
//                    tv_comments.visibility = View.GONE
//                    rcv_comment.visibility = View.GONE

                    val rational = Rational(utubePlayer.getWidth(), utubePlayer.getHeight())
                    val mParams = PictureInPictureParams.Builder()
                        .setAspectRatio(rational)
                        .build()
                    enterPictureInPictureMode(mParams)
                } catch (e: IllegalStateException) {
                    e.printStackTrace()
                }
            } else {
                Toast.makeText(
                    this@PlayerActivity,
                    "API 26 needed to perform PiP",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
*/

/*
    private fun pictureInPictureMode() {
        Log.d(TAG, "pictureInPictureMode: Try to enter in PIP mode")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Log.d(TAG, "pictureInPictureMode: Supports PIP")
            //setup PIP height width
            val aspectRatio = Rational(utubePlayer.width, utubePlayer.height)
            pictureInPictureParamsBuilder!!.setAspectRatio(aspectRatio).build()
            enterPictureInPictureMode(pictureInPictureParamsBuilder!!.build())
        } else {
            Log.d(TAG, "pictureInPictureMode: Doesn't supports PIP")
            Toast.makeText(this, "Your device doesn't supports PIP", Toast.LENGTH_LONG).show()
        }
    }

    override fun onUserLeaveHint() {
        super.onUserLeaveHint()
        //when user presses home button, if not in PIP mode, enter in PIP, requires Android N and above
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Log.d(TAG, "onUserLeaveHint: was not in PIP")
            pictureInPictureMode()
        } else {
            Log.d(TAG, "onUserLeaveHint: Already in PIP")
        }
    }

    override fun onPictureInPictureModeChanged(
        isInPictureInPictureMode: Boolean,
        newConfig: Configuration?
    ) {
        super.onPictureInPictureModeChanged(isInPictureInPictureMode, newConfig)
        if (isInPictureInPictureMode) {
            Log.d(TAG, "onPictureInPictureModeChanged: Entered PIP")
            //hid pip button and actionbar
            imgBtn_pip.visibility = View.GONE
//            actionBar!!.hide()
        } else {
            Log.d(TAG, "onPictureInPictureModeChanged: Exited PIP")
            imgBtn_pip.visibility = View.VISIBLE
//            actionBar!!.show()
        }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        //when 1st video is playing, and entered in PIP, clicked 2nd video play 2nd video
        Log.d(TAG, "onNewIntent: Play New Video")
        setVideoView(intent)
    }

        override fun onStop() {
        super.onStop()
        if (videoView.isPlaying){
            videoView.stopPlayback()
        }
    }
    override fun onPause() {
        super.onPause()
        // If called while in PiP mode, do not pause playback
        if (utubePlayer.isActivated) {
            utubePlayer.releasePointerCapture()
        } else {
            // Use existing playback logic for paused Activity behavior.
        }
    }

    private fun intilizePlayer(videoId: String) {
        utubePlayer.initialize(
            "AIzaSyAGPiwZJTlrJqeG5bET8YDEiCJ8zCJCQ_A",
            object : YouTubePlayer.OnInitializedListener {
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


        //call comment from videoId
        val recyclerViewCmt = findViewById<RecyclerView>(R.id.rcv_comment)
        val requestComment = ServiceBuilder.buildService(YoutubeEndpoints::class.java)
        val callComment = requestComment.getComment(
            "snippet",
            40,
            "plainText",
            videoId,
            "relevance",
            "AIzaSyAGPiwZJTlrJqeG5bET8YDEiCJ8zCJCQ_A"
        )
        callComment.enqueue(object : Callback<CommentData> {
            override fun onResponse(call: Call<CommentData>, response: Response<CommentData>) {
                recyclerViewCmt.apply {
                    setHasFixedSize(true)
                    layoutManager =
                        LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
                    adapter = CommentAdapter(response.body()!!.items, this@PlayerActivity)
                }
            }

            override fun onFailure(call: Call<CommentData>, t: Throwable) {
                Toast.makeText(this@PlayerActivity, "Fail", Toast.LENGTH_SHORT).show()
            }

        })

        //call video description describ by ID
        tv_videoDescripPlayer = findViewById(R.id.tv_videoDescripPlayer)
        val requestDescrip = ServiceBuilder.buildService(YoutubeEndpoints::class.java)
        val callDescrip = requestDescrip.getVideoDetail(
            "snippet",
            videoId,
            "AIzaSyAGPiwZJTlrJqeG5bET8YDEiCJ8zCJCQ_A"
        )
        callDescrip.enqueue(object : Callback<TrendFeed> {
            override fun onResponse(call: Call<TrendFeed>, response: Response<TrendFeed>) {
                if (response.isSuccessful) {
                    tv_videoDescripPlayer.text = response.body()!!.items[0].snippet.description
                }
            }

            override fun onFailure(call: Call<TrendFeed>, t: Throwable) {
                Toast.makeText(this@PlayerActivity, "Fail", Toast.LENGTH_SHORT).show()
            }

        }
        )

        //call channel subscribers by channelId
        val requestSubs = ServiceBuilder.buildService(YoutubeEndpoints::class.java)
        val callSubs = requestSubs.getChannelStatistics(
            "statistics",
            channelId,
            "AIzaSyAGPiwZJTlrJqeG5bET8YDEiCJ8zCJCQ_A"
        )
        callSubs.enqueue(object : Callback<ChannelStatistics> {
            override fun onResponse(
                call: Call<ChannelStatistics>,
                response: Response<ChannelStatistics>
            ) {
                tv_channelSubscribes.text =
                    response.body()!!.items[0].statistics.subscriberCount + " Subscribers"
            }

            override fun onFailure(call: Call<ChannelStatistics>, t: Throwable) {
                Toast.makeText(this@PlayerActivity, "Fail", Toast.LENGTH_SHORT).show()
            }

        })

        //call video views likes by ID
        tv_videoViewPlayer = findViewById(R.id.tv_videoViewPlayer)
        tv_videoLikePlayer = findViewById(R.id.tv_videoLikePlayer)
        val requestViewLike = ServiceBuilder.buildService(YoutubeEndpoints::class.java)
        val callViewLike = requestViewLike.getVideoViewLikeCmt(
            "statistics",
            videoId,
            "AIzaSyAGPiwZJTlrJqeG5bET8YDEiCJ8zCJCQ_A"
        )
        callViewLike.enqueue(object : Callback<VideoPlayerData> {
            override fun onResponse(
                call: Call<VideoPlayerData>,
                response: Response<VideoPlayerData>
            ) {
                if (response.isSuccessful) {
                    tv_videoViewPlayer.text =
                        response.body()!!.items[0].statistics.viewCount.toString() + " Views"
                    tv_videoLikePlayer.text =
                        response.body()!!.items[0].statistics.likeCount.toString() + " Likes"
                }
            }

            override fun onFailure(call: Call<VideoPlayerData>, t: Throwable) {
                Toast.makeText(this@PlayerActivity, "Fail", Toast.LENGTH_SHORT).show()
            }

        })

*/