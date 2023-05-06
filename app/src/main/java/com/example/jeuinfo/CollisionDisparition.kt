package com.example.jeuinfo

/*
 description des comportements obligatoires pour toute classe implémentant CollisionDisparition (la collision provoque la disparition de l'obstacle)
 */
interface CollisionDisparition {
    fun collision(joueur: Joueur)

}