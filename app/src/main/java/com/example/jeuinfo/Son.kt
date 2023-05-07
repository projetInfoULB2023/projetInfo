package com.example.jeuinfo

// importations
import android.content.Context
import android.media.MediaPlayer
import android.net.Uri
import java.security.KeyStore.TrustedCertificateEntry


// classe Son
class Son(private val context: Context, private val path:Int) {
    private var mediaPlayer=MediaPlayer.create(context,path)
    val soundFileUri = Uri.parse("android.resource://" + "jeuinfo" + "/" + path)


    // fonction d'activation du son
    fun start(){
        mediaPlayer.start()
    }
    fun playing():Boolean{
        return mediaPlayer.isPlaying
    }
    // fonction de mise à l'arrêt du son
    fun stop(){
        mediaPlayer.stop()
        mediaPlayer.prepareAsync()
    }
    fun resume(){
        mediaPlayer.start()
    }
}