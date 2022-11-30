package com.example.music_player

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.music_player.databinding.PlayListRecyclerViewItemBinding

class PlayListAdapter(private val context: Context, private val playList: ArrayList<MusicData>):
    RecyclerView.Adapter<PlayListAdapter.PlayListViewHolder>() {

    class PlayListViewHolder(binding: PlayListRecyclerViewItemBinding): RecyclerView.ViewHolder(binding.root) {

        val musicImage = binding.musicImagePLA
        val musicName = binding.musicNamePLA
        val musicSinger = binding.musicSingerPLA
        val root = binding.root
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlayListViewHolder {

        return PlayListViewHolder(PlayListRecyclerViewItemBinding.inflate(LayoutInflater.from(context), parent, false))
    }

    override fun onBindViewHolder(holder: PlayListViewHolder, position: Int) {

        Glide.with(context)
            .load(playList[position].musicImage)
            .into(holder.musicImage)
        holder.musicName.text = playList[position].musicName
        holder.musicSinger.text = playList[position].musicSinger

        // 현재 재생 중인 음악의 제목 & 가수 색상 변경
        if (playList[position].musicID == PlayerActivity.musicService?.nowPlayingId) {

            holder.musicName.setTextColor(ContextCompat.getColor(context, R.color.selected_music))
            holder.musicSinger.setTextColor(ContextCompat.getColor(context, R.color.selected_music))
        }

        // 현재 재생 중인 음악 외 제목 & 가수 색상 변경
        else {

            holder.musicName.setTextColor(ContextCompat.getColor(context, R.color.music_name))
            holder.musicSinger.setTextColor(ContextCompat.getColor(context, R.color.music_singer))
        }

        holder.root.setOnClickListener {

            when {

                // 현재 재생 중인 음악과 같은 음악을 클릭했을 때
                (playList[position].musicID == PlayerActivity.musicService?.nowPlayingId) -> {

                    val intent = Intent(context, PlayerActivity::class.java)
                    intent.putExtra("position", PlayerActivity.musicPosition)
                    intent.putExtra("class", "NowPlaying")
                    ContextCompat.startActivity(context, intent, null)
                }

                // 현재 재생 중인 음악 없이 클릭했을 때
                else -> {

                    val intent = Intent(context, PlayerActivity::class.java)
                    intent.putExtra("position", position)
                    intent.putExtra("class", "PlayListAdapter")
                    ContextCompat.startActivity(context, intent, null)
                }
            }
        }
    }

    override fun getItemCount(): Int {

        return playList.size
    }
}