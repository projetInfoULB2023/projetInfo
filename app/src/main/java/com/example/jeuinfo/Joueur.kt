package com.example.jeuinfo

import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.Rect
import android.graphics.RectF
import android.media.MediaPlayer
import java.util.ArrayList
import kotlin.math.abs

class Joueur(x1:Float,y1:Float,largeur:Float,hauteur:Float,private val width:Float,private val height:Float,
              val taillejoueur:Float, val deadSound:Son,image:Bitmap)
    : Element(x1,y1,largeur,hauteur,image) {
    var alive =true
    fun detectSortieEcran(){
        if(this.x1 + largeur > width){
            this.x1 = width - largeur
        }else if (this.x1 < 0){
            this.x1 = 0F
        }
        if(this.y1 + hauteur > height){
            this.deadSound.start()
            this.alive = false
        }
    }
    }