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
        var bonuses= mutableListOf<Bonus>()
        var lives=1
        fun detectSortieEcran() {
            if (this.x1 + largeur > width) {
                this.x1 = width - largeur
            } else if (this.x1 < 0) {
                this.x1 = 0F
            }
            if (this.y1 + hauteur > height) {
                this.deadSound.start()
                this.alive = false
            }else if(this.y1 < 0){
                this.y1 +=DrawingView.saut*2
            }
        }
        fun mort(){
            lateinit var delItem:Bonus
            for(bonus in bonuses){
                if(bonus is BonusVie){
                    delItem=bonus
                    lives-=1
                }
            }
            try {
                bonuses.remove(delItem)
                this.y1+=hauteur
            }catch(e:UninitializedPropertyAccessException){
                this.alive = false
            }

        }
        fun checkBonus(){
            var counter = 1
            for(bonus in bonuses){
                if(bonus is BonusSaut) counter+=1
            }
            DrawingView.saut=taillejoueur*2*counter
        }
    }