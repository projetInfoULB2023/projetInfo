package com.example.jeuinfo

//importations
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.Rect
import android.graphics.RectF
import android.media.MediaPlayer
import java.util.ArrayList
import kotlin.math.abs



// classe Joueur héritante de la classe Element
class Joueur (x1:Float, y1:Float, largeur:Float, hauteur:Float, private val width:Float, private val height:Float, val taillejoueur:Float, val deadSound:Son, image:Bitmap) : Element(x1,y1,largeur,hauteur,image) {
        var alive = true //état de vie du joueur
        var bonuses = mutableListOf<Bonus>() //liste des bonus actifs sur le joueur
        var lives = 1 //nombre de vies du joueur


        // fonction de détection de la sortie d'écran
        fun detectSortieEcran() {
            //interdiction des sorties d'écran horizontales
            if (this.x1 + largeur > width) {
                this.x1 = width - largeur
            } else if (this.x1 < 0) {
                this.x1 = 0F
            }
            //interdiction des sorties d'écran verticales
            if (this.y1 + hauteur > height) {
                //mort du joueur si sortie par le haut
                this.deadSound.start()
                this.alive = false
            } else if(this.y1 < 0) {
                //repoussement du joueur vers le haut si sortie par le bas
                this.y1 += DrawingView.getSaut()*2
            }
        }


        // fonction de mort du joueur
        fun mort() {
            lateinit var delItem:Bonus
            deadSound.start()
            //suppression du bonus vie s'il existe
            for(bonus in bonuses){
                if(bonus is BonusVie){
                    delItem = bonus
                    lives -= 1
                    break
                }
            }
            //mort du joueur si le bonus vie n'est pas présent
            try {
                bonuses.remove(delItem)
                this.y1 += hauteur
                DrawingView.actualScore -= 1
            }catch(e:UninitializedPropertyAccessException){
                this.alive = false
            }
        }


        // fonction de vérification du bonus de saut et modification du pas de saut s'il est présent
        fun checkBonus() {
            var counter = 1
            for(bonus in bonuses){
                if(bonus is BonusSaut) counter += 1
            }
            DrawingView.setSaut(taillejoueur*2*counter)
        }
    }