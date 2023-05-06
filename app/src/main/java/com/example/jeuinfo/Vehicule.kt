package com.example.jeuinfo

// importations
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Matrix
import android.graphics.RectF
import kotlin.math.abs


// classe Vehicule héritante de la classe Element et implémentant les interfaces Deplacable et CollisionMortelle
class Vehicule(x1:Float, y1:Float, largeur:Float, hauteur:Float, val vitesse:Float, image: Bitmap): Element(x1,y1,largeur,hauteur, image), Deplacable, CollisionMortelle {
    var imageSetup = false

    // réécriture de la fonction deplacement()
    override fun deplacement() {
        //permet la réapparition des véhicules de l'autre coté de l'écran
        if(this.x1 >= DrawingView.getCwidth() && vitesse > 0){
            this.x1 =- largeur
        }
        else if(this.x1+largeur < 0 && vitesse < 0){
            this.x1 = DrawingView.getCwidth().toFloat()
        }
        this.r.offset(vitesse,0F)
        this.x1 += vitesse
    }



    // fonction de mise en place du visuel des véhicules
    fun setupImage(x:Float){
        //inversion de l'image si l'obstacle recule (cohérence par rapport à l'avant du véhicule)
        if(x<0) {
            val matrix = Matrix().apply { postScale(-1f, 1f, image.width.toFloat() / 2f, image.height.toFloat() / 2f) }
            this.image = Bitmap.createBitmap(this.image, 0, 0, image.width, image.height, matrix, true)
        }
    }



    // réécriture de la fonction de mise à jour du visuel
    override fun update(canvas: Canvas) {
        //mouvement horizontal en plus
        if(!imageSetup) {
            setupImage(vitesse)
            imageSetup=true
        }
        this.deplacement()
        super.update(canvas)
    }



    // réécriture de la fonction de gestion des collisions (collision avec véhicule entraîne la mort du joueur)
    override fun collision(joueur:Joueur, startingPos:Float) {
        joueur.r = RectF(joueur.x1+marge,joueur.y1+marge,joueur.x1+joueur.largeur-marge,joueur.y1+joueur.hauteur-marge)
        if(abs(this.y1-joueur.y1) < joueur.taillejoueur){
            if(this.r.intersect(joueur.r)){
                joueur.mort()
            }
        }
    }
}
