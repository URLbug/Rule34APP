package com.example.rule34.adapters

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.os.Handler
import android.os.Looper
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.MediaController
import android.widget.Toast
import android.widget.VideoView
import androidx.recyclerview.widget.RecyclerView
import com.example.rule34.R
import com.example.rule34.models.PostsModel
import com.squareup.picasso.Picasso
import java.io.BufferedInputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL
import java.util.concurrent.Executors

class RecylerAdapter(val context: Context, var data: List<PostsModel>) : RecyclerView.Adapter<RecylerAdapter.CREATOR>() {

    val inflater: LayoutInflater = LayoutInflater.from(context)
    var mediaController: MediaController = MediaController(context)

    val myExecutor = Executors.newSingleThreadExecutor()
    val myHandler = Handler(Looper.getMainLooper())

    private fun mSaveMediaToStorage(bitmap: Bitmap?, type: String) {
        val filename = "${System.currentTimeMillis()}." + type

        var fos: OutputStream? = null

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            context.contentResolver?.also { resolver ->
                val contentValues = ContentValues().apply {
                    put(MediaStore.MediaColumns.DISPLAY_NAME, filename)
                    put(MediaStore.MediaColumns.MIME_TYPE, if(type == "jpg") "image/jpg" else "video/mp4")
                    put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)
                }
                val imageUri: Uri? = resolver.insert(if(type == "jpg") MediaStore.Images.Media.EXTERNAL_CONTENT_URI else MediaStore.Video.Media.EXTERNAL_CONTENT_URI, contentValues)

                fos = imageUri?.let { resolver.openOutputStream(it) }
            }
        } else {
            val imagesDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
            val image = File(imagesDir, filename)

            fos = FileOutputStream(image)
        }
        fos?.use {
            bitmap?.compress(Bitmap.CompressFormat.JPEG , 100, it)
            Toast.makeText(context , "Saved to Gallery" , Toast.LENGTH_SHORT).show()
        }

        if(fos == null)
            Toast.makeText(context , "ERROR" , Toast.LENGTH_SHORT).show()
    }

    private fun mLoad(string: String): Bitmap? {
        val url: URL = mStringToURL(string)!!

        val connection: HttpURLConnection?

        try {
            connection = url.openConnection() as HttpURLConnection
            connection.connect()

            val inputStream: InputStream = connection.inputStream
            val bufferedInputStream = BufferedInputStream(inputStream)

            return BitmapFactory.decodeStream(bufferedInputStream)
        } catch (e: IOException) {
            e.printStackTrace()

            Toast.makeText(context, "Error", Toast.LENGTH_LONG).show()
        }
        return null
    }

    // Function to convert string to URL
    private fun mStringToURL(string: String): URL? {
        try {
            return URL(string)
        } catch (e: MalformedURLException) {
            e.printStackTrace()
        }
        return null
    }

    class CREATOR (itemView: View) : RecyclerView.ViewHolder(itemView) {
        val image: ImageView = itemView.findViewById(R.id.imageView)

        val video: VideoView = itemView.findViewById(R.id.videoView)

        val download: Button = itemView.findViewById(R.id.download)
    }

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

        holder.download.setOnClickListener {
            myExecutor.execute {
                val mImage = mLoad(state.file_url)
                myHandler.post {
                    if (mImage != null) {
                        mSaveMediaToStorage(mImage, if (".mp4" !in state.file_url) "jpg" else "mp4")
                    }
                }
            }
        }

        if (".mp4" !in state.file_url) {
            holder.video.visibility = View.INVISIBLE

            Picasso.get().load(state.file_url).into(holder.image)
        } else {
            holder.image.visibility = View.INVISIBLE

            holder.video.setVideoURI(Uri.parse(state.file_url))

            holder.video.setMediaController(mediaController)

            mediaController.setMediaPlayer(holder.video)
        }
    }
}