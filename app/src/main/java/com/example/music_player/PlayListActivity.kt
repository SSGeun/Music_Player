package com.example.music_player

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.music_player.databinding.PlayListActivityBinding

class PlayListActivity: AppCompatActivity(), NowPlaying.OnFragmentInteractionListener {

    // Binding Object 생성
    private lateinit var binding: PlayListActivityBinding
    private lateinit var playListAdapter: PlayListAdapter

    companion object {

        var playList: ArrayList<MusicData> = ArrayList()
    }

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        initializeLayout()
    }

    private fun initializeLayout() {

        // Binding 초기화
        binding = PlayListActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        playList = getPlayList()

        binding.playListRV.setHasFixedSize(true)
        playListAdapter = PlayListAdapter(this@PlayListActivity, playList)
        binding.playListRV.adapter = playListAdapter
    }

    private fun getPlayList(): ArrayList<MusicData> {

        val playList = ArrayList<MusicData>()

        playList.apply {

            add(MusicData(musicID = 1, musicFile = R.raw.assaf_ayalon__in_pulse, musicImage = R.drawable.assaf_ayalon__in_pulse, musicName = "In Pulse", musicSinger = "Assaf Ayalon"))
            add(MusicData(musicID = 2, musicFile = R.raw.ben_fox__driving_solo, musicImage = R.drawable.ben_fox__driving_solo, musicName = "Driving Solo", musicSinger = "Ben Fox"))
            add(MusicData(musicID = 3, musicFile = R.raw.cristof__the_journey_we_make, musicImage = R.drawable.cristof__the_journey_we_make, musicName = "The Journey We Make", musicSinger = "Cristof Walters"))
            add(MusicData(musicID = 4, musicFile = R.raw.elad_perez__beautiful_day, musicImage = R.drawable.elad_perez__beautiful_day, musicName = "Beautiful Day", musicSinger = "Elad Perez"))
            add(MusicData(musicID = 5, musicFile = R.raw.jamie_rutherford__far_away_feelings, musicImage = R.drawable.jamie_rutherford__far_away_feelings, musicName = "Far Away Feelings", musicSinger = "Jamie Rutherford"))
            add(MusicData(musicID = 6, musicFile = R.raw.leva__quiet_wind, musicImage = R.drawable.leva__quiet_wind, musicName = "Quiet Wind", musicSinger = "Leva"))
            add(MusicData(musicID = 7, musicFile = R.raw.leva__someday, musicImage = R.drawable.leva__someday, musicName = "Someday", musicSinger = "Leva"))
            add(MusicData(musicID = 8, musicFile = R.raw.marcelo_nami__amor_sem_medidas, musicImage = R.drawable.marcelo_nami__amor_sem_medidas, musicName = "Amor sem Medidas", musicSinger = "Marcelo Nami"))
            add(MusicData(musicID = 9, musicFile = R.raw.mark_grundhoefer__waltz_for_a_frog, musicImage = R.drawable.mark_grundhoefer__waltz_for_a_frog, musicName = "Waltz for a Frog", musicSinger = "Mark Grundhoefer"))
            add(MusicData(musicID = 10, musicFile = R.raw.moze__apple_hill, musicImage = R.drawable.moze__apple_hill, musicName = "Apple Hill", musicSinger = "MOZE"))
            add(MusicData(musicID = 11, musicFile = R.raw.paper_planes__campfire_calm, musicImage = R.drawable.paper_planes__campfire_calm, musicName = "Campfire Calm", musicSinger = "Paper Planes"))
            add(MusicData(musicID = 12, musicFile = R.raw.paper_planes__morning_color, musicImage = R.drawable.paper_planes__morning_color, musicName = "Morning Color", musicSinger = "Paper Planes"))
            add(MusicData(musicID = 13, musicFile = R.raw.prophecy_playground__north_bound_wreck, musicImage = R.drawable.prophecy_playground__north_bound_wreck, musicName = "North Bound Wreck", musicSinger = "Prophecy Playground"))
            add(MusicData(musicID = 14, musicFile = R.raw.roie_shpigler__sky_surf, musicImage = R.drawable.roie_shpigler__sky_surf, musicName = "Sky Surf", musicSinger = "Roie Shpigler"))
            add(MusicData(musicID = 15, musicFile = R.raw.russo__last_round, musicImage = R.drawable.russo__last_round, musicName = "Last Round", musicSinger = "Russo"))
            add(MusicData(musicID = 16, musicFile = R.raw.sand_diver__daybreak_ritual, musicImage = R.drawable.sand_diver__daybreak_ritual, musicName = "Daybreak Ritual", musicSinger = "Sand Diver"))
        }

        return playList
    }

    override fun onRestart() {

        super.onRestart()

        binding.playListRV.adapter = playListAdapter
    }

    override fun onFragmentInteraction(changed: Boolean) {

        Log.e("CHANGED: ", changed.toString())

        if (changed) {

            binding.playListRV.adapter = playListAdapter
        }
    }


}