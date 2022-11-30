package com.example.music_player

import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.os.Binder
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import com.example.music_player.PlayerActivity.Companion.musicService

class MusicService: Service() {

    private var musicBinder = MusicBinder()
    var mediaPlayer: MediaPlayer? = null

    private lateinit var runnable: Runnable
    var currentTime: Int = 0
    var nowPlayingId: Int = 0

    override fun onBind(intent: Intent?): IBinder {

        return musicBinder
    }

    inner class MusicBinder: Binder() {

        fun currentService(): MusicService {

            return this@MusicService
        }
    }

    // Media Player 설정
    fun createMediaPlayer() {

        try {

            if (musicService!!.mediaPlayer == null) {

                musicService!!.mediaPlayer = MediaPlayer()
            }

            else {

                musicService!!.mediaPlayer!!.release()
            }

            musicService!!.mediaPlayer = MediaPlayer.create(baseContext, PlayerActivity.playList[PlayerActivity.musicPosition].musicFile)
            musicService!!.mediaPlayer!!.start()

            PlayerActivity.isPlaying = true

            PlayerActivity.binding.musicPlayBtnPA.setImageResource(R.drawable.music_pause)

            PlayerActivity.binding.seekBarStart.text = formatDuration(mediaPlayer!!.currentPosition.toLong())
            PlayerActivity.binding.seekBarEnd.text = formatDuration(mediaPlayer!!.duration.toLong())
            PlayerActivity.binding.musicSeekBar.progress = 0
            PlayerActivity.binding.musicSeekBar.max = mediaPlayer!!.duration

            nowPlayingId = PlayerActivity.playList[PlayerActivity.musicPosition].musicID

        } catch (e: Exception) {

            return
        }
    }

    // Seek Bar 설정
    fun seekBarSetup() {

        runnable = Runnable {

            PlayerActivity.binding.seekBarStart.text = formatDuration(mediaPlayer!!.currentPosition.toLong())
            PlayerActivity.binding.musicSeekBar.progress = mediaPlayer!!.currentPosition
            Handler(Looper.getMainLooper()).postDelayed(runnable, 200)

            currentTime = mediaPlayer!!.currentPosition
        }

        Handler(Looper.getMainLooper()).postDelayed(runnable, 0)
    }
}