package com.example.music_player

import android.annotation.SuppressLint
import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.media.MediaPlayer
import android.os.Bundle
import android.os.IBinder
import android.widget.SeekBar
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.music_player.databinding.PlayerActivityBinding

class PlayerActivity : AppCompatActivity(), ServiceConnection, MediaPlayer.OnCompletionListener {

    companion object {

        lateinit var playList: ArrayList<MusicData>
        var musicPosition: Int = 0
        var isPlaying: Boolean = false
        var musicService: MusicService? = null

        @SuppressLint("StaticFieldLeak")
        lateinit var binding: PlayerActivityBinding
    }

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        binding = PlayerActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initializeLayout()

        // 음악 재생 & 일시정지, 버튼 클릭 이벤트
        binding.musicPlayBtnPA.setOnClickListener {

            if (isPlaying) {

                pauseMusic()
            }

            else {

                playMusic()
            }
        }

        // 이전 음악, 버튼 클릭 이벤트
        binding.musicPreviousBtnPA.setOnClickListener {

            prevNextMusic(increment = false)
        }

        // 다음 음악, 버튼 클릭 이벤트
        binding.musicNextBtnPA.setOnClickListener {

            prevNextMusic(increment = true)
        }

        // 30초 전, 버튼 클릭 이벤트
        binding.musicReplayBtnPA.setOnClickListener {

            replayForwardMusic(increment = false)
        }

        // 30초 후, 버튼 클릭 이벤트
        binding.musicForwardBtnPA.setOnClickListener {

            replayForwardMusic(increment = true)
        }

        // 접기, 버튼 클릭 이벤트
        binding.playerFoldBtnPA.setOnClickListener {

            finish()
        }

        // Seek Bar 드래그 이벤트
        binding.musicSeekBar.setOnSeekBarChangeListener (object: SeekBar.OnSeekBarChangeListener {

            override fun onProgressChanged(p0: SeekBar?, pos: Int, changed: Boolean) {

                if (changed) {

                    musicService!!.mediaPlayer!!.seekTo(pos)
                }
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {

            }

            override fun onStopTrackingTouch(p0: SeekBar?) {

            }

        })
    }

    // Layout 설정
    private fun setLayout() {

        Glide.with(this)
            .load(playList[musicPosition].musicImage)
            .into(binding.musicImagePA)
        binding.musicNamePA.text = playList[musicPosition].musicName
        binding.musicSingerPA.text = playList[musicPosition].musicSinger
    }

    // Layout 초기화
    private fun initializeLayout() {

        musicPosition = intent.getIntExtra("position", 0)

        when (intent.getStringExtra("class")) {

            "PlayListAdapter" -> {

                // For Starting Service
                val intent = Intent(this, MusicService::class.java)
                bindService(intent, this, BIND_AUTO_CREATE)
                startService(intent)

                playList = ArrayList()
                playList.addAll(PlayListActivity.playList)
                setLayout()
            }

            "NowPlaying" -> {

                setLayout()

                binding.seekBarStart.text = formatDuration(musicService!!.mediaPlayer!!.currentPosition.toLong())
                binding.seekBarEnd.text = formatDuration(musicService!!.mediaPlayer!!.duration.toLong())
                binding.musicSeekBar.progress = musicService!!.mediaPlayer!!.currentPosition
                binding.musicSeekBar.max = musicService!!.mediaPlayer!!.duration

                if (isPlaying) {

                    binding.musicPlayBtnPA.setImageResource(R.drawable.music_pause)
                }

                else {

                    binding.musicPlayBtnPA.setImageResource(R.drawable.music_play)
                }
            }
        }
    }

    // 음악 재생
    private fun playMusic() {

        binding.musicPlayBtnPA.setImageResource(R.drawable.music_pause)
        isPlaying = true
        musicService!!.mediaPlayer!!.start()
    }

    // 음악 일시 정지
    private fun pauseMusic() {

        binding.musicPlayBtnPA.setImageResource(R.drawable.music_play)
        isPlaying = false
        musicService!!.mediaPlayer!!.pause()
    }

    // 이전 & 다음 음악
    private fun prevNextMusic(increment: Boolean) {

        musicService!!.mediaPlayer!!.stop()

        // 다음 음악
        if (increment) {

            setMusicPosition(increment = true)
            setLayout()
            musicService!!.createMediaPlayer()
            musicService!!.mediaPlayer!!.setOnCompletionListener(this)
        }

        // 이전 음악
        else {

            setMusicPosition(increment = false)
            setLayout()
            musicService!!.createMediaPlayer()
            musicService!!.mediaPlayer!!.setOnCompletionListener(this)
        }
    }

    // 30초 전 & 후
    private fun replayForwardMusic(increment: Boolean) {

        // 30초 후
        if (increment) {
            
            setMusicTime(increment = true)
        }

        // 30초 전
        else {

            setMusicTime(increment = false)
        }
    }

    // 30초 전 & 후, 현재 시간 범위 검사
    private fun setMusicTime(increment: Boolean) {

        if (increment) {

            if (musicService!!.mediaPlayer!!.currentPosition + 30000 >= musicService!!.mediaPlayer!!.duration) {

                musicService!!.currentTime = musicService!!.mediaPlayer!!.duration
            }

            else {

                musicService!!.currentTime += 30000
            }

            musicService!!.mediaPlayer!!.seekTo(musicService!!.currentTime)
        }

        else {

            if (musicService!!.currentTime - 30000 <= 0) {

                musicService!!.currentTime = 0
            }

            else {

                musicService!!.currentTime -= 30000
            }

            musicService!!.mediaPlayer!!.seekTo(musicService!!.currentTime)
        }
    }

    override fun onServiceConnected(name: ComponentName?, service: IBinder?) {

        val binder = service as MusicService.MusicBinder
        musicService = binder.currentService()

        musicService!!.createMediaPlayer()
        musicService!!.mediaPlayer!!.setOnCompletionListener(this)
        musicService!!.seekBarSetup()
    }

    override fun onServiceDisconnected(name: ComponentName?) {

        musicService = null
    }

    // Media Player 완료 시
    override fun onCompletion(mp: MediaPlayer?) {

        setMusicPosition(increment = true)
        musicService!!.createMediaPlayer()
        musicService!!.mediaPlayer!!.setOnCompletionListener(this)

        try {

            setLayout()
        } catch (e: Exception) {

            return
        }
    }
}