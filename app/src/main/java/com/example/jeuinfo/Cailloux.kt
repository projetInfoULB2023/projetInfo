package com.example.jeuinfo


// importations
import android.graphics.Bitmap
import android.graphics.RectF
import kotlin.math.abs



// classe héritante de la classe Element et implémentant l'interface CollisionSimple
class Cailloux(x1:Float, y1:Float, largeur:Float, hauteur:Float, image: Bitmap): Element(x1, y1, largeur, hauteur, image), CollisionSimple {

    // réécriture de la fonction collision()
    override fun collision(joueur:Joueur,direction:Int,saut:Float) {
        joueur.r = RectF(joueur.x1+marge,joueur.y1+marge,joueur.x1+joueur.largeur-marge,joueur.y1+joueur.hauteur-marge)
        if(abs(this.y1-joueur.y1) < joueur.taillejoueur){
            if(this.r.intersect(joueur.r)){
                when(direction){
                    //collision vers le haut
                    0 -> {
                        joueur.y1 += saut
                        DrawingView.actualScore -= 1
                    }
                    //collision vers le bas
                    1 -> {
                        joueur.y1 -= saut
                        DrawingView.actualScore -= 1
                    }
                    2 -> joueur.x1 += saut //collision vers la gauche
                    3 -> joueur.x1 -= saut //collision vers la droite
                }
            }
        }
    }
}