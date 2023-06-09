package com.example.jeuinfo


// importations
import android.graphics.Bitmap
import android.graphics.RectF
import kotlin.math.abs


// classe héritante de la classe Bonus et implémentant l'interface CollisionDisparition
class BonusSaut(x1:Float, y1:Float, largeur:Float, hauteur:Float, vitesseX:Float, vitesseY:Float, image: Bitmap,son:Son) : Bonus(x1, y1, largeur, hauteur, vitesseX, vitesseY, image,son), CollisionDisparition {

    //réécriture de la fonction collision() pour le cas d'une collision avec un bonus de saut
    override fun collision(joueur:Joueur) {
        joueur.r = RectF(joueur.x1+marge,joueur.y1+marge,joueur.x1+joueur.largeur-marge,joueur.y1+joueur.hauteur-marge)
        if(abs(this.y1-joueur.y1) < joueur.taillejoueur*2){
            if(this.r.intersect(joueur.r)){
                son.start()
                joueur.bonuses.add(this)
                DrawingView.toBeRemoved.add(this)
                joueur.checkBonus()
            }
        }
    }
}