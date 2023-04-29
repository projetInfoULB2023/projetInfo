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
    fun addObs(x:Int,observer: Observer){
        observers.add(x,observer)
    }

    fun clear(){
        observers.clear()
    }
    fun remove(obs:Observer){
        observers.remove(obs)
    }
}