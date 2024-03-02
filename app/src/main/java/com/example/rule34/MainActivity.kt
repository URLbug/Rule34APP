package com.example.rule34

import com.android.volley.Request
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import com.beust.klaxon.Klaxon

class MainActivity : AppCompatActivity() {
    var url: String = "https://api.rule34.xxx/index.php?page=dapi&s=post&q=index&json=1"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

    }

    public fun serachTag(touch: View){
        val tags: EditText = findViewById(R.id.editTextText)

        val text: String = tags.text.toString()

        val recyclerView: RecyclerView = findViewById(R.id.list)

        var adapter: RecylerAdapter

        if(text != "")
            url += "&tags=" + text.split(" ").joinToString(separator="+")
        else
            url = "https://api.rule34.xxx/index.php?page=dapi&s=post&q=index&json=1"

        getJson { posts ->
            adapter = RecylerAdapter(this, posts)

            recyclerView.adapter = adapter
        }
    }

    private fun getJson(callback: (List<PostsModel>) -> Unit) {
        val ques = Volley.newRequestQueue(this)

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
