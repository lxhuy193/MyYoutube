package com.example.myyoutube.adapter

import android.os.Bundle
import android.support.annotation.NonNull
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.myyoutube.R
import com.example.myyoutube.fragment.*

class ViewPagerPlayerAdapter(fragmentManager: FragmentManager) :
    FragmentPagerAdapter(fragmentManager) {
    override fun getCount(): Int {
        return 3
    }

    override fun getItem(position: Int): Fragment {
        when (position) {
            0 -> return CommentFragment()
            1 -> return RelatedVideoFragment()
            2 -> return DescriptionFragment()
            else -> return TrendingMusicFragment()
        }
    }

    override fun getPageTitle(position: Int): CharSequence? {
        when (position) {
            0 -> return "Comments"
            1 -> return "Related"
            2 -> return "Description"
            else -> return "Comments"
        }
    }
}