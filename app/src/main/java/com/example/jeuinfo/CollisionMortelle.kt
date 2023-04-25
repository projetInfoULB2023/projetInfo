package com.example.jeuinfo

interface CollisionMortelle {
    fun collision(joueur: Joueur,obstacles: MutableList<Element>,startingPos:Float)
}