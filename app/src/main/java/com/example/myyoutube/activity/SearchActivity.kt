package com.example.myyoutube.activity

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.ImageButton
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myyoutube.R
import com.example.myyoutube.adapter.SearchAdapter
import com.example.myyoutube.adapter.TrendingAdapter
import com.example.myyoutube.additionClass.RelatedItemInfo
import com.example.myyoutube.newpipeExtracter.ExtractorHelper
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
//import com.google.android.youtube.player.internal.z
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import org.schabi.newpipe.extractor.InfoItem
import org.schabi.newpipe.extractor.kiosk.KioskInfo
import org.schabi.newpipe.extractor.search.SearchInfo
import org.schabi.newpipe.extractor.stream.StreamInfo
import org.schabi.newpipe.extractor.stream.StreamInfoItem

class SearchActivity : AppCompatActivity() {
    lateinit var progressBar: ProgressBar
    lateinit var recyclerView: RecyclerView
    lateinit var toolBar: Toolbar
    lateinit var imgBtn: ImageButton
    lateinit var textInputLayout: TextInputLayout
    lateinit var tiet_search: TextInputEditText

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
//        progressBar = findViewById(R.id.progressBarSearch)
        recyclerView = findViewById(R.id.recyclerViewSearch)
        toolBar = findViewById(R.id.toolBar)
        imgBtn = findViewById(R.id.imgBtn)
        textInputLayout = findViewById(R.id.textInputLayout)
        tiet_search = findViewById(R.id.tiet_search)

        //BACK NAVIGATION
        imgBtn.setOnClickListener {
            finish()
        }

        //HIDE ACTION BAR
        supportActionBar?.hide()


        tiet_search.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }

            override fun afterTextChanged(s: Editable) {
                tiet_search.setOnEditorActionListener(object : TextView.OnEditorActionListener {
                    override fun onEditorAction(
                        v: TextView?,
                        actionId: Int,
                        event: KeyEvent?
                    ): Boolean {
                        if (actionId == EditorInfo.IME_ACTION_DONE) {

                            ExtractorHelper.searchFor(
                                0,
                                s.toString(),
                                arrayOfNulls<String>(0).filterNotNull(),
                                ""
                            )
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe { result: SearchInfo ->
                                    val resultStream = mutableListOf<InfoItem>()
                                    for (i in result.relatedItems){
                                        if (i.infoType == InfoItem.InfoType.STREAM){
                                            resultStream.add(i)
                                        }
                                    }

                                    ExtractorHelper.getStreamInfo(0, resultStream[0].url, true)
                                        .subscribeOn(Schedulers.io())
                                        .observeOn(AndroidSchedulers.mainThread())
                                        .subscribe(
                                            { streamInfo: StreamInfo ->
//                                                println("relatedItem: " + RelatedItemInfo.getInfo(streamInfo).relatedItems)
                                            }
                                        ) { exception: Throwable? ->
                                            println("error")
                                        }
                                    recyclerView.apply {
                                        setHasFixedSize(true)
                                        layoutManager = LinearLayoutManager(context)
                                        adapter = SearchAdapter(resultStream, context)
                                    }
//                                    println("resultSearch " + result.relatedItems)
//                                    println("resultStream " + resultStream)
                                }

                            return true
                        }
                        return false
                    }
                })
            }

        })

    }


//    // NAVIGATION BACK ON ACTION BAR
//    override fun onSupportNavigateUp(): Boolean {
//        return super.onSupportNavigateUp()
//        onBackPressed()
//        finish()
//        return true
//    }

    //        //CHANGE TITLE IN ONCREATE
//        val actionBar = supportActionBar
//        actionBar!!.title = "Results"
//        actionBar.setDisplayHomeAsUpEnabled(true)

//    //DISPLAY CONFIGURATION MENU
//    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
//        menuInflater.inflate(R.menu.menu_options_search, menu)
//        val searchItem = menu?.findItem(R.id.menu_search_activity)
//        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
//        val searchView = searchItem?.actionView as SearchView
//
//        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
//
//        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
//            override fun onQueryTextSubmit(query: String?): Boolean {
//                searchView.clearFocus()
////                searchView.setQuery("", false)
//                searchItem.collapseActionView()
//                println("bbbbb " + query)
////                val intent = Intent(this@2SearchActivity, SearchActivity::class.java)
////                intent.putExtra("searchKey", query)
////                startActivity(intent)
//
//                if (query != null) {
//                    callApi(query)
//                }
//                return true
//            }
//
//            override fun onQueryTextChange(newText: String?): Boolean {
//                return false
//            }
//
//        })
////        return super.onCreateOptionsMenu(menu)
//        return true
//    }

//
//    fun callApi(keyWord: String) {
//        val request = ServiceBuilder.buildService(YoutubeEndpoints::class.java)
//        val call = request.getSearch(
//            "snippet",
//            20,
//            "Hello",
//            "AIzaSyAGPiwZJTlrJqeG5bET8YDEiCJ8zCJCQ_A"
//        )

//        call.enqueue(object : Callback<SearchData> {
//            override fun onResponse(call: Call<SearchData>, response: Response<SearchData>) {
//                progressBar.visibility = View.GONE
//                if(response.body() == null) println("nullllll")
////                else println("resultlttt: " + response.body())
////                println("zzzzz " + response.body()!!)
//                recyclerView.apply {
//                    setHasFixedSize(true)
//                    layoutManager = LinearLayoutManager(this@SearchActivity)
//                    if (response.body() != null){
//                        println()
//                        adapter = SearchAdapter(response.body()!!.items, this@SearchActivity)
//                    }
//                }
//            }
//
//            override fun onFailure(call: Call<SearchData>, t: Throwable) {
//                Toast.makeText(this@SearchActivity, "Fail", Toast.LENGTH_SHORT).show()
//            }
//
//        })
//    }
}