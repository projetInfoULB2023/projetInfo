package com.example.jeuinfo

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.SurfaceHolder
import android.view.SurfaceView
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.abs

//Cam avance automatiquement, possible de reculer, devant à gauche ou à droite, obstacles bougent tout seuls horizontalement
//mort quand colision avec un obstacle,

class DrawingView @JvmOverloads constructor (context: Context, attributes: AttributeSet? = null, defStyleAttr: Int = 0): SurfaceView(context, attributes,defStyleAttr),
    SurfaceHolder.Callback,Runnable {
    private var deviceHeight = 0
    private var deviceWidth = 0
    private val backgroundPaint = Paint()
    private val random = Random()
    private var drawing = true
    lateinit var canvas:Canvas
    lateinit var thread:Thread
    private var tailleJoueur = 0F
    private var saut = 0F
    private val blue = Color.BLUE
    private val red = Color.RED
    private lateinit var posJoueur:Array<Float>
    private var setup = false
    private  var elements = ArrayList<Element>()
    private lateinit var barre1  : Element
    private lateinit var joueur: Joueur
    //Entrée touche
    var x1=0F
    var x2=0F
    var y1=0F
    var y2=0F
    private fun draw(){
        if(holder.surface.isValid){
            canvas =holder.lockCanvas()
            //Permet de ne pas acculumer les éléments dessinés
            backgroundPaint.color= Color.WHITE
            canvas?.drawRect(0F,0F,width.toFloat(),height.toFloat(),backgroundPaint)
            //Code pour dessiner ici
            if(!setup){
                drawObstacles()
                drawPlayer()
                setup = true
            }
            tickGame()
            //Fin code pour dessiner
            holder.unlockCanvasAndPost(canvas)
        }
    }
    private fun tickGame(){
        for(obs in elements){
            obs.avance(canvas)
        }
        joueur.detectSortieEcran()
    }
    private fun drawObstacles(){
        barre1 = Element(0F,200F,width.toFloat(),300F,red)
        elements.add(barre1)
    }
    private fun drawPlayer(){
        //alligne le joueur et les obstacles
        tailleJoueur = width/24F
        saut = tailleJoueur*2F
        val reste = height*7/8 % tailleJoueur
        posJoueur= arrayOf(width/12*5F-tailleJoueur,height*7/8-reste)
        joueur = Joueur((posJoueur[0]-tailleJoueur).toFloat(),(posJoueur[1]+tailleJoueur).toFloat(),(posJoueur[0]+tailleJoueur).toFloat(),
            (posJoueur[1]-tailleJoueur).toFloat(),width.toFloat(),tailleJoueur)
        elements.add(joueur)
    }
    fun pause(){
        drawing = false
        thread.join()
    }
    fun resume(){
        drawing = true
        thread=Thread(this)
        thread.start()
    }

    fun getDimensions(deviceW:Int,deviceH:Int){
        deviceWidth=deviceW
        deviceHeight=deviceH
    }

    override fun onTouchEvent(e: MotionEvent): Boolean {
        //S'active quand l'écran est touché
        //println("$deviceWidth, $deviceHeight, $width, $height")
        when(e.action){
            MotionEvent.ACTION_DOWN ->{ x1 = e.rawX; y1=e.rawY}
            MotionEvent.ACTION_UP -> {
                x2=e.rawX;y2=e.rawY
                //Direction de swipe
                if(abs(x2-x1) > abs(y2-y1)){
                    //Mouvement horizontal, reste à déterminer gauche ou droite
                    if(x2-x1 > 0){
                        //Droite
                        joueur.x1 += saut
                        joueur.x2 +=saut
                    }else{
                        //Gauche
                        joueur.x1 -= saut
                        joueur.x2 -= saut
                    }
                }else{
                    //Mouvement vertical, reste à déterminer haut ou bas
                    if(y2-y1 > 0){
                        //Bas
                        joueur.y1 += saut
                        joueur.y2 +=saut
                    }else {
                        //Haut
                        joueur.y1 -= saut
                        joueur.y2 -=saut
                    }
                }
            }
        }

        if (e.action == MotionEvent.ACTION_DOWN) {
            // x et y donnent la position du click, il faudrait encore tester le y par rapport à la position de notre drawingview sur l'écran
            val x = e.rawX
            //Le -724 est à retester avec d'autres tailles d'écran
            val y = e.rawY - 724
        }
        //invalidate permet de dessiner ce qui a été changé
        invalidate()
        return true
    }

    override fun run() {
        while(drawing){
            draw()
        }
    }
    override fun surfaceCreated(p0: SurfaceHolder) {
        TODO("Not yet implemented")
    }
    override fun surfaceChanged(p0: SurfaceHolder, p1: Int, p2: Int, p3: Int) {
        TODO("Not yet implemented")
    }
    override fun surfaceDestroyed(p0: SurfaceHolder) {
        TODO("Not yet implemented")
    }
}