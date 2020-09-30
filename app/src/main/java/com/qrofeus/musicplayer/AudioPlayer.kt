package com.qrofeus.musicplayer

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.media.MediaPlayer
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.view.View
import android.widget.SeekBar
import android.widget.Toast
import kotlinx.android.synthetic.*
import kotlinx.android.synthetic.main.activity_audio_player.*

class AudioPlayer() : AppCompatActivity() {

    private lateinit var mp: MediaPlayer
    private var playTime: Int = 0
    //var looper : Looper = Looper.myLooper()!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_audio_player)

        Toast.makeText(this, "Audio Player Started", Toast.LENGTH_SHORT).show()

        val filePath = intent.extras?.get("filePath") as String

        songTitle.text = intent.extras?.get("title") as String
        songArtist.text = intent.extras?.get("artist") as String

        mp = MediaPlayer.create(this, Uri.parse(filePath))
        mp.setVolume(0.5f, 0.5f)

        playTime = mp.duration

        mp.setOnCompletionListener {
            playPause.setBackgroundResource(R.drawable.icon_play)
        }

        //Audio Progress Bar
        playbackBar.max = playTime
        playbackBar.setOnSeekBarChangeListener(
            object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(
                    seekBar: SeekBar?,
                    progress: Int,
                    fromUser: Boolean
                ) {
                    if(fromUser) {
                        mp.seekTo(progress)
                    }
                }
                override fun onStartTrackingTouch(seekBar: SeekBar?) {
                }
                override fun onStopTrackingTouch(seekBar: SeekBar?) {
                }
            }
        )

        //Keep updating progress and time labels
        Thread(Runnable(fun() {
            while (mp != null) {
                try {
                    val msg = Message()
                    msg.what = mp.currentPosition
                    handler.sendMessage(msg)
                    Thread.sleep(1000)
                } catch (e: InterruptedException) {
                }
            }
        })).start()

        //Volume Bar
        volumeBar.setOnSeekBarChangeListener(
            object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(
                    seekBar: SeekBar?,
                    progress: Int,
                    fromUser: Boolean
                ) {
                    if(fromUser) {
                        val volumeProgress = progress / 100.0f
                        mp.setVolume(volumeProgress,volumeProgress)
                    }
                }
                override fun onStartTrackingTouch(seekBar: SeekBar?) {
                }
                override fun onStopTrackingTouch(seekBar: SeekBar?) {
                }
            }
        )
    }

    //Progress bar and Time label Updating
    @SuppressLint("HandlerLeak")
    //var handler = object : Handler(looper){
    var handler = object : Handler(){
        override fun handleMessage(msg: Message) {
            val currentPlayback = msg.what
            //Progress Bar update
            playbackBar.progress = currentPlayback

            //Time Label update
            val elapsed = timeLabel(currentPlayback)
            elapsedLabel.text = elapsed

            val remaining = "-" + timeLabel(playTime - currentPlayback)
            remainingLabel.text = remaining
        }
    }

    fun timeLabel(time: Int): String {
        var string: String
        val min = (time/1000/60)
        val seconds = (time/1000 % 60)

        string = "$min:"
        if(seconds < 10)    string += "0"
        string += seconds

        return string
    }

    fun playButton(view: View){
        if(mp.isPlaying){
            mp.pause()
            playPause.setBackgroundResource(R.drawable.icon_play)
        } else {
            mp.start()
            playPause.setBackgroundResource(R.drawable.icon_pause)
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        mp.stop()
    }

    override fun onDestroy() {
        super.onDestroy()
        clearFindViewByIdCache()
    }
}
