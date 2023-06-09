package com.example.jeuinfo

// importations
import android.graphics.Canvas


// classe Observable (gestion des Observers)
class Observable {
    private val observers = mutableListOf<Observer>()


    fun addObs(observer:Observer){
        observers.add(observer)
    }



    fun updateObs(canvas: Canvas){
        observers.forEach{it.update(canvas)}
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