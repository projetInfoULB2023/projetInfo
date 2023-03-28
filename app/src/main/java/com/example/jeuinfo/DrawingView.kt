package com.example.jeuinfo

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.SurfaceHolder
import android.view.SurfaceView
import java.util.*

class DrawingView @JvmOverloads constructor (context: Context, attributes: AttributeSet? = null, defStyleAttr: Int = 0): SurfaceView(context, attributes,defStyleAttr),
    SurfaceHolder.Callback,Runnable {
    private val backgroundPaint = Paint()
    private val random = Random()
    private var drawing = true
    lateinit var canvas:Canvas
    lateinit var thread:Thread
    var flolfo = 15
    var robin = 3
    var test = 2
    var matteo = 6
    private fun draw(){
        if(holder.surface.isValid){
            canvas =holder.lockCanvas()
            backgroundPaint.color= Color.WHITE
            canvas?.drawRect(0F,0F,width.toFloat(),height.toFloat(),backgroundPaint)
            //Code pour dessiner ici
            holder.unlockCanvasAndPost(canvas)

        }
    }

    fun pause(){
        drawing = false
        thread.join()
    }

    fun resume(){
        drawing = true
        thread=Thread(this)
        thread.start()
    }

    override fun onTouchEvent(e: MotionEvent): Boolean {
        //S'active quand l'écran est touché
        if (e.action == MotionEvent.ACTION_DOWN) {
            // x et y donnent la position du click, il faudrait encore tester le y par rapport à la position de notre drawingview sur l'écran
            val x = e.rawX
            val y = e.rawY - 800

            println("($x,$y)")
        }
        //invalidate permet de dessiner ce qui a été changé
        invalidate()

        return true
    }

    override fun run() {
        while(drawing){
            draw()
        }
    }

    override fun surfaceCreated(p0: SurfaceHolder) {
        TODO("Not yet implemented")
    }

    override fun surfaceChanged(p0: SurfaceHolder, p1: Int, p2: Int, p3: Int) {
        TODO("Not yet implemented")
    }

    override fun surfaceDestroyed(p0: SurfaceHolder) {
        TODO("Not yet implemented")
    }


}