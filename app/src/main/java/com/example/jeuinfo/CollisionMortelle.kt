package com.example.jeuinfo

/*
 description des comportements obligatoires pour toute classe implémentant CollisionMortelle (la collision provoque la mort du joueur)
 */
interface CollisionMortelle {
    fun collision(joueur: Joueur,startingPos:Float)
}