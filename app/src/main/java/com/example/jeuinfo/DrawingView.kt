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
    private val tailleJoueur = 50
    private val saut = tailleJoueur*2
    val blue = Color.BLUE
    val red = Color.RED
    private lateinit var posJoueur:Array<Int>
    private var setup = false
    private  var elements = ArrayList<Element>()
    private lateinit var test1 : Element
    private lateinit var joueur: Element
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
            drawObstacles()
            drawPlayer()
            //Fin code pour dessiner
            holder.unlockCanvasAndPost(canvas)
        }
    }
    private fun drawObstacles(){
        if(!setup){
            test1 = Element(0F,200F,width.toFloat(),300F,red)
            elements.add(test1)
        }
    }
    private fun drawPlayer(){
        if(!setup){
            posJoueur= arrayOf(width/2,height*7/8)
            joueur = Element((posJoueur[0]-tailleJoueur).toFloat(),(posJoueur[1]+tailleJoueur).toFloat(),(posJoueur[0]+tailleJoueur).toFloat(),(posJoueur[1]-tailleJoueur).toFloat(),blue)
            elements.add(joueur)
            setup = true
        }
        var paintPlayer=Paint()
        paintPlayer.color=Color.BLUE
        canvas.drawRect(joueur.r,paintPlayer)
        for(obs in elements){
            obs.avance(canvas)
        }
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
                println("$x1,$x2,$y1,$y2")
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