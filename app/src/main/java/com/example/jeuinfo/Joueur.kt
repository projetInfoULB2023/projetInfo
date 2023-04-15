package com.example.jeuinfo

import android.graphics.Color
import android.graphics.Rect
import android.graphics.RectF
import android.media.MediaPlayer

class Joueur(x1:Float,y1:Float,largeur:Float,hauteur:Float,width:Float,height:Float,taillejoueur:Float,deadSound:MediaPlayer) : Element(x1,y1,largeur,hauteur,Color.BLUE) {
    private val width = width
    private val taillejoueur= taillejoueur
    private val height = height
    private val deadSound = deadSound
    fun detectSortieEcran(){
        if(this.x1 + largeur > width){
            this.x1 = width - largeur
        }else if (this.x1 < 0){
            this.x1 = 0F
        }
        if(this.y1 + hauteur > height){
            this.y1 -= taillejoueur*2
            deadSound.start()
        }
    }
}