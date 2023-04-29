package com.example.jeuinfo

import android.graphics.Bitmap
import android.graphics.Matrix

interface Deplacable {
    var image:Bitmap
    fun deplacement(){}
    fun setupImage(x:Float){
        //flip l'image si l'obstacle recule (cohérence de la tete du camion)
        if(x<0){
            val matrix = Matrix().apply { postScale(-1f, 1f, image.width.toFloat() / 2f, image.height.toFloat() / 2f) }
            this.image = Bitmap.createBitmap(this.image, 0, 0, image.width, image.height, matrix, true)
        }
    }
}