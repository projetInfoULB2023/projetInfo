package com.example.jeuinfo

// importations
import android.graphics.Bitmap
import android.graphics.Matrix

/*
 description des comportements et attributs obligatoires pour toute classe implémentant Deplacable (éléments du jeu pouvant être sujets à un déplacement)
 */
interface Deplacable {
    var image:Bitmap
    fun deplacement(){}
}