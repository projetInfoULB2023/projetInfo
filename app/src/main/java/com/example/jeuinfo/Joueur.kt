package com.example.jeuinfo

import android.graphics.Color
import android.media.MediaPlayer

class Joueur(x1:Float,y1:Float,x2:Float,y2:Float,width:Float,height:Float,taillejoueur:Float,deadSound:MediaPlayer) : Element(x1,y1,x2,y2,Color.BLUE) {
    private val width = width
    private val taillejoueur= taillejoueur
    private val height = height
    private val deadSound = deadSound
    fun detectSortieEcran(){
        if(this.x2 > width){
            this.x2 = width
            this.x1 = width-taillejoueur.toFloat()*2
        }else if (this.x1 < 0){
            this.x1 = 0F
            this.x2 = taillejoueur.toFloat()*2
        }
        if(this.y2 > height){
            this.y1 -= taillejoueur*2
            this.y2 -= taillejoueur*2
            deadSound.start()
        }
    }
}