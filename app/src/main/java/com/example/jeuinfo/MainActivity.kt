package com.example.jeuinfo

import android.app.AlertDialog
import android.content.DialogInterface
import android.media.MediaPlayer
import android.os.Bundle
import android.provider.MediaStore.Audio.Media
import android.util.DisplayMetrics
import androidx.appcompat.app.AppCompatActivity
import com.example.jeuinfo.databinding.ActivityMainBinding
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import kotlin.system.exitProcess



class MainActivity : AppCompatActivity() {
    private lateinit var drawingView: DrawingView

    // val vitesseTemp = Element.vitesseCam
    var menuTest = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetrics)
        val height = displayMetrics.heightPixels
        val width = displayMetrics.widthPixels
        val mediaPlayer: MediaPlayer = MediaPlayer.create(this, R.raw.mort)

        drawingView = binding.vMain
        drawingView.getMediaPlayer(mediaPlayer)
        drawingView.getDimensions(width, height)
        drawingView.setWillNotDraw(false)
        drawingView.invalidate()
    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu,menu)
        return super.onCreateOptionsMenu(menu)
    }
    override fun onPause() {
        super.onPause()
        drawingView.pause()
    }



    override fun onResume() {
        super.onResume()
        drawingView.resume()
    }
}
