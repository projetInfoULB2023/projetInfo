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
    private lateinit var drawingView:DrawingView
    var menuTest = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding =ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetrics)
        val height = displayMetrics.heightPixels
        val width = displayMetrics.widthPixels
        val mediaPlayer : MediaPlayer = MediaPlayer.create(this, R.raw.mort)

        drawingView = binding.vMain
        drawingView.getMediaPlayer(mediaPlayer)
        drawingView.getDimensions(width,height)
        drawingView.setWillNotDraw(false)
        drawingView.invalidate()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onMenuOpened(featureId: Int, menu: Menu): Boolean {
        drawingView.setPause(true)
        return super.onMenuOpened(featureId, menu)
    }

    override fun onPanelClosed(featureId: Int, menu: Menu) {
        if(menuTest == 0) drawingView.setPause(false)
        super.onPanelClosed(featureId, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.about -> {
                menuTest = 1
                Toast.makeText(this, "About Selected", Toast.LENGTH_SHORT).show()
                val builder = AlertDialog.Builder(this)
                builder.setTitle("About")
                builder.setMessage("An app brought to you by Poly polyp inc.")
                builder.apply {
                    setNeutralButton("return", DialogInterface.OnClickListener{ dialog, id ->
                        drawingView.setPause(false)
                        menuTest = 0
                    })
                }
                val alertDialog: AlertDialog = builder.create()
                alertDialog.show()
            }
            R.id.settings -> {
                Toast.makeText(this, "Settings Selected", Toast.LENGTH_SHORT).show()
            }
            R.id.exit -> {
                Toast.makeText(this, "Exit Selected", Toast.LENGTH_SHORT).show()
                exitProcess(0)
            }
        }
        return super.onOptionsItemSelected(item)
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
