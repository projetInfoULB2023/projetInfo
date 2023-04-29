package com.example.jeuinfo

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Matrix
import android.graphics.RectF
import kotlin.math.abs

class Vehicule(x1:Float,
                      y1:Float,
                      largeur:Float,
                      hauteur:Float,
                      val vitesse:Float,
                      image: Bitmap): Element(x1,y1,largeur,hauteur, image),Deplacable,CollisionMortelle {
    var imageSetup = false
    override fun deplacement() {
        //Permet la réapparition de l'autre coté de l'écran
        if(this.x1>=DrawingView.Cwidth && vitesse > 0){
            this.x1=-largeur
        }
        else if(this.x1+largeur< 0 && vitesse <0){
            this.x1 = DrawingView.Cwidth.toFloat()
        }
        this.r.offset(vitesse,0F)
        this.x1 += vitesse
    }

    override fun avance(canvas: Canvas) {
        //Mouvement horizontal en plus
        if(!imageSetup) {
            setupImage(vitesse)
            imageSetup=true
        }
        this.deplacement()
        super.avance(canvas)
    }

    override fun collision(joueur:Joueur,startingPos:Float) {
        joueur.r = RectF(joueur.x1+marge,joueur.y1+marge,joueur.x1+joueur.largeur-marge,joueur.y1+joueur.hauteur-marge)
        if(abs(this.y1-joueur.y1) < joueur.taillejoueur){
            if(this.r.intersect(joueur.r)){
                //Code pour mort du joueur, le jeu se réinitialise
                joueur.mort()
            }
            }
        }
                      }
