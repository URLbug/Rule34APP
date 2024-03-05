package com.example.rule34.models

import android.content.Context
import com.android.volley.Request
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import com.beust.klaxon.Klaxon

class APIModel(val context: Context) {
    lateinit var posts: List<List<PostsModel>>

    var url: String = "https://api.rule34.xxx/index.php?page=dapi&s=post&q=index&json=1"

    var pages: Int = 5
    var page_now: Int = 0

    fun addPosts(posts: List<PostsModel>){
        this.posts = posts.chunked(pages)
    }

    fun nextPosts(): List<PostsModel> {
        if(page_now < posts.size) {
            page_now += 1
        }

        return posts[page_now]
    }

    fun lastPosts(): List<PostsModel> {
        if(page_now > 0) {
            page_now -= 1
        }

        return posts[page_now]
    }

    fun getJson(callback: (List<PostsModel>) -> Unit) {
        val ques = Volley.newRequestQueue(context)

        val jsonArrayReq = JsonArrayRequest(
            Request.Method.GET, url, null,
            { response ->
                val jsonArray = response.toString()

                Klaxon().parseArray<PostsModel>(jsonArray)?.let { callback(it) }
            },
            {
                it
            }
        )

        ques.add(jsonArrayReq)
    }

}