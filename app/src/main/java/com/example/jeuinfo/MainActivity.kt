package com.example.jeuinfo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.jeuinfo.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var drawingView:DrawingView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding =ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        drawingView = binding.vMain
        drawingView.setWillNotDraw(false)
        drawingView.invalidate()
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
