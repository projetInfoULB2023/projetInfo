package com.example.jeuinfo

import android.graphics.Color

class Joueur(x1:Float,x2:Float,x3:Float,x4:Float,width:Float,taillejoueur:Int) : Element(x1,x2,x3,x4,Color.BLUE) {
    private val width = width
    private val taillejoueur= taillejoueur
    fun detectSortieEcran(){
        if(this.x2 > width){
            this.x2 = width
            this.x1 = width-taillejoueur.toFloat()*2
        }else if (this.x1 < 0){
            this.x1 = 0F
            this.x2 = taillejoueur.toFloat()*2
        }
    }
}