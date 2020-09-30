package com.qrofeus.musicplayer

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Toast
import androidx.core.app.ActivityCompat
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.song_desc.view.*

class MainActivity : AppCompatActivity() {

    private var songsList = ArrayList<SongDesc>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if(ActivityCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),111)
        } else
            loadSongs()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode==111 && grantResults[0]==PackageManager.PERMISSION_GRANTED) {
            loadSongs()
        }
    }

    //Call Media Player for selected song
    @SuppressLint("InlinedApi") //Suppresses this -> @RequiresApi(Build.VERSION_CODES.R)
    private fun loadSongs() {
        //External Storage URI (Uniform Resource Identifier)
        val uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
        val select = MediaStore.Audio.Media.IS_MUSIC + "!=0"

        //Sort through all files from External for Music files
        val rs = contentResolver.query(uri, null, select, null, null)
        if(rs!=null){
            while(rs.moveToNext()){
                val path = rs.getString(rs.getColumnIndex(MediaStore.Audio.Media.DATA))
                val title = rs.getString(rs.getColumnIndex(MediaStore.Audio.Media.TITLE))
                val artist = rs.getString(rs.getColumnIndex(MediaStore.Audio.Media.ARTIST))
                songsList.add(SongDesc(path, title, artist))
            }
        }
        rs?.close()
        val adapter = SongAdapter(songsList)
        list_songs.adapter = adapter

        list_songs.setOnItemClickListener { _, _, position, _ ->
            Toast.makeText(this, "Item Clicked", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, AudioPlayer::class.java)
                .putExtra("filePath", songsList[position].path).putExtra("title", songsList[position].title)
                .putExtra("artist", songsList[position].artist)
            startActivity(intent)
        }
    }

    //Adapter Class - Song_ArrayList to ListView
    inner class SongAdapter : BaseAdapter{
        private var listofSongs = ArrayList<SongDesc>()
        constructor(listofSongs: ArrayList<SongDesc>) : super() {
            this.listofSongs = listofSongs
        }

        override fun getCount(): Int {
            return listofSongs.size
        }

        override fun getItem(position: Int): Any {
            return listofSongs[position]
        }

        override fun getItemId(position: Int): Long {
            return position.toLong()
        }

        @SuppressLint("ViewHolder")
        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
            val myView : View = layoutInflater.inflate(R.layout.song_desc,null)
            val song = listofSongs[position]
            myView.songName.text = listofSongs[position].title
            myView.songArtist.text = listofSongs[position].artist
            return myView
        }
    }
}