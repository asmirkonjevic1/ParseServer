package com.hfad.parse_server.Fragments


import android.graphics.BitmapFactory
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.hfad.parse_server.Adapter.PostAdapter
import com.hfad.parse_server.Model.Post
import com.hfad.parse_server.R
import com.parse.*
import kotlinx.android.synthetic.main.fragment_home.*

class HomeFragment : Fragment(), SwipeRefreshLayout.OnRefreshListener {

    companion object {
        lateinit var viewAdapter: RecyclerView.Adapter<*>
    }
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewManager: RecyclerView.LayoutManager
    private lateinit var mSwipeRefreshLayout: SwipeRefreshLayout
    private var postList : ArrayList<Post> = ArrayList()


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        recyclerView = view.findViewById(R.id.recyclerView)
        mSwipeRefreshLayout = view.findViewById(R.id.swiperefresh)

        viewManager = LinearLayoutManager(context)
        viewAdapter = PostAdapter(context, postList)

        recyclerView.layoutManager = viewManager
        recyclerView.adapter = viewAdapter

        getData()

        mSwipeRefreshLayout.setOnRefreshListener(this)

        return view

    }

    override fun onRefresh() {
        getData()
    }

    fun getData(){
        postList.clear()
        val query = ParseQuery<ParseObject>("Image")
        query.orderByDescending("createdAt")
        query.findInBackground(object : FindCallback<ParseObject>{
            override fun done(objects: MutableList<ParseObject>?, e: ParseException?) {
                if (e == null && objects?.size != null) {
                    for (obj in objects) {
                        val objectId = obj.objectId
                        val file: ParseFile = obj.get("image") as ParseFile
                        val username = obj.getString("username")

                        file.getDataInBackground(object : GetDataCallback {
                            override fun done(data: ByteArray?, e: ParseException?) {
                                if (e == null && data != null) {
                                    val bitmap = BitmapFactory.decodeByteArray(data, 0, data.size)
                                    postList.add(Post(objectId, bitmap, username))
                                    viewAdapter.notifyDataSetChanged()
                                    mSwipeRefreshLayout.isRefreshing = false
                                } else {
                                    e!!.printStackTrace()
                                }
                            }
                        })
                    }

                } else {
                    e!!.printStackTrace()
                }

            }
        })
    }
}
