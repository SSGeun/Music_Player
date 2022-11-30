package com.example.music_player

import java.util.concurrent.TimeUnit

data class MusicData (

    var musicID: Int,               // 곡 ID
    val musicFile: Int,             // 곡 파일
    val musicImage: Int,            // 곡 이미지
    val musicName: String,          // 곡 이름
    val musicSinger: String,        // 곡 가수
    )

// Seek Bar 시간 설정 시, 시간 포멧
fun formatDuration (duration: Long): String {

    val minutes = TimeUnit.MINUTES.convert(duration, TimeUnit.MILLISECONDS)
    val seconds = (TimeUnit.SECONDS.convert(duration, TimeUnit.MILLISECONDS) - (minutes * TimeUnit.SECONDS.convert(1, TimeUnit.MINUTES)))

    return String.format("%02d:%02d", minutes, seconds)
}

// 이전 & 다음 음악 시, Play List 범위 검사
fun setMusicPosition(increment: Boolean) {

    // 다음 음악
    if (increment) {

        if ((PlayerActivity.playList.size - 1) == PlayerActivity.musicPosition) {

            PlayerActivity.musicPosition = 0
        }

        else {

            ++PlayerActivity.musicPosition
        }
    }

    // 이전 음악
    else {

        if (PlayerActivity.musicPosition == 0) {

            PlayerActivity.musicPosition = (PlayerActivity.playList.size - 1)
        }

        else {

            --PlayerActivity.musicPosition
        }
    }
}