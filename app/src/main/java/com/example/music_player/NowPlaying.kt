package com.example.music_player

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.music_player.databinding.FragmentNowPlayingBinding

class NowPlaying : Fragment(), MediaPlayer.OnCompletionListener {

    private var listener: OnFragmentInteractionListener? = null

    companion object {

        @SuppressLint("StaticFieldLeak")
        lateinit var binding: FragmentNowPlayingBinding
    }

    override fun onAttach(context: Context) {

        super.onAttach(context)

        if (context is OnFragmentInteractionListener) {

            listener = context
        }

        else {

            throw RuntimeException(context.toString())
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.fragment_now_playing, container, false)
        binding = FragmentNowPlayingBinding.bind(view)
        binding.root.visibility = View.GONE

        // 음악 재생 & 일시정지, 버튼 클릭 이벤트
        binding.musicPlayBtnNP.setOnClickListener {

            if (PlayerActivity.isPlaying) {

                pauseMusic()
            }

            else {

                playMusic()
            }
        }

        // 다음 음악, 버튼 클릭 이벤트
        binding.musicNextBtnNP.setOnClickListener {

            listener?.onFragmentInteraction(changed = true)

            setMusicPosition(increment = true)

            PlayerActivity.musicService!!.createMediaPlayer()
            PlayerActivity.musicService!!.mediaPlayer!!.setOnCompletionListener(this)

            Glide.with(this)
                .load(PlayerActivity.playList[PlayerActivity.musicPosition].musicImage)
                .into(binding.musicImageNP)

            playMusic()
        }



        // 이전 음악, 버튼 클릭 이벤트
        binding.musicPreviousBtnNP.setOnClickListener {

            listener?.onFragmentInteraction(changed = true)

            setMusicPosition(increment = false)

            PlayerActivity.musicService!!.createMediaPlayer()
            PlayerActivity.musicService!!.mediaPlayer!!.setOnCompletionListener(this)

            Glide.with(this)
                .load(PlayerActivity.playList[PlayerActivity.musicPosition].musicImage)
                .into(binding.musicImageNP)

            playMusic()
        }

        // Fragment 클릭 시, Player Activity로 이동
        binding.root.setOnClickListener {

            val intent = Intent(requireContext(), PlayerActivity::class.java)
            intent.putExtra("position", PlayerActivity.musicPosition)
            intent.putExtra("class", "NowPlaying")
            ContextCompat.startActivity(requireContext(), intent, null)
        }

        return view
    }

    override fun onResume() {

        super.onResume()

        if (PlayerActivity.musicService != null) {

            binding.root.visibility = View.VISIBLE

            Glide.with(this)
                .load(PlayerActivity.playList[PlayerActivity.musicPosition].musicImage)
                .into(binding.musicImageNP)

            if (PlayerActivity.isPlaying) {

                binding.musicPlayBtnNP.setImageResource(R.drawable.music_pause)

                PlayerActivity.musicService!!.mediaPlayer!!.setOnCompletionListener(this)
            }

            else {

                binding.musicPlayBtnNP.setImageResource(R.drawable.music_play)
            }
        }
    }

    // 음악 재생
    private fun playMusic() {

        PlayerActivity.musicService!!.mediaPlayer!!.start()
        binding.musicPlayBtnNP.setImageResource(R.drawable.music_pause)
        PlayerActivity.binding.musicNextBtnPA.setImageResource(R.drawable.music_pause)
        PlayerActivity.isPlaying = true
    }

    // 음악 일시정지
    private fun pauseMusic() {

        PlayerActivity.musicService!!.mediaPlayer!!.pause()
        binding.musicPlayBtnNP.setImageResource(R.drawable.music_play)
        PlayerActivity.binding.musicNextBtnPA.setImageResource(R.drawable.music_play)
        PlayerActivity.isPlaying = false
    }

    // Media Player 완료 시
    override fun onCompletion(mp: MediaPlayer?) {

        setMusicPosition(increment = true)

        try {

            Glide.with(this)
                .load(PlayerActivity.playList[PlayerActivity.musicPosition].musicImage)
                .into(binding.musicImageNP)

        } catch (e: Exception) {

            return
        }

        playMusic()
    }

    interface OnFragmentInteractionListener {

        fun onFragmentInteraction(changed: Boolean)
    }
}