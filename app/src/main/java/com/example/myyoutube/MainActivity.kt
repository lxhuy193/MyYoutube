package com.example.myyoutube

import android.app.SearchManager
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ProgressBar
import android.widget.SearchView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myyoutube.Data.TrendFeed
import com.example.myyoutube.Network.ServiceBuilder
import com.example.myyoutube.Network.YoutubeEndpoints
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val progressBar = findViewById<ProgressBar>(R.id.progressBar)
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)

        val request = ServiceBuilder.buildService(YoutubeEndpoints::class.java)
        val call = request.getTrendding(
            "snippet",
            20,
            "mostPopular",
            "AIzaSyD9pbOdvK1sevSbG7GXmIRfwMmiHm4J23U",
            "VN",
            10
        )

        call.enqueue(object : Callback<TrendFeed> {
            override fun onResponse(call: Call<TrendFeed>, response: Response<TrendFeed>) {
                if (response.isSuccessful) {
                    progressBar.visibility = View.GONE
                    recyclerView.apply {
                        setHasFixedSize(true)
                        layoutManager = LinearLayoutManager(this@MainActivity)
                        adapter = MainAdapter(response.body()!!.items, this@MainActivity)
                    }
                }
            }

            override fun onFailure(call: Call<TrendFeed>, t: Throwable) {
                Toast.makeText(this@MainActivity, "Fail", Toast.LENGTH_SHORT).show()
            }

        })
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.menu_search) {
            Toast.makeText(this, "Click search item", Toast.LENGTH_SHORT).show()
            println("aaaaaaaaaaaaa")
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_options, menu)

        val searchItem: MenuItem? = menu?.findItem(R.id.menu_search)
//        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView: SearchView? = searchItem?.actionView as SearchView
//        searchView?.setSearchableInfo(searchManager.getSearchableInfo(componentName))

        searchView?.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                TODO("Not yet implemented")
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        }
        )

        return super.onCreateOptionsMenu(menu)
    }
}