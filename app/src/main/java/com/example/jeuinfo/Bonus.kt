package com.example.jeuinfo

import android.graphics.Bitmap
import kotlin.random.Random

class Bonus(x1:Float,
            y1:Float,
            largeur:Float,
            hauteur:Float,
            private val vitesse:Float,
            private val width:Float,
            image: Bitmap
): Element(x1,y1,largeur,hauteur, image),Deplacable,CollisionImportante {
    override var imageSetup = false
    private var dx = if(Random.nextFloat()>0.5) 1 else -1

    override fun deplacement() {
        //Permet le rebond
        dx = if(this.x1+largeur >= width ) -1 else if (this.x1 <= 0) 1 else dx
        this.r.offset(dx*vitesse,0F)
        this.x1 += dx*vitesse
    }

    override fun setupImage() {
        TODO("Not yet implemented")
    }

    override fun collision(joueur: Joueur) {
        TODO("Not yet implemented")
    }
}