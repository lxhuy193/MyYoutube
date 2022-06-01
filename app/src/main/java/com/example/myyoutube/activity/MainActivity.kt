package com.example.myyoutube.activity

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.Window
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.viewpager.widget.ViewPager
import com.example.myyoutube.R
import com.example.myyoutube.adapter.ViewPagerAdapter
import com.example.myyoutube.newpipeExtracter.ExtractorHelper
import com.google.android.material.tabs.TabLayout
import org.schabi.newpipe.extractor.Extractor

class MainActivity : AppCompatActivity() {

//    private lateinit var searchView: SearchView
//    private var queryTextListener: SearchView.OnQueryTextListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        window.requestFeature(Window.FEATURE_ACTION_BAR);
//        supportActionBar?.hide()
        setContentView(R.layout.activity_main)

        //TabLayout + viewpager
        val viewPagerMain = findViewById<ViewPager>(R.id.vp_main)
        viewPagerMain.adapter = ViewPagerAdapter(supportFragmentManager)
        val tabLayoutMain = findViewById<TabLayout>(R.id.tl_main)
        //SET TITLE TEXT COLOR
        tabLayoutMain.setTabTextColors(Color.parseColor("#282828"), Color.parseColor("#ffffff"))
        tabLayoutMain.setupWithViewPager(viewPagerMain)
        //SET ELEVATION ACTION BAR TO ZERO
        supportActionBar!!.elevation = 0F
    }

    //SEARCH CONFIGURATION
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_options, menu)
        val searchItem = menu?.findItem(R.id.menu_search)
//        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
//        val searchView = searchItem?.actionView as SearchView
//
//        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))

//        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
//            override fun onQueryTextSubmit(query: String?): Boolean {
//                searchView.clearFocus()
//                searchView.setQuery("", false)
//                searchItem.collapseActionView()
//                println("aaaa " + query)
//                val intent = Intent(this@MainActivity, SearchActivity::class.java)
//                intent.putExtra("searchKey", query)
//                startActivity(intent)
//                return true
//            }
//
//            override fun onQueryTextChange(newText: String?): Boolean {
//                return false
//            }
//        })
        return true
    }

    //INTENT TO SEARCH FRAGMENT
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.menu_search) {
            Toast.makeText(this, "Click search item", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, SearchActivity::class.java)
            startActivity(intent)
        }
        return super.onOptionsItemSelected(item)
    }

}