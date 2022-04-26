package com.example.myyoutube.activity

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.View
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myyoutube.Data.SearchData
import com.example.myyoutube.Data.SearchItem
import com.example.myyoutube.Data.TrendFeed
import com.example.myyoutube.Network.ServiceBuilder
import com.example.myyoutube.Network.YoutubeEndpoints
import com.example.myyoutube.R
import com.example.myyoutube.adapter.SearchAdapter
import com.example.myyoutube.adapter.TrendingAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchActivity : AppCompatActivity() {
    lateinit var progressBar: ProgressBar
    lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        progressBar = findViewById(R.id.progressBarSearch)
        recyclerView = findViewById(R.id.recyclerViewSearch)

        //CHANGE TITLE
        val actionBar = supportActionBar
        actionBar!!.title = "Results"
        actionBar.setDisplayHomeAsUpEnabled(true)
    }

    //DISPLAY CONFIGURATION MENU
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_options_search, menu)
        val searchItem = menu?.findItem(R.id.menu_search_activity)
        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView = searchItem?.actionView as SearchView

        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                searchView.clearFocus()
//                searchView.setQuery("", false)
                searchItem.collapseActionView()
                println("bbbbb " + query)
//                val intent = Intent(this@2SearchActivity, SearchActivity::class.java)
//                intent.putExtra("searchKey", query)
//                startActivity(intent)

                if (query != null) {
                    callApi(query)
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }

        })
//        return super.onCreateOptionsMenu(menu)
        return true
    }

    // NAVIGATION BACK ON ACTION BAR
    override fun onSupportNavigateUp(): Boolean {
//        return super.onSupportNavigateUp()
        onBackPressed()
        finish()
        return true
    }

    fun callApi(keyWord: String) {
        val request = ServiceBuilder.buildService(YoutubeEndpoints::class.java)
        val call = request.getSearch(
            "snippet",
            20,
            "Hello",
            "AIzaSyAGPiwZJTlrJqeG5bET8YDEiCJ8zCJCQ_A"
        )

        call.enqueue(object : Callback<SearchData> {
            override fun onResponse(call: Call<SearchData>, response: Response<SearchData>) {
                progressBar.visibility = View.GONE
                if(response.body() == null) println("nullllll")
//                else println("resultlttt: " + response.body())
//                println("zzzzz " + response.body()!!)
                recyclerView.apply {
                    setHasFixedSize(true)
                    layoutManager = LinearLayoutManager(this@SearchActivity)
                    if (response.body() != null){
                        println()
                        adapter = SearchAdapter(response.body()!!.items, this@SearchActivity)
                    }
                }
            }

            override fun onFailure(call: Call<SearchData>, t: Throwable) {
                Toast.makeText(this@SearchActivity, "Fail", Toast.LENGTH_SHORT).show()
            }

        })
    }

}