package com.example.jeuinfo

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.RectF
import kotlin.math.abs
import kotlin.random.Random

open class Bonus(x1:Float,
                 y1:Float,
                 largeur:Float,
                 hauteur:Float,
                 val vitesseX:Float,
                 val vitesseY:Float,
                 image: Bitmap
): Element(x1,y1,largeur,hauteur, image),Deplacable {
    private var dx = if(Random.nextFloat()>0.5) 1 else -1
    private var dy = if(Random.nextFloat()>0.5) 1 else -1
    override fun deplacement() {
        //Permet le rebond
        if(this.x1+largeur>=DrawingView.Cwidth || this.x1<=0){
            dx*=-1
        }
        if(this.y1 < 0 || this.y1+hauteur > DrawingView.Cheight){
            dy*=-1
        }
        this.r.offset(dx*vitesseX,dy*vitesseY)
        this.x1+=dx*vitesseX
        this.y1+=dy*vitesseY
    }
    override fun avance(canvas: Canvas){
        this.deplacement()
        super.avance(canvas)
    }
}