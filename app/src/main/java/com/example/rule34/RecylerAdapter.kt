package com.example.rule34

import android.content.Context
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso

class RecylerAdapter(val context: Context, var data: List<PostsModel>) : RecyclerView.Adapter<RecylerAdapter.CREATOR>() {

    class CREATOR (itemView: View) : RecyclerView.ViewHolder(itemView) {}

    val inflater = LayoutInflater.from(context)

    @Override
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CREATOR {
        val binding = inflater.inflate(R.layout.item, parent, false)

        return CREATOR(binding)
    }

    @Override
    override fun getItemCount(): Int = data.size

    @Override
    override fun onBindViewHolder(holder: CREATOR, position: Int) {
        val state: PostsModel = data[position]

        val image: ImageView = holder.itemView.findViewById(R.id.imageView)

        Picasso.get().load(state.file_url).into(image)
    }

}