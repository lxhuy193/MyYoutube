package com.example.myyoutube

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.myyoutube.Data.ChannelItem
import com.example.myyoutube.Data.TrendItem


class MainAdapter(val trendItems: List<TrendItem>,val context: Context) : RecyclerView.Adapter<MainAdapter.VH>() {
    var onItemClick: ((TrendItem) -> Unit)? = null

    inner class VH(view: View) : RecyclerView.ViewHolder(view) {
        private val iv_thumbNail = view.findViewById<ImageView>(R.id.iv_thumbnail)
        private val tv_videoTitle = view.findViewById<TextView>(R.id.tv_videoTitle)

        fun bind(item: TrendItem) {
            Glide.with(itemView.context).load(item.snippet.thumbnails.high.url).into(iv_thumbNail)
            tv_videoTitle.text = item.snippet.title
        }


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        return VH(
            LayoutInflater.from(parent.context).inflate(R.layout.item_trending, parent, false)
        )
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.itemView.setOnClickListener{
            v: View -> Unit
            val intent = Intent (context, PlayerActivity::class.java)
            intent.putExtra("videoId",trendItems[position].id )
            context.startActivity(intent)
        }
        return holder.bind(trendItems[position])
    }

    override fun getItemCount(): Int {
        return trendItems.size
    }
}