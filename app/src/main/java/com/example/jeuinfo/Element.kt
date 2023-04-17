package com.example.jeuinfo

import android.graphics.*


open class Element(x1:Float, y1:Float, largeur:Float,hauteur:Float,image:Bitmap) {
    private val paint = Paint()

    var vitesseCam = 4F

    var image = image
    var x1: Float= x1
    var y1: Float= y1
    var largeur: Float = largeur
    var hauteur : Float = hauteur
    var r = RectF(x1,x1+largeur,y1,y1 + hauteur)
    var dy= 1
    val marge =3

    open fun draw(canvas:Canvas){
        this.r = RectF(x1+marge,y1+marge,x1+largeur-marge,y1+hauteur-marge)
        canvas?.drawBitmap(image,null , Rect(x1.toInt(),y1.toInt(),(x1+largeur).toInt(),(y1+hauteur).toInt()),paint)
    }

    open fun avance(canvas:Canvas){
        this.r.offset(0F, dy*vitesseCam)
        y1+=vitesseCam
        draw(canvas)
    }
}