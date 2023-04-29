package com.example.jeuinfo

import android.graphics.Canvas

interface Observer {
    fun avance(canvas: Canvas)
}