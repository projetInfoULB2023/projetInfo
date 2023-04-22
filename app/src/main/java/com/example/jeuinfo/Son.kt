package com.example.jeuinfo

import android.content.Context
import android.media.MediaPlayer

class Son(private val context: Context,private val path:Int) {
    private var mediaPlayer=MediaPlayer.create(context,path)
    fun start(){
        mediaPlayer?.start()
    }
    fun stop(){
        mediaPlayer?.stop()
    }

}