package com.example.myyoutube

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.myyoutube.Data.Item
import com.example.myyoutube.Data.TrendFeed

class TrendingAdapter(val items: List<Item>) : RecyclerView.Adapter<TrendingAdapter.VH>() {
    inner class VH (view : View) : RecyclerView.ViewHolder(view){
        private val iv_thumbNail = view.findViewById<ImageView>(R.id.iv_thumbnail)
        private val tv_videoTitle = view.findViewById<TextView>(R.id.tv_videoTitle)

        fun bind(item : Item){
            Glide.with(itemView.context).load(item.snippet.thumbnails.default.url).into(iv_thumbNail)
            tv_videoTitle.text = item.snippet.title

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        return VH(LayoutInflater.from(parent.context).inflate(R.layout.item_trending, parent, false))
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        return holder.bind(items[position])
    }

    override fun getItemCount(): Int {
        return items.size
    }
}