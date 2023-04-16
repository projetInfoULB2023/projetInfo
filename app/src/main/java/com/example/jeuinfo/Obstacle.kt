package com.example.jeuinfo

import android.graphics.*
import kotlin.random.Random

open class Obstacle(
    x1:Float,
    y1:Float,
    largeur:Float,
    hauteur:Float,
    vitesse:Float,
    width:Float,
    image: Bitmap
) : Element(x1,y1,largeur,hauteur, image) {

    val width = width
    val vitesse = vitesse
    var dx = if(Random.nextFloat() > 0.5) 1 else -1
    var imageSetup = false

    private fun setupImage(){
        //flip l'image si l'obstacle recule (cohérence de la tete du camion)
        if(dx==-1){
            val matrix = Matrix().apply { postScale(-1f, 1f, image.width.toFloat() / 2f, image.height.toFloat() / 2f) }
            this.image = Bitmap.createBitmap(this.image, 0, 0, image.width, image.height, matrix, true)
        }
        imageSetup=true
    }

    private fun deplacement(){
        //Permet le rebond

        //dx = if(this.x1+largeur >= width ) -1 else if (this.x1 <= 0) 1 else dx
        //this.r.offset(dx*vitesse,0F)
        //this.x1 += dx*vitesse

        //Permet la réapparition de l'autre coté de l'écran
        if(this.x1>=width && dx > 0){
            this.x1=-largeur
        }
        else if(this.x1+largeur< 0 && dx <0){
            this.x1 = width
        }

        this.r.offset(dx*vitesse,0F)
        this.x1 +=dx*vitesse
    }

    override fun avance(canvas: Canvas) {
        //Mouvement horizontal en plus
        if(!imageSetup) setupImage()
        deplacement()
        super.avance(canvas)
    }
}
