package com.example.rule34

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.example.rule34.activiti.PostsActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    public fun serachTag(touch: View){
        var url: String = "https://api.rule34.xxx/index.php?page=dapi&s=post&q=index&json=1"

        val tags: EditText = findViewById(R.id.editTextText)

        val postsIntent: Intent = Intent(this, PostsActivity::class.java)

        val text: String = tags.text.toString()

        if(text != "") {
            url = "https://api.rule34.xxx/index.php?page=dapi&s=post&q=index&json=1"

            url += "&tags=" + text.split(" ").joinToString(separator = "+")
        }

        postsIntent.putExtra("url", url)

        startActivity(postsIntent)
    }

}
