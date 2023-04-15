package com.example.jeuinfo

import android.graphics.*
import kotlin.random.Random

class ObstacleMouvant(
    x1:Float,
    y1:Float,
    largeur:Float,
    hauteur:Float,
    vitesse:Float,
    width:Float,
    image: Bitmap
) : Element(x1,y1,largeur,hauteur,
    Color.GREEN,image) {
    val width = width
    val vitesse = vitesse
    var dx = if(Random.nextFloat() > 0.5) 1 else -1

    override fun avance(canvas: Canvas) {
        //Mouvement horizontal en plus
        deplacement()
        super.avance(canvas)
    }

    fun deplacement(){
        //Permet le rebond
        dx = if(this.x1+largeur >= width ) -1 else if (this.x1 <= 0) 1 else dx
        this.r.offset(dx*vitesse,0F)
        this.x1 += dx*vitesse
    }
}