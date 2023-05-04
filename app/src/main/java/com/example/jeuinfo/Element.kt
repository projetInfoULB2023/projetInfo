package com.example.jeuinfo

import android.graphics.*


open class Element(var x1:Float, var y1:Float, var largeur:Float,var hauteur:Float,var image:Bitmap) : Observer{
    private val paint = Paint()
    companion object {
        var vitesseCam = 0F
    }
    var r = RectF(x1,x1+largeur,y1,y1 + hauteur)
    private var dy= 1
    protected val marge =3
    fun draw(canvas:Canvas){
        this.r = RectF(x1+marge,y1+marge,x1+largeur-marge,y1+hauteur-marge)
        canvas?.drawBitmap(image,null , Rect(x1.toInt(),y1.toInt(),(x1+largeur).toInt(),(y1+hauteur).toInt()),paint)
    }
    override fun update(canvas:Canvas){
        this.r.offset(0F, dy*vitesseCam)
        y1+=vitesseCam
        draw(canvas)
    }
}