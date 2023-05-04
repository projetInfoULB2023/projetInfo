package com.example.jeuinfo

import android.content.Context
import android.media.MediaPlayer


class Son(private val context: Context,private val path:Int) {
    private var mediaPlayer=MediaPlayer.create(context,path)
    fun start(){
        if (mediaPlayer.isPlaying) {
            mediaPlayer.stop()
        }
        if (mediaPlayer.isPlaying || mediaPlayer.currentPosition > 1) {
            mediaPlayer.reset()
        }
        mediaPlayer.start()

    }
    fun stop(){
        mediaPlayer.stop()
    }
}