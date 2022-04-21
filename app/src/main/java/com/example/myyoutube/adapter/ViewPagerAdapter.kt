package com.example.myyoutube.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.myyoutube.fragment.TrendingGamesFragment
import com.example.myyoutube.fragment.TrendingMusicFragment
import com.example.myyoutube.fragment.TrendingNewsFragment

class ViewPagerAdapter(fragmentManager: FragmentManager) :
    FragmentPagerAdapter(fragmentManager) {
    override fun getCount(): Int {
        return 3
    }

    override fun getItem(position: Int): Fragment {
        when (position){
            0 -> return TrendingMusicFragment()
            1 -> return TrendingNewsFragment()
            2 -> return TrendingGamesFragment()
            else -> return TrendingMusicFragment()
        }
    }

    override fun getPageTitle(position: Int): CharSequence? {
        when (position){
            0 -> return "Music"
            1 -> return "News"
            2 -> return "Game"
            else -> return "Music"
        }
    }

}