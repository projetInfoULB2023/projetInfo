package com.example.jeuinfo

// importations
import android.content.Context
import android.media.MediaPlayer



// classe Son
class Son(private val context: Context, private val path:Int) {
    private var mediaPlayer=MediaPlayer.create(context,path)

    // fonction d'activation du son
    fun start(){
        if (mediaPlayer.isPlaying) {
            mediaPlayer.stop()
        }
        if (mediaPlayer.isPlaying || mediaPlayer.currentPosition > 1) {
            mediaPlayer.reset()
        }
        mediaPlayer.start()

    }



    // fonction de mise à l'arrêt du son
    fun stop(){
        mediaPlayer.stop()
    }
}