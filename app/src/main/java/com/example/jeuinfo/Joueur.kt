package com.example.jeuinfo

import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.Rect
import android.graphics.RectF
import android.media.MediaPlayer
import java.util.ArrayList
import kotlin.math.abs

class Joueur(x1:Float,y1:Float,largeur:Float,hauteur:Float,private val width:Float,private val height:Float,
             private val taillejoueur:Float,private val deadSound:Son,image:Bitmap)
    : Element(x1,y1,largeur,hauteur,image) {
    //Association avec le deadSound:Son

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
    fun collision(elements: ArrayList<Element>, direction:Int, saut:Float){
        this.r= RectF(x1+marge,y1+marge,x1+largeur-marge,y1+hauteur-marge)
        for(obstacle in elements){
            //Vérification uniquement si le joueur est sur la ligne de l'obstacle, sinon pas nécessaire => fait gagner en performance
            if(abs(obstacle.y1-this.y1) < taillejoueur){
                if(obstacle.r.intersect(this.r)){
                    if(obstacle is ObstacleFixe){
                        //0, haut. 1, bas. 2, gauche.3, droite
                        when(direction){
                            0 -> this.y1 += saut
                            1 -> this.y1 -= saut
                            2 -> this.x1 += saut
                            3 -> this.x1 -= saut
                        }
                    }else if (obstacle is Obstacle){

                    }
                }
            }
        }
    }
    }