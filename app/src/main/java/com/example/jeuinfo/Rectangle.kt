package com.example.jeuinfo

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import java.util.*


class Rectangle(x1:Float,x2:Float,y1:Float,y2:Float,private val vitesse:Float) {
    private val random = Random()
    private val paint = Paint()
    private var color = Color.RED
    val r = RectF(x1,y1,x2,y2)

    var dx:Int= if( random.nextDouble() >0.5 ) 1 else -1
    var dy:Int= if( random.nextDouble() >0.5 ) 1 else -1

    fun draw(canvas:Canvas){
        paint.color = color
        canvas?.drawRect(r,paint)
    }
    fun bouge(canvas:Canvas){
        r.offset(dx*vitesse.toFloat(),dy*vitesse.toFloat())
        draw(canvas)
    }
}
