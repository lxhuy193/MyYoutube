package com.example.myyoutube.fragment

import android.content.Intent
import android.content.pm.ActivityInfo
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.support.annotation.NonNull
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.viewpager.widget.ViewPager
import com.bumptech.glide.Glide
import com.example.myyoutube.R
import com.example.myyoutube.activity.MainActivity
import com.example.myyoutube.activity.PlayerActivity
import com.example.myyoutube.newpipeExtracter.ExtractorHelper
import com.google.android.material.tabs.TabLayout
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.PlayerConstants
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView
import de.hdodenhof.circleimageview.CircleImageView
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import org.schabi.newpipe.extractor.channel.ChannelInfo
import org.schabi.newpipe.extractor.stream.StreamInfo
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.motion.widget.MotionLayout
import com.example.myyoutube.activity.ChannelDetailActivity
import com.example.myyoutube.adapter.*
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.YouTubePlayerCallback


class PlayerFragment : Fragment() {

    private val TAG = "PLAYER_FRAGMENT"
    private var paused = false

    companion object {
        var videoUrl: String? = null
        var videoView: String? = null
        var videoLike: String? = null
        var videoDate: String? = null
        var videoDescrip: String? = null
        var channelUrl: String? = null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
//        (activity as MainActivity?)!!.window.requestFeature(Window.FEATURE_ACTION_BAR)
//        (activity as MainActivity?)!!.supportActionBar!!.hide()
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_player, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        /*
        YOUTUBE PLAYER VIEW
         */
        if (TrendingAdapter.clickCode == "TrendingAdapter") {
            videoUrl = TrendingAdapter.videoUrl
            TrendingAdapter.clickCode = ""
        } else if (RelatedPlayerAdapter.clickCode == "RelatedPlayerAdapter") {
            videoUrl = RelatedPlayerAdapter.videoUrl
            RelatedPlayerAdapter.clickCode = ""
        } else if (ChannelVideoAdapter.clickCode == "ChannelVideoAdapter") {
            videoUrl = ChannelVideoAdapter.videoUrl
//            ChannelVideoAdapter.clickCode = 0
        } else if (SearchAdapter.clickCode == "SearchAdapter"){
            videoUrl = SearchAdapter.videoUrl
        }

        val youTubePlayerView: YouTubePlayerView
        youTubePlayerView = view.findViewById(R.id.utubePlayer)
        lifecycle.addObserver(youTubePlayerView)

        val videoId = videoUrl?.substringAfterLast("=")
        val progressBarPlayerFragment =
            view.findViewById<ProgressBar>(R.id.progressBarPlayerFragment)
        youTubePlayerView.addYouTubePlayerListener(object : AbstractYouTubePlayerListener() {
            override fun onReady(@NonNull youTubePlayer: YouTubePlayer) {
                val id = videoId
                if (id != null){
                    youTubePlayer.loadVideo(id!!, 0f)
                    progressBarPlayerFragment.visibility = View.GONE
                    youTubePlayer.play()
                }
                setPlaybackSpeedButtonsClickListeners(youTubePlayer)
            }
        })


        /*
        VIEWPAGER
        */
        //TabLayout + viewpager
        val viewPagerPlayer = view.findViewById<ViewPager>(R.id.vp_player)
        viewPagerPlayer.adapter = ViewPagerPlayerAdapter(childFragmentManager)
        val tabLayoutPlayer = view.findViewById<TabLayout>(R.id.tl_player)
        //SET TITLE TEXT COLOR
        tabLayoutPlayer.setTabTextColors(Color.parseColor("#282828"), Color.parseColor("#ffffff"))
        tabLayoutPlayer.setupWithViewPager(viewPagerPlayer)
        //SET ELEVATION ACTION BAR TO ZERO
        //(context as MainActivity).supportActionBar!!.elevation = 0F

        /*
        GET PLAYER DETAIL BY VIDEO URL + MOTION LAYOUT DETAIL
         */
        val tv_videoTitlePlayer = view.findViewById<TextView>(R.id.tv_videoTitlePlayer)
        val tv_channelTitlePlayer = view.findViewById<TextView>(R.id.tv_channelTitlePlayer)
        val civ_channelIhumbnailPlayer =
            view.findViewById<CircleImageView>(R.id.civ_channelThumbnailPlayer)
        val tv_motionTitle = view.findViewById<TextView>(R.id.tv_motionTitle)

        ExtractorHelper.getStreamInfo(0, videoUrl, false)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ result: StreamInfo ->
                tv_videoTitlePlayer.text = result.name
                tv_motionTitle.text = result.name
                tv_channelTitlePlayer.text = result.uploaderName
                Glide.with(this).load(result.uploaderAvatarUrl)
                    .into(civ_channelIhumbnailPlayer)

                /*
                DESCRIPTION
                 */
                videoView = result.viewCount.toString()
                videoDate = result.textualUploadDate
                videoLike = result.likeCount.toString()
                videoDescrip = result.description.content


                /*
                GET CHANNEL URL FOR SUBSCRIBERS COUNT
                 */
                channelUrl = result.uploaderUrl
                ExtractorHelper.getChannelInfo(0, PlayerActivity.channelUrl.toString(), false)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({ result1: ChannelInfo ->
                        // CHUA DUOC SU DUNG
                        println("sacmac " + result1.subscriberCount)
                    }) { throwable: Throwable ->
                        println("erorrrr call getChannelInfo failed" + throwable)
                    }

            }) { throwable: Throwable ->
                println("erorrrr call getStreamInfo failed")
            }

        /*
        PICTURE IN PICTURE
         */
        initPictureInPicture()

        /*
        SET ONCLICKLISTENER click_channelPlayer
         */
