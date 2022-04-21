package com.example.myyoutube

import android.app.SearchManager
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.core.view.MenuItemCompat
import androidx.viewpager.widget.ViewPager
import com.example.myyoutube.adapter.ViewPagerAdapter
import com.google.android.material.tabs.TabLayout

class MainActivity : AppCompatActivity() {

//    private lateinit var searchView: SearchView
//    private var queryTextListener: SearchView.OnQueryTextListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //TabLayout + viewpager
        val viewPagerMain = findViewById<ViewPager>(R.id.vp_main)
        viewPagerMain.adapter = ViewPagerAdapter(supportFragmentManager)
        val tabLayoutMain = findViewById<TabLayout>(R.id.tl_main)
        //SET TITLE TEXT COLOR
        tabLayoutMain.setTabTextColors(Color.parseColor("#282828"), Color.parseColor("#ffffff"))
        tabLayoutMain.setupWithViewPager(viewPagerMain)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_options, menu)
        val searchItem = menu?.findItem(R.id.menu_search)
        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView = searchItem?.actionView as SearchView

        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                searchView.clearFocus()
                searchView.setQuery("", false)
                searchItem.collapseActionView()
                println("aaaa "+query)
                Toast.makeText(this@MainActivity, query, Toast.LENGTH_SHORT).show()
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                println("bbbb "+newText)
                Toast.makeText(this@MainActivity, newText, Toast.LENGTH_SHORT).show()
                return false
            }
        })

        return true


//        if (searchItem != null) {
//            val searchView = MenuItemCompat.getActionView(searchItem) as SearchView
//            searchView.setOnCloseListener(object : SearchView.OnCloseListener {
//                override fun onClose(): Boolean {
//                    return true
//                }
//            })
//
//            val searchPlate =
//                searchView.findViewById(androidx.appcompat.R.id.search_src_text) as EditText
//            searchPlate.hint = "Find your favorites"
//            val searchPlateView: View =
//                searchView.findViewById(androidx.appcompat.R.id.search_plate)
//            searchPlateView.setBackgroundColor(
//                ContextCompat.getColor(
//                    this,
//                    android.R.color.transparent
//                )
//            )
//
//            searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
//                override fun onQueryTextSubmit(query: String?): Boolean {
//                    Toast.makeText(this@MainActivity, query, Toast.LENGTH_LONG).show()
//                    println("aaaaaaaa " + query)
//                    return false
//                }
//
//                override fun onQueryTextChange(newText: String?): Boolean {
//                    Toast.makeText(this@MainActivity, newText, Toast.LENGTH_LONG).show()
//                    print("bbbbbbbb " + newText)
//                    return false
//                }
//
//            })
//
//            val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
//            searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
//
//        }

//        return super.onCreateOptionsMenu(menu)
//        val searchItem: MenuItem? = menu?.findItem(R.id.menu_search)
//        val searchManager = getSystemService(Context.SEARCH_SERVICE)
//
//        if (searchItem != null) {
//            searchView = searchItem.actionView as SearchView
//        }
//        if (searchView != null) {
//            searchView.setSearchableInfo(searchManager.g)
//        }

    }


//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        if (item.itemId == R.id.menu_search) {
//            Toast.makeText(this, "Click search item", Toast.LENGTH_SHORT).show()
//            println("aaaaaaaaaaaaa")
//        }
//        return super.onOptionsItemSelected(item)
//    }
//
//    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
//        menuInflater.inflate(R.menu.menu_options, menu)
//
//        val searchItem: MenuItem? = menu?.findItem(R.id.menu_search)
////        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
//        val searchView: SearchView? = searchItem?.actionView as SearchView
////        searchView?.setSearchableInfo(searchManager.getSearchableInfo(componentName))
//
//        searchView?.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
//            override fun onQueryTextSubmit(query: String?): Boolean {
//                println("aaaaaa" + query)
//                return false
//            }
//
//            override fun onQueryTextChange(newText: String?): Boolean {
//                println("bbbbbb" + newText)
//                return false
//            }
//        }
//        )
//
//        return super.onCreateOptionsMenu(menu)
//    }

//    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
//        menuInflater.inflate(R.menu.menu_options, menu)
//
//        // Associate searchable configuration with the SearchView
//        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
//        (menu?.findItem(R.id.menu_search)?.actionView as SearchView).apply {
//            setSearchableInfo(searchManager.getSearchableInfo(componentName))
//        }
////        return super.onCreateOptionsMenu(menu)
//        return true
//    }
}