package com.example.jeuinfo

import android.graphics.*
import java.util.*


open class Element(x1:Float, y1:Float, largeur:Float,hauteur:Float,color:Int,image:Bitmap) {
    private val random = Random()
    private val paint = Paint()
    private val vitesseCam = 0.5F
    private val image = image
    var color = color
    var x1: Float= x1
    var y1: Float= y1
    var largeur: Float = largeur
    var hauteur : Float = hauteur
    var r = RectF(x1,x1+largeur,y1,y1 + hauteur)
    var dy= 1

    open fun draw(canvas:Canvas){
        this.r = RectF(x1,y1,x1+largeur,y1+hauteur)
        paint.color = this.color
        canvas?.drawRect(this.r,paint)
        canvas?.drawBitmap(image,x1,y1,paint)
    }

    open fun avance(canvas:Canvas){
        this.r.offset(0F, dy*vitesseCam)
        y1+=vitesseCam
        draw(canvas)
    }
}