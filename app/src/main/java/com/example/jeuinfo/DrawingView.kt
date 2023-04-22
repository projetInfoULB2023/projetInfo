package com.example.jeuinfo

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.SurfaceHolder
import android.view.SurfaceView
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.abs


//Etapes importantes

//Eventuellement penser à des pouvoirs (blocs à récupérer pour avoir une vie en plus,sauter plus loin, détruire un obstacle, ...)
//Ajout différents personnages
//Mort quand écrasé par véhicules (collisions mortelles)
//Rien quand tente d'avancer sur un cailloux

class DrawingView @JvmOverloads constructor (private var context: Context, attributes: AttributeSet? = null, defStyleAttr: Int = 0): SurfaceView(context, attributes,defStyleAttr),
    SurfaceHolder.Callback,Runnable {
    private var deviceHeight = 0
    private var deviceWidth = 0
    private val backgroundPaint = Paint()
    private val random = Random()
    private val maxVoitures = 3
    private val maxCailloux = 3
    private var drawing = true
    lateinit var canvas:Canvas
    lateinit var thread:Thread
    private var direction = 0
    private var tailleJoueur = 0F
    private var saut = 0F
    private lateinit var posJoueur:Array<Float>
    private var setup = false
    private var obstacles = mutableListOf<Element>()
    private var decor = ArrayList<Element>()
    private lateinit var joueur: Joueur
    private var reste = 0F

    private var routeImage = BitmapFactory.decodeResource(resources,R.drawable.route)
    private var herbeImage = BitmapFactory.decodeResource(resources,R.drawable.herbe)

    private var options = BitmapFactory.Options()
    private var caillouArbre = BitmapFactory.decodeResource(resources,R.drawable.caillou_arbre)
    private var caillouBuisson = BitmapFactory.decodeResource(resources,R.drawable.caillou_buisson)
    private var caillouFougere = BitmapFactory.decodeResource(resources,R.drawable.caillou_fougere)
    private var caillouPalmier = BitmapFactory.decodeResource(resources,R.drawable.caillou_palmier)
    private var voitureBleu = BitmapFactory.decodeResource(resources,R.drawable.voiture_bleu)
    private var voitureGrise = BitmapFactory.decodeResource(resources,R.drawable.voiture_grise)
    private var voitureJaune = BitmapFactory.decodeResource(resources,R.drawable.voiture_jaune)
    private var voitureOrange = BitmapFactory.decodeResource(resources,R.drawable.voiture_orange)
    private var voitureRouge = BitmapFactory.decodeResource(resources,R.drawable.voiture_rouge)
    private var busScolaire=BitmapFactory.decodeResource(resources,R.drawable.bus_scolaire,options)
    private var camionBleu = BitmapFactory.decodeResource(resources,R.drawable.camion_bleu)
    private var camionRouge = BitmapFactory.decodeResource(resources,R.drawable.camion_rouge)
    private var monstre = BitmapFactory.decodeResource(resources,R.drawable.monstre)
    private var startingPos =0F
    private var time =0
    //Entrée touche
    var x1=0F
    var x2=0F
    var y1=0F
    var y2=0F
    private var textPaint = Paint()
    private var compteurMort = 0
    private var deadScreen=false
    private var sonMonstre = Son(context,R.raw.grognement)
    private var sonMusique = Son(context,R.raw.musiquefond)
    private fun draw(){
        if(holder.surface.isValid){
            canvas =holder.lockCanvas()
            //Permet de ne pas acculumer les éléments dessinés
            canvas?.drawRect(0F,0F,width.toFloat(),height.toFloat(),backgroundPaint)
            //Code pour dessiner ici
            if(!setup){
                setupVariables()
                drawObstacles()
                drawPlayer()
                setup = true
            }
            if(joueur.alive) tickGame()
            else mort()
            //Fin code pour dessiner
            holder.unlockCanvasAndPost(canvas)
            }
    }
    private fun mort(){
        //On reinitialise la partie
        if (!deadScreen){
            sonMusique.stop()
            sonMonstre.start()
            joueur.deadSound.start()
            obstacles.clear()
            decor.clear()
            deadScreen=true
            backgroundPaint.color=Color.RED
            textPaint.color=Color.BLACK
        }
        compteurMort+=1
        textPaint.textSize = width/6F
        canvas?.drawText("TY E MOO' !",width/2F-width/2.3F,height/2F-20,textPaint)
        textPaint.textSize = width/15F
        canvas?.drawText("Clique sur l'écran pour rejouer",width/15F,height/2F+height/10,textPaint)
        canvas?.drawBitmap(monstre,null,Rect(0,0,width,height),backgroundPaint)
        if(compteurMort>80)
            backgroundPaint.color=Color.GREEN

    }
    private fun setupVariables(){
        sonMusique.start()
        time = 200
        compteurMort=0
        backgroundPaint.color= Color.WHITE
        deadScreen=false
        tailleJoueur=width/24F
        saut = tailleJoueur*2F
        reste=tailleJoueur*36%tailleJoueur
        startingPos = tailleJoueur*((height/tailleJoueur).toInt()-8)
        //decor
        routeImage=Bitmap.createScaledBitmap(routeImage,width,tailleJoueur.toInt()*2,true)
        herbeImage=Bitmap.createScaledBitmap(herbeImage,width,tailleJoueur.toInt()*2,true)
        monstre=Bitmap.createScaledBitmap(monstre,width,height,true)
    }

    private fun tickGame(){
        autoGen()
        for(obs in decor) obs.avance(canvas)
        for(obs in obstacles) {
            obs.avance(canvas)
            if(obs is Deplacable){
                if(!obs.imageSetup)obs.setupImage()
                obs.deplacement()
                if(obs is CollisionMortelle) obs.collision(joueur,obstacles,startingPos)
            }

        }
        joueur.detectSortieEcran()
        joueur.avance(canvas)
    }
    private fun autoGen(){
        //Analyse la position en y du premier élément pour déterminer quand générer la suite
        if(time*Element.vitesseCam+2>=tailleJoueur*2){
            //On trouve la position de l'élement le plus haut sur l'écran pour positionner le suivant en fonction de cela
            var lowestEl = decor[0]
            for(el in decor){
                for(i in 0..decor.size){
                    if(el.y1<lowestEl.y1){
                        lowestEl=el
                    }
                }
            }
            val posLow = lowestEl.y1
            time=0
            //On génère une nouvelle ligne
            if(random.nextFloat()>0.5){
                //On génère une ligne d'herbe
                val herbe = Element(0F,posLow-2*tailleJoueur,width.toFloat(),tailleJoueur*2,herbeImage)
                //Manipulation pour mettre la nouvelle herbe en première position
                decor.add(herbe)
                //Génération cailloux
                val x = random.nextInt(maxCailloux)
                var list = mutableListOf(0,1,2,3,4,5,6,7,8,9,10,11)
                for (j in 0..x) {
                    val index = random.nextInt(list.size)
                    val location = list[index]
                    list.remove(location)
                    lateinit var obstacleTemp: Cailloux
                    var image = caillouArbre

                    //Rocher
                    val larg = 2F

                    //Détermination type
                    val y = random.nextInt(4)
                    when (y) {
                        0 -> image = caillouArbre
                        1 -> image = caillouBuisson
                        2 -> image = caillouFougere
                        3 -> image = caillouPalmier
                    }
                    obstacleTemp = Cailloux(
                        (location * tailleJoueur*2),
                        posLow-2 * tailleJoueur,
                        tailleJoueur * larg,
                        tailleJoueur * 2,
                        image
                    )
                    obstacles.add(obstacleTemp)
                }
            }else{
            //On génère une ligne de route
            val route = Element(0F,posLow-2*tailleJoueur,width.toFloat(),tailleJoueur*2,routeImage)
            decor.add(route)
            //Génération de véhicules
            var r = random.nextInt(3)
            lateinit var obstacleTemp :Vehicule
            var larg = 0F
            var speed = 0F
            var image = caillouArbre
            val z = random.nextInt(maxVoitures)
            var vehiList = mutableListOf(1,4,7,10)
            //Meme direction pour tous les véhicules de la meme ligne
            var dx = if(random.nextFloat()> 0.5) 1 else -1
            for(j in 0..z) {
                val index = random.nextInt(vehiList.size)
                val location = vehiList[index]
                when (r) {
                    0 -> {
                        //voiture
                        speed = 7F
                        larg = 2F
                        //Détermination couleur
                        when (random.nextInt(5)) {
                            0 -> image = voitureBleu
                            1 -> image = voitureGrise
                            2 -> image = voitureJaune
                            3 -> image = voitureOrange
                            4 -> image = voitureRouge
                        }
                    }
                    1 -> {
                        //camion
                        speed = 5F
                        larg = 4F
                        //Reste à déterminer la couleur
                        val y = random.nextInt(2)
                        when (y) {
                            0 -> image = camionBleu
                            1 -> image = camionRouge
                        }
                    }
                    2 -> {
                        //bus scolaire, rien d'autre à déterminer
                        speed = 4F
                        larg = 5F
                        image = busScolaire
                        }
                    }
                    obstacleTemp = Vehicule(location*tailleJoueur*2,posLow-2*tailleJoueur, tailleJoueur*larg,tailleJoueur*2,speed*dx,
                        width.toFloat(),image)

                    obstacles.add(obstacleTemp)
                    vehiList.remove(location)

                }
            }
            //Supprime les éléments qui ont quitté le jeu
            lateinit var delItem : Element
            //D'abord les éléments du décor
            var removable = false
            for(el in decor){
                if (el.y1 > height+tailleJoueur*2){
                    delItem = el
                    removable = true
                }
            }
            if (removable) decor.remove(delItem)
            var delItems=ArrayList<Element>()
            //Ensuite les véhicules
            for(el in obstacles) if(el.y1 > height+tailleJoueur*2) delItems.add(el)
            for(el in delItems) obstacles.remove(el)

        }
        time+=1
    }
    private fun drawObstacles(){
        //Génération aléatoire d'obstacles
        for (i in 0..(height/tailleJoueur).toInt() step 4){
            var r = random.nextInt(3)
            lateinit var obstacleTemp :Vehicule
            var larg = 0F
            var speed = 0F
            var path = 0
            val z = random.nextInt(maxVoitures)
            var vehiList = mutableListOf(2,5,8,11)
            //Meme direction pour tous les véhicules de la meme ligne
            var dx = if(random.nextFloat()> 0.5) 1 else -1
            for(j in 0..z){
                val index = random.nextInt(vehiList.size)
                val location = vehiList[index]
                vehiList.remove(location)
                when(r){
                    0 -> {
                        //voiture
                        speed = 7F
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
                        speed = 5F
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
                        speed = 4F
                        larg=5F
                        path=R.drawable.bus_scolaire
                    }
                }
                obstacleTemp = Vehicule(location*tailleJoueur*2,i*tailleJoueur, tailleJoueur*larg,tailleJoueur*2,speed*dx,
                    width.toFloat(),BitmapFactory.decodeResource(resources,path))
                obstacles.add(obstacleTemp)
            }

            //Génération lignes de terrain
            val herbe = Element(0F,(i+2)*tailleJoueur,width.toFloat(),tailleJoueur*2 ,herbeImage)
            decor.add(herbe)
            val route = Element(0F,i*tailleJoueur,width.toFloat(),tailleJoueur*2,routeImage)
            decor.add(route)

            //Génération cailloux
            val x = random.nextInt(maxCailloux)
            var caiList = mutableListOf(0,1,2,3,4,5,6,7,8,9,10,11)
            for (j in 0..x) {
                val index = random.nextInt(caiList.size)
                val location = caiList[index]
                caiList.remove(location)
                lateinit var obstacleTemp: Cailloux
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
                obstacleTemp = Cailloux(
                    (location * tailleJoueur*2),
                    (i+2) * tailleJoueur,
                    tailleJoueur * larg,
                    tailleJoueur * 2,
                    BitmapFactory.decodeResource(resources, path)
                )
                obstacles.add(obstacleTemp)
            }
        }
    }

    private fun drawPlayer(){
        val deadSound = Son(context,R.raw.mort)
        //alligne le joueur et les obstacles
        posJoueur= arrayOf(width/12*7F-tailleJoueur,startingPos)
        joueur = Joueur((posJoueur[0]-tailleJoueur),(posJoueur[1]+tailleJoueur),tailleJoueur*2,
            tailleJoueur*2,width.toFloat(),height.toFloat(),tailleJoueur,deadSound,BitmapFactory.decodeResource(resources,R.drawable.bersini))
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
            MotionEvent.ACTION_DOWN ->{
                x1 = e.rawX; y1=e.rawY
                println(compteurMort)
                if (compteurMort>80){
                    joueur.alive = true
                    setup = false
                }
            }
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
                for(obs in obstacles){
                    if(obs is CollisionSimple) obs.collision(joueur,direction,saut)
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