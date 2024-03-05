package com.example.rule34.activiti

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.rule34.MainActivity
import com.example.rule34.R
import com.example.rule34.adapters.RecylerAdapter
import com.example.rule34.models.APIModel

class PostsActivity : AppCompatActivity() {
    lateinit var api: APIModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_posts)

        val recyclerView: RecyclerView = findViewById(R.id.list)

        api = APIModel(this)

        api.url = intent.extras?.get("url").toString()

        api.getJson {
            api.addPosts(it)

            recyclerView.adapter = RecylerAdapter(this, api.posts[api.page_now])
        }
    }

    public fun nextPage(touch: View){
        val recyclerView: RecyclerView = findViewById(R.id.list)

        recyclerView.adapter = RecylerAdapter(this, api.nextPosts())
    }

    public fun lastPage(touch: View){
        val recyclerView: RecyclerView = findViewById(R.id.list)

        recyclerView.adapter = RecylerAdapter(this, api.lastPosts())
    }
}