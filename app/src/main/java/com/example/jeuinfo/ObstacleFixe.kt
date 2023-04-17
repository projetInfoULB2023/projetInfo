package com.example.jeuinfo

import android.graphics.Bitmap
import android.graphics.Canvas

class ObstacleFixe(x1:Float,
                   y1:Float,
                   largeur:Float,
                   hauteur:Float,
                   width:Float,
                   image: Bitmap
) : Obstacle(x1,y1,largeur,hauteur,0F,width,image){
    override fun deplacement() {
    }

}