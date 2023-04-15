package com.example.jeuinfo

import android.content.Context
import android.graphics.*
import android.media.MediaPlayer
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.SurfaceHolder
import android.view.SurfaceView
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.abs

//Etapes importantes

//Trouver une bonne taille d'obstacles et la faire correspondre avec la position du joueur
//Eventuellement penser à des pouvoirs (blocs à récupérer pour avoir une vie en plus,sauter plus loin, détruire un obstacle, ...)
//Mort quand collision avec un obstacle
//Génération automatique et aléatoire d'obstacles
//Set up aléatoire d'obstacles au début, puis génération petit à petit.
//Redimensionner les images

class DrawingView @JvmOverloads constructor (context: Context, attributes: AttributeSet? = null, defStyleAttr: Int = 0): SurfaceView(context, attributes,defStyleAttr),
    SurfaceHolder.Callback,Runnable {
    private var deviceHeight = 0
    private var deviceWidth = 0
    private val backgroundPaint = Paint()
    private val random = Random()
    private var drawing = true
    lateinit var canvas:Canvas
    lateinit var thread:Thread
    private var tailleJoueur = width/24F
    private var saut = 0F
    private val blue = Color.BLUE
    private val red = Color.RED
    private lateinit var posJoueur:Array<Float>
    private var setup = false
    private  var elements = ArrayList<Element>()
    private lateinit var barre1  : Obstacle
    private lateinit var joueur: Joueur
    private lateinit var music1 : MediaPlayer
    private var reste = 0F
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
                setupVariables()
                drawObstacles()
                drawPlayer()
                setup = true
            }
            tickGame()
            //Fin code pour dessiner
            holder.unlockCanvasAndPost(canvas)
        }
    }
    private fun setupVariables(){
        tailleJoueur = width/24F
        saut = tailleJoueur*2F
        reste = height*7/8 % tailleJoueur
    }
    private fun tickGame(){
        for(obs in elements){
            obs.avance(canvas)
        }
        joueur.avance(canvas)
        joueur.detectSortieEcran()
        collisions()
    }
    private fun collisions(){
        for(obstacle in elements){
        }
    }

    fun getMediaPlayer(music:MediaPlayer){
        music1 = music
    }

    private fun drawObstacles(){
        barre1 = Obstacle(0F,tailleJoueur*8,width/5.toFloat()+200,2*tailleJoueur,2F, width.toFloat(),
            BitmapFactory.decodeResource(resources,R.drawable.camion_bleu))
        elements.add(barre1)
    }

    private fun drawPlayer(){
        //alligne le joueur et les obstacles
        posJoueur= arrayOf(width/12*7F-tailleJoueur,height*7/8-reste)
        joueur = Joueur((posJoueur[0]-tailleJoueur).toFloat(),(posJoueur[1]+tailleJoueur).toFloat(),tailleJoueur*2,
            tailleJoueur*2,width.toFloat(),height.toFloat(),tailleJoueur,music1,BitmapFactory.decodeResource(resources,R.drawable.bersini))
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
                    }else{
                        //Gauche
                        joueur.x1 -= saut
                    }
                }else{
                    //Mouvement vertical, reste à déterminer haut ou bas
                    if(y2-y1 > 0){
                        //Bas
                        joueur.y1 += saut
                        //Detection collision basse


                    }else {
                        //Haut
                        joueur.y1 -= saut
                        //Détection collision haute

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