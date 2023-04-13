package com.example.jeuinfo

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.RectF
import kotlin.random.Random

class ObstacleMouvant(x1:Float,y1:Float,x2:Float,y2:Float,vitesse:Float,width:Float) : Element(x1,y1,x2,y2,
    Color.GREEN) {
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
        dx = if(this.x2 >= width ) -1 else if (this.x1 <= 0) 1 else dx
        this.r.offset(dx*vitesse,0F)
        this.x1 += dx*vitesse
        this.x2 += dx*vitesse
    }

}