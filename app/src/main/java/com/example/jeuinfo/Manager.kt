package com.example.jeuinfo

import android.graphics.Canvas

class Manager {
    private val observers = mutableListOf<Observer>()

    fun addObs(observer:Observer){
        observers.add(observer)
    }

    fun updateObs(canvas: Canvas){
        observers.forEach{it.avance(canvas)}
    }

    fun clear(){
        observers.clear()
    }
    fun remove(obs:Observer){
        observers.remove(obs)
    }
}