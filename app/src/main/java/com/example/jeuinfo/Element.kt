package com.example.jeuinfo

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import java.util.*


open class Element(x1:Float, y1:Float, x2:Float, y2:Float,color:Int) {
    private val random = Random()
    private val paint = Paint()
    private val vitesseCam = 0.5F
    var color = color
    var x1: Float= x1
    var y1: Float= y1
    var x2: Float= x2
    var y2: Float= y2
    var r = RectF(x1,x2,y1,y2)
    var dy= 1

    fun draw(canvas:Canvas){
        this.r = RectF(x1,y1,x2,y2)
        paint.color = this.color
        canvas?.drawRect(this.r,paint)
    }

    open fun avance(canvas:Canvas){
        this.r.offset(0F, dy*vitesseCam)
        y1+=vitesseCam
        y2+=vitesseCam
        draw(canvas)
    }
}