//        val click_channelPlayer = view.findViewById<LinearLayout>(R.id.click_channelPlayer)
        val click_channelPlayer = view.findViewById<TextView>(R.id.tv_channelTitlePlayer)
        click_channelPlayer.setOnClickListener {
            goToChannel()
        }

        /*
        MOTION LAYOUT DETAIL
         */

        //CLOSE BUTTON
        val iv_motionClose = view.findViewById<ImageView>(R.id.iv_motionClose)
        iv_motionClose.setOnClickListener {
            val motionLayout: MotionLayout? = null
            motionLayout?.transitionToEnd()
            val mainActivity = activity as MainActivity
            mainActivity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
            mainActivity.supportFragmentManager.beginTransaction().remove(this).commit()
        }

        //PLAY + PAUSE BUTTON
//        var youTubePlayer : YouTubePlayer? = null
        val iv_motionPlay = view.findViewById<ImageView>(R.id.iv_motionPlay)
        iv_motionPlay.setOnClickListener {
            paused = if (paused) {
                iv_motionPlay.setImageResource(R.drawable.ic_pause_24)
                youTubePlayerView.getYouTubePlayerWhenReady(object :
                    YouTubePlayerCallback {
                    override fun onYouTubePlayer(youTubePlayer: YouTubePlayer) {
                        youTubePlayer.play()
                    }
                })
                false
            } else {
                iv_motionPlay.setImageResource(R.drawable.ic_play_arrow_24)
                youTubePlayerView.getYouTubePlayerWhenReady(object : YouTubePlayerCallback {
                    override fun onYouTubePlayer(youTubePlayer: YouTubePlayer) {
                        youTubePlayer.pause()
                    }
                })
                true
            }
        }

    }

    public fun goToChannel() {
        val intent = Intent(activity, ChannelDetailActivity::class.java)
        startActivity(intent)
    }

    private fun initPictureInPicture() {
        val imgBtn_pip = requireView().findViewById<ImageButton>(R.id.imgBtn_pip)
        imgBtn_pip.setOnClickListener(View.OnClickListener { view: View? ->
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                val supportsPIP =
                    requireContext().packageManager.hasSystemFeature(PackageManager.FEATURE_PICTURE_IN_PICTURE)
                if (supportsPIP) {
                    val linearLayout = view?.findViewById<LinearLayout>(R.id.linearLayout)
                    linearLayout?.visibility = View.GONE

                    Toast.makeText(activity, TAG, Toast.LENGTH_SHORT).show()
                    activity?.enterPictureInPictureMode()

                }
            } else {
//                AlertDialog.Builder(this)
//                    .setTitle("Can't enter picture in picture mode")
//                    .setMessage("In order to enter picture in picture mode you need a SDK version >= N.")
//                    .show()
            }
        })
    }

    private fun setPlaybackSpeedButtonsClickListeners(youTubePlayer: YouTubePlayer) {
        val playbackSpeed_0_25 = view?.findViewById<Button>(R.id.playback_speed_0_25)
        val playbackSpeed_0_5 = view?.findViewById<Button>(R.id.playback_speed_0_5)
        val playbackSpeed_1 = view?.findViewById<Button>(R.id.playback_speed_1)
        val playbackSpeed_1_5 = view?.findViewById<Button>(R.id.playback_speed_1_5)
        val playbackSpeed_2 = view?.findViewById<Button>(R.id.playback_speed_2)

        playbackSpeed_0_25?.setOnClickListener { view: View? ->
            youTubePlayer.setPlaybackRate(
                PlayerConstants.PlaybackRate.RATE_0_25
            )
        }

        playbackSpeed_0_5?.setOnClickListener { view: View? ->
            youTubePlayer.setPlaybackRate(
                PlayerConstants.PlaybackRate.RATE_0_5
            )
        }

        playbackSpeed_1?.setOnClickListener { view: View? ->
            youTubePlayer.setPlaybackRate(
                PlayerConstants.PlaybackRate.RATE_1
            )
        }

        playbackSpeed_1_5?.setOnClickListener { view: View? ->
            youTubePlayer.setPlaybackRate(
                PlayerConstants.PlaybackRate.RATE_1_5
            )
        }

        playbackSpeed_2?.setOnClickListener { view: View? ->
            youTubePlayer.setPlaybackRate(
                PlayerConstants.PlaybackRate.RATE_2
            )
        }
    }
}