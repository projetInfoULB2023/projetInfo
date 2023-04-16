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

//Eventuellement penser à des pouvoirs (blocs à récupérer pour avoir une vie en plus,sauter plus loin, détruire un obstacle, ...)
//Génération automatique et aléatoire d'obstacles
//Ajout différents personnages

class DrawingView @JvmOverloads constructor (context: Context, attributes: AttributeSet? = null, defStyleAttr: Int = 0): SurfaceView(context, attributes,defStyleAttr),
    SurfaceHolder.Callback,Runnable {
    private var deviceHeight = 0
    private var deviceWidth = 0
    private val backgroundPaint = Paint()
    private val random = Random()
    private var drawing = true
    lateinit var canvas:Canvas
    lateinit var thread:Thread
    private var direction = 0
    private var tailleJoueur = width/24F
    private var saut = 0F
    private var paint = Paint()
    private val blue = Color.BLUE
    private val red = Color.RED
    private lateinit var posJoueur:Array<Float>
    private var setup = false
    private var elements = ArrayList<Element>()
    private var decor = ArrayList<Element>()
    private lateinit var joueur: Joueur
    private lateinit var music1 : MediaPlayer
    private var reste = 0F
    private var routeImage = BitmapFactory.decodeResource(resources,R.drawable.route)
    private var herbeImage = BitmapFactory.decodeResource(resources,R.drawable.herbe)
    private var counter =0
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
        routeImage=Bitmap.createScaledBitmap(routeImage,width,tailleJoueur.toInt()*2,false)
        herbeImage=Bitmap.createScaledBitmap(herbeImage,width,tailleJoueur.toInt()*2,false)
    }

    private fun tickGame(){
        for(obs in decor) obs.avance(canvas)
        for(obs in elements) obs.avance(canvas)
        autoGen()
        joueur.collision(elements,direction,saut)
        joueur.detectSortieEcran()
        joueur.avance(canvas)
    }
    fun autoGen(){
        //Analyse la position en y du premier élément pour déterminer quand générer la suite
        if(decor[0].y1> 0 ){
            //On génère une nouvelle ligne
            if(counter%2 == 0){
                //On génère une ligne d'herbe
                val herbe = Obstacle(0F,-2*tailleJoueur,width.toFloat(),tailleJoueur*2,0F,width.toFloat() ,herbeImage)
                //Manipulation pour mettre la nouvelle herbe en première position
                decor.add(decor[0])
                decor[0] = herbe
            }else{
                //On génère une ligne de route
                val route = Obstacle(0F,-2*tailleJoueur,width.toFloat(),tailleJoueur*2,0F,width.toFloat() ,routeImage)
                decor.add(decor[0])
                decor[0] = route

            }
            counter+=1
        }
        println(height)
        if(decor[decor.size-1].y1 >= height){
            decor.removeAt(decor.size-1)
            println(decor.size)
        }
    }
    fun getMediaPlayer(music:MediaPlayer){
        music1 = music
    }

    private fun drawObstacles(){
        //Génération aléatoire d'obstacles pour 32/4 lignes
        for (i in 0..32 step 4){
            var r = random.nextInt(3)
            lateinit var obstacleTemp :Obstacle
            var larg = 0F
            var speed = 0F
            var path = 0
            when(r){
                0 -> {
                    //voiture
                    val herbe = Obstacle(0F,(i+2)*tailleJoueur,width.toFloat(),tailleJoueur*2,0F,width.toFloat() ,herbeImage)
                    decor.add(herbe)
                    val route = Obstacle(0F,i*tailleJoueur,width.toFloat(),tailleJoueur*2,0F,width.toFloat() ,routeImage)
                    decor.add(route)
                    speed = 5F
                    larg = 2F
                    //Détermination couleur
                    val y = random.nextInt(5)
                    when(y){
                        0->path=R.drawable.voiture_bleu
                        1->path=R.drawable.voiture_grise
                        2->path=R.drawable.voiture_jaune
                        3->path=R.drawable.voiture_orange
                        4->path=R.drawable.voiture_rouge
                    }
                }
                1 -> {
                    //camion
                    val herbe = Obstacle(0F,(i+2)*tailleJoueur,width.toFloat(),tailleJoueur*2,0F,width.toFloat() ,herbeImage)
                    decor.add(herbe)
                    val route = Obstacle(0F,i*tailleJoueur,width.toFloat(),tailleJoueur*2,0F,width.toFloat() ,routeImage)
                    decor.add(route)
                    speed = 4F
                    larg=4F
                    //Reste à déterminer la couleur
                    val y = random.nextInt(2)
                    when(y){
                        0->path=R.drawable.camion_bleu
                        1->path=R.drawable.camion_rouge
                    }
                }
                2 -> {
                    //bus scolaire, rien d'autre à déterminer
                    val herbe = Obstacle(0F,(i+2)*tailleJoueur,width.toFloat(),tailleJoueur*2,0F,width.toFloat() ,herbeImage)
                    decor.add(herbe)
                    val route = Obstacle(0F,i*tailleJoueur,width.toFloat(),tailleJoueur*2,0F,width.toFloat() ,routeImage)
                    decor.add(route)
                    speed = 3F
                    larg=5F
                    path=R.drawable.bus_scolaire
                }
            }

            obstacleTemp = Obstacle(random.nextFloat()*width,i*tailleJoueur, tailleJoueur*larg,tailleJoueur*2,speed,
                width.toFloat(),BitmapFactory.decodeResource(resources,path))
            elements.add(obstacleTemp)

            //Génération cailloux
            val x = random.nextInt(3)
            var list = mutableListOf(0,1,2,3,4,5,6,7,8,9,10,11)
            for (j in 0..x) {
                val index = random.nextInt(list.size)
                val location = list[index]
                list.remove(location)
                lateinit var obstacleTemp: ObstacleFixe
                var path = 0

                //Rocher
                val speed = 0F
                val larg = 2F

                //Détermination type
                val y = random.nextInt(4)
                when (y) {
                    0 -> path = R.drawable.caillou_arbre
                    1 -> path = R.drawable.caillou_buisson
                    2 -> path = R.drawable.caillou_fougere
                    3 -> path = R.drawable.caillou_palmier
                }
                obstacleTemp = ObstacleFixe(
                    (location * tailleJoueur*2),
                    (i+2) * tailleJoueur,
                    tailleJoueur * larg,
                    tailleJoueur * 2,
                    width.toFloat(),
                    BitmapFactory.decodeResource(resources, path)
                )
                elements.add(obstacleTemp)
            }
        }
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
                        direction=3
                    }else{
                        //Gauche
                        joueur.x1 -= saut
                        direction=2
                    }
                }else{
                    //Mouvement vertical, reste à déterminer haut ou bas
                    if(y2-y1 > 0){
                        //Bas
                        joueur.y1 += saut
                        direction=1
                    }else {
                        //Haut
                        joueur.y1 -= saut
                        direction=0
                    }
                }
            }
        }
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