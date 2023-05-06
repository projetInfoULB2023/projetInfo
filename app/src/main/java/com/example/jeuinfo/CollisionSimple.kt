package com.example.jeuinfo

/*
 description des comportements obligatoires pour toute classe implémentant CollisionSimple (la collision est interdite)
 */
interface CollisionSimple {
    fun collision(joueur: Joueur,direction:Int,saut:Float){}
}