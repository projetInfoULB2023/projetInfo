package com.example.jeuinfo

import android.graphics.Bitmap
import android.graphics.RectF
import kotlin.math.abs

class Cailloux(x1:Float,
               y1:Float,
               largeur:Float,
               hauteur:Float,
               image: Bitmap): Element(x1,y1,largeur,hauteur, image),CollisionSimple {

    override fun collision(joueur:Joueur,direction:Int,saut:Float) {
        joueur.r = RectF(joueur.x1+marge,joueur.y1+marge,joueur.x1+joueur.largeur-marge,joueur.y1+joueur.hauteur-marge)
        if(abs(this.y1-joueur.y1) < joueur.taillejoueur){
            if(this.r.intersect(joueur.r)){
                //0, haut. 1, bas. 2, gauche.3, droite
                when(direction){
                    0 -> {
                        joueur.y1 += saut
                        DrawingView.actualScore-=1
                    }
                    1 -> {
                        joueur.y1 -= saut
                        DrawingView.actualScore-=1
                    }
                    2 -> joueur.x1 += saut
                    3 -> joueur.x1 -= saut
                }
            }
        }
    }

}