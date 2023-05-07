package com.example.jeuinfo


//importations
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.SurfaceHolder
import android.view.SurfaceView
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.abs

class DrawingView @JvmOverloads constructor (private var context: Context, attributes: AttributeSet? = null, defStyleAttr: Int = 0): SurfaceView(context, attributes,defStyleAttr),
    SurfaceHolder.Callback,Runnable {
    // VALEURS DE CLASSE
    private val backgroundPaint = Paint() //peinture de fond
    private var textPaint = Paint() //peinture des textes
    private var livesPaint = Paint()
    private val random = Random()
    private val maxVoitures = 3 //nombre maximal d'obstacles mouvants par route
    private val maxCailloux = 3 //nombre maximal d'obstacles inanimés par pelouse

    // VARIABLES DE CLASSE
    private var drawing = true
    private var deadScreen = true
    private var ready = true
    private var tailleJoueur = 0F //taille de l'icône joueur
    private var observable = Observable()
    private var setup = false
    private var decor = ArrayList<Element>() //obtention du décor depuis la classe Element
    private var obstacles = mutableListOf<Element>() //obtention des obstacles depuis la classe Element
    private var reste = 0F
    private var startingPos = 0F //position initiale du joueur
    private var time = 0
    private var cancelUp = false
    lateinit var canvas:Canvas
    lateinit var thread:Thread
    private lateinit var joueur: Joueur

    // IMAGES ET SONS DE L'APPLICATION
    private var routeImage = BitmapFactory.decodeResource(resources,R.drawable.route)
    private var herbeImage = BitmapFactory.decodeResource(resources,R.drawable.herbe)
    private var caillouArbre = BitmapFactory.decodeResource(resources,R.drawable.caillou_arbre)
    private var caillouBuisson = BitmapFactory.decodeResource(resources,R.drawable.caillou_buisson)
    private var caillouFougere = BitmapFactory.decodeResource(resources,R.drawable.caillou_fougere)
    private var caillouPalmier = BitmapFactory.decodeResource(resources,R.drawable.caillou_palmier)
    private var voitureBleu = BitmapFactory.decodeResource(resources,R.drawable.voiture_bleu)
    private var voitureGrise = BitmapFactory.decodeResource(resources,R.drawable.voiture_grise)
    private var voitureJaune = BitmapFactory.decodeResource(resources,R.drawable.voiture_jaune)
    private var voitureOrange = BitmapFactory.decodeResource(resources,R.drawable.voiture_orange)
    private var voitureRouge = BitmapFactory.decodeResource(resources,R.drawable.voiture_rouge)
    private var options = BitmapFactory.Options()
    private var busScolaire = BitmapFactory.decodeResource(resources,R.drawable.bus_scolaire, options)
    private var camionBleu = BitmapFactory.decodeResource(resources,R.drawable.camion_bleu)
    private var camionRouge = BitmapFactory.decodeResource(resources,R.drawable.camion_rouge)
    private var coeur = BitmapFactory.decodeResource(resources,R.drawable.coeur)
    private var chaussures = BitmapFactory.decodeResource(resources,R.drawable.chaussures)
    private var sonMusique = Son(context,R.raw.musiquefond)
    private var sonBonus = Son(context,R.raw.bonus)
    private var sonSaut = Son(context,R.raw.jump)

    // VARIABLES D'ETAT (liées à l'évolution de la partie)
    private var score = 0 //setup du score joueur
    private var compteurMort = 0
    private lateinit var posJoueur:Array<Float> //position du joueur
    var x1 = 0F //abscisse du premier contact du doigt
    var x2 = 0F //abscisse du dernier contact du doigt
    var y1 = 0F //ordonnée du premier contact du doigt
    var y2 = 0F //ordonnée du dernier contact du doigt
    private var direction = 0 //direction de mouvement du doigt
    companion object {
        private var Cheight =0
        fun getCheight():Int{
            return Cheight
        }
        var actualScore=0
        private var Cwidth=0
        fun getCwidth():Int{
            return Cwidth
        }
        private var saut = 0F
        fun setSaut(x:Float){
            saut=x
        }
        fun getSaut():Float{
            return saut
        }
        var toBeRemoved = mutableListOf<Element>()
    }

    // fonction de dessin de l'application
    private fun draw(){
        if(holder.surface.isValid){
            canvas = holder.lockCanvas()
            canvas.drawRect(0F,0F,width.toFloat(),height.toFloat(),backgroundPaint) //réinitialise l'affichage
            //dessin de la première image (avec setup des variables nécessaires)
            if(!setup){
                setupVariables() //mise à jour des variables d'état
                drawObstacles() //appel de la fonction de dessin des obstacles
                drawPlayer() //appel de la fonction de dessin du joueur
                setup = true
            }
            if(joueur.alive && ready) tickGame() //continuer la partie si le joueur n'est pas mort
            else mort() //arrêt de la partie sinon

            holder.unlockCanvasAndPost(canvas)
            }
    }



    // fonction exécutée en cas de mort du joueur
    private fun mort() {
        //réinitialisation de la partie
        if (deadScreen) {
            joueur.deadSound.start() //production du son de mort
            obstacles.clear() //suppression des obstacles mémorisés
            decor.clear() //suppression du décor mémorisé
            observable.clear()
            deadScreen = false
            backgroundPaint.color = Color.RED
            textPaint.color = Color.BLACK
            ready = false
        }

        if(compteurMort==2) {
            drawObstacles()
            drawPlayer()
            backgroundPaint.color = Color.GREEN
        }
        textPaint.textSize = width/6F
        canvas.drawText(score.toString(),width/2F-width/18,height/2F-20,textPaint)
        textPaint.textSize = width/15F
        canvas.drawText("Cliquer sur l'écran pour rejouer",width/15F,height/2F+height/10,textPaint)
        compteurMort += 1
    }



    // fonction de setup des variables d'état
    private fun setupVariables(){
        //variables d'état
        Cheight = height
        Cwidth = width
        sonMusique.start()
        time = 200
        compteurMort = 0
        tailleJoueur = width/24F
        saut = tailleJoueur*2F
        reste = tailleJoueur*36%tailleJoueur
        startingPos = tailleJoueur*17

        //décor
        routeImage=Bitmap.createScaledBitmap(routeImage,width,tailleJoueur.toInt()*2,true)
        herbeImage=Bitmap.createScaledBitmap(herbeImage,width,tailleJoueur.toInt()*2,true)
    }

    private fun checkSound(){
        if(!sonMusique.playing()){
            sonMusique.start()
        }
    }

    // fonction de mise à jour de l'affichage
    private fun tickGame() {
        autoGen()
        checkSound()
        //déplacement des obstacles (verticalement selon l'avancement de l'écran et horizontalement selon leur type et leur vitesse
        observable.updateObs(canvas)
        for(obs in obstacles) {
            if(obs is Deplacable){
                obs.deplacement()
                //détection des collisions
                if(obs is CollisionMortelle) obs.collision(joueur,startingPos)
                if(obs is CollisionDisparition) obs.collision(joueur)
            }
        }

        //suppression des obstacles passés
        for(obs in toBeRemoved){
            obstacles.remove(obs)
            observable.remove(obs)
        }
        toBeRemoved.clear()

        joueur.detectSortieEcran() //détection de la sortie d'écran du joueur
        joueur.update(canvas) //mise à jour de la position du joueur

        //mise à jour du score
        if(actualScore>=score) score = actualScore
        drawText()
    }



    // fonction de mise à jour des textes (bonus et score)
    private fun drawText(){
        livesPaint.color=Color.WHITE
        livesPaint.textSize=width/20F
        canvas.drawText(joueur.lives.toString(),width/15F,height/20F,livesPaint)
        canvas.drawText(score.toString(),width-width/12F,height/20F,livesPaint)
    }



    // fonction de dessin des routes
    private fun drawRoute(posLow:Float){
        var larg = 0F
        var speed = 0F
        var image = caillouArbre
        val z = random.nextInt(maxVoitures)
        val vehiList = mutableListOf(1,4,7,10)

        //génération d'une ligne de route
        val route = Element(0F,posLow-2*tailleJoueur,width.toFloat(),tailleJoueur*2,routeImage)
        decor.add(route)
        observable.addObs(0,route)

        //Génération des véhicules
        val r = random.nextInt(3) //type du véhicule
        lateinit var obstacleTemp :Vehicule
        val dx = if(random.nextFloat()> 0.5) 1 else -1 //détermination d'une direction des véhicules pour la ligne

        //mise en place des véhicules
        for(j in 0..z) {
            val index = random.nextInt(vehiList.size)
            val location = vehiList[index]
            when (r) {
                //véhicule est une voiture
                0 -> {
                    speed = 7F
                    larg = 2F
                    //détermination couleur
                    when (random.nextInt(5)) {
                        0 -> image = voitureBleu
                        1 -> image = voitureGrise
                        2 -> image = voitureJaune
                        3 -> image = voitureOrange
                        4 -> image = voitureRouge
                    }
                }
                //véhicule est un camion
                1 -> {
                    speed = 5F
                    larg = 4F
                    //détermination de la couleur
                    val y = random.nextInt(2)
                    when (y) {
                        0 -> image = camionBleu
                        1 -> image = camionRouge
                    }
                }
                //véhicule est un bus scolaire
                2 -> {
                    speed = 4F
                    larg = 5F
                    image = busScolaire
                }
            }

            obstacleTemp = Vehicule(location*tailleJoueur*2,posLow-2*tailleJoueur, tailleJoueur*larg,tailleJoueur*2,speed*dx,image)

            obstacles.add(obstacleTemp)
            observable.addObs(obstacleTemp)
            vehiList.remove(location)

        }
    }



    // fonction de dessin des pelouses
    private fun drawHerbe(posLow:Float){
        val herbe = Element(0F,posLow-2*tailleJoueur,width.toFloat(),tailleJoueur*2,herbeImage) //génération d'une ligne d'herbe

        //Manipulation pour mettre la nouvelle herbe en première position
        decor.add(herbe)
        observable.addObs(0,herbe)

        //Génération des rochers
        val x = random.nextInt(maxCailloux)
        val list = mutableListOf(0,1,2,3,4,5,6,7,8,9,10,11)
        for (j in 0..x) {
            val index = random.nextInt(list.size)
            val location = list[index]
            list.remove(location)
            lateinit var obstacleTemp: Cailloux
            var image = caillouArbre
            val larg = 2F

            //Détermination du type de rocher
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
            observable.addObs(obstacleTemp)
        }
    }



    // fonction de génération automatique du terrain
    private fun autoGen(){
        //détermine la nécessité de charger de nouveau terrain en fonction du temps écoulé
        if(time*Element.vitesseCam+2>=tailleJoueur*2){
            //génération des bonus
            if(random.nextFloat()>0.95){
                lateinit var objTemp:Bonus
                if(random.nextFloat()>0.2) {
                    objTemp=BonusVie(width*random.nextFloat()-tailleJoueur*2,height*random.nextFloat()-tailleJoueur*2,tailleJoueur*3,tailleJoueur*3,
                    6*random.nextFloat(),6*random.nextFloat(),coeur,sonBonus)
                } else {
                    objTemp=BonusSaut(width*random.nextFloat()-tailleJoueur*2,height*random.nextFloat()-tailleJoueur*2,tailleJoueur*3,tailleJoueur*3,
                        6*random.nextFloat(),6*random.nextFloat(),chaussures,sonBonus)
                }
                obstacles.add(objTemp)
                observable.addObs(objTemp)
            }

            //détermination de la position de l'élément le plus haut sur l'écran pour positionner le suivant en fonction
            var lowestEl = decor[0]
            for(el in decor){
                for(i in 0..decor.size){
                    if(el.y1<lowestEl.y1){
                        lowestEl = el
                    }
                }
            }
            val posLow = lowestEl.y1
            time = 0

            //génération d'une nouvelle ligne (avec autant de chance que ce soit une route ou une pelouse)
            if(random.nextFloat()>0.5){
               drawHerbe(posLow)
            }else{
                drawRoute(posLow)
            }

            //suppression des éléments qui ont quitté l'écran
            lateinit var delItem : Element
            //suppression du décor
            var removable = false
            for(el in decor){
                if (el.y1 > height+tailleJoueur*2){
                    delItem = el
                    removable = true
                }
            }
            if (removable){
                decor.remove(delItem)
                observable.remove(delItem)
            }
            val delItems=ArrayList<Element>()
            //suppression des véhicules
            for(el in obstacles) if(el.y1 > height+tailleJoueur*2) delItems.add(el)
            for(el in delItems) {
                obstacles.remove(el)
                observable.remove(el)
            }

        }
        time += 1
    }



    // fonction de dessin des obstacles initiaux
    private fun drawObstacles(){
        //génération aléatoire d'obstacles
        for (i in 0..(height/tailleJoueur+3).toInt() step 4){
            //génération des lignes de terrain
            val herbe = Element(0F,(i+2)*tailleJoueur,width.toFloat(),tailleJoueur*2 ,herbeImage)
            decor.add(herbe)
            observable.addObs(0,herbe)
            val route = Element(0F,i*tailleJoueur,width.toFloat(),tailleJoueur*2,routeImage)
            decor.add(route)
            observable.addObs(0,route)
            drawTerrain(i)
            genCailloux(i)
        }
    }



    // fonction de dessin des routes initiales
    private fun drawTerrain(i:Int){
        val z = random.nextInt(maxVoitures)
        val r = random.nextInt(3)
        lateinit var obstacleTemp :Vehicule
        var larg = 0F
        var speed = 0F
        var path = 0
        val vehiList = mutableListOf(2,5,8,11)

        //Meme direction pour tous les véhicules de la meme ligne
        val dx = if(random.nextFloat()> 0.5) 1 else -1 //détermination de la direction des véhicules de la ligne

        //création des véhicules de la ligne
        for(j in 0..z){
            val index = random.nextInt(vehiList.size)
            val location = vehiList[index]
            vehiList.remove(location)
            when(r){
                //le véhicule est une voiture
                0 -> {
                    speed = 7F
                    larg = 2F

                    //détermination de la couleur de la voiture
                    val y = random.nextInt(5)
                    when(y){
                        0->path=R.drawable.voiture_bleu
                        1->path=R.drawable.voiture_grise
                        2->path=R.drawable.voiture_jaune
                        3->path=R.drawable.voiture_orange
                        4->path=R.drawable.voiture_rouge
                    }
                }

                //le véhicule est un camion
                1 -> {
                    speed = 5F
                    larg = 4F
                    //détermination de la couleur du camion
                    val y = random.nextInt(2)
                    when(y){
                        0->path=R.drawable.camion_bleu
                        1->path=R.drawable.camion_rouge
                    }
                }

                //le véhicule est un bus scolaire
                2 -> {
                    speed = 4F
                    larg = 5F
                    path = R.drawable.bus_scolaire
                }
            }
            obstacleTemp = Vehicule(location*tailleJoueur*2,i*tailleJoueur, tailleJoueur*larg,tailleJoueur*2,speed*dx,
                BitmapFactory.decodeResource(resources,path))
            obstacles.add(obstacleTemp)
            observable.addObs(obstacleTemp)
        }
    }



    // fonction de génération des cailloux initiaux
    private fun genCailloux(i:Int){
        val x = random.nextInt(maxCailloux)
        val caiList = mutableListOf(0,1,2,3,4,5,6,7,8,9,10,11)
        for (j in 0..x) {
            val index = random.nextInt(caiList.size)
            val location = caiList[index]
            caiList.remove(location)
            lateinit var obstacleTemp: Cailloux
            var path = 0
            val larg = 2F //largeur des rochers

            //détermination du type de rocher
            val y = random.nextInt(4)
            when (y) {
                0 -> path = R.drawable.caillou_arbre
                1 -> path = R.drawable.caillou_buisson
                2 -> path = R.drawable.caillou_fougere
                3 -> path = R.drawable.caillou_palmier
            }

            //création du rocher
            obstacleTemp = Cailloux(
                (location * tailleJoueur*2),
                (i+2) * tailleJoueur,
                tailleJoueur * larg,
                tailleJoueur * 2,
                BitmapFactory.decodeResource(resources, path)
            )
            obstacles.add(obstacleTemp)
            observable.addObs(obstacleTemp)
        }
    }



    // fonction de dessin du joueur
    private fun drawPlayer(){
        val deadSound = Son(context,R.raw.mort)

        //alignement du joueur et des obstacles
        posJoueur= arrayOf(width/12*7F-tailleJoueur,startingPos)
        joueur = Joueur((posJoueur[0]-tailleJoueur),(posJoueur[1]+tailleJoueur),tailleJoueur*2,
            tailleJoueur*2,width.toFloat(),height.toFloat(),tailleJoueur,deadSound,BitmapFactory.decodeResource(resources,R.drawable.avatar))
    }



    // fonction exécutée pour mettre le jeu en pause
    fun pause(){
        drawing = false
        sonMusique.stop()
        thread.join()
    }



    // fonction exécutée pour remettre le jeu en route
    fun resume(){
        drawing = true
        sonMusique.resume()
        thread=Thread(this)
        thread.start()
    }


    // fonction de remise en route du jeu après une mort
    fun revive(){
        ready = true
        deadScreen = true
        actualScore = 0
        score = 0
        time = 200
        compteurMort = 0
        cancelUp = true
        saut = tailleJoueur*2
    }



    // fonction exécutée lors des événements de toucher de l'écran
    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(e: MotionEvent): Boolean {
        when(e.action){
            //l'écran est touché
            MotionEvent.ACTION_DOWN -> {
                x1 = e.rawX; y1 = e.rawY //obtention des coordonnées du toucher
                if (compteurMort>2){
                    revive()
                }
            }

            //l'écran est relâché
            MotionEvent.ACTION_UP -> {
                if(!cancelUp) {
                    x2 = e.rawX; y2 = e.rawY //obtention des coordonnées du relâchement
                    sonSaut.start()
                    //détermination de la direction du glissement
                    if(abs(x2-x1)<5 && abs(y2-y1)<5) {
                        //mouvement insuffisant, simple click considéré, donc un mouvement vers le haut
                        joueur.y1 -= saut
                        direction = 0
                        actualScore+=(saut/tailleJoueur/2).toInt()
                    } else if(abs(x2-x1) > abs(y2-y1)) {
                        //mouvement horizontal, détermination de la direction horizontale
                        if(x2-x1 > 0){
                            //vers la droite
                            joueur.x1 += saut
                            direction = 3
                        } else {
                            //vers la gauche
                            joueur.x1 -= saut
                            direction = 2
                        }
                    } else {
                        //mouvement vertical, détermination de la direction verticale
                        if(y2-y1 > 0) {
                            //vers le bas
                            joueur.y1 += saut
                            direction = 1
                            actualScore -= 1
                        } else {
                            //vers le haut
                            joueur.y1 -= saut
                            direction = 0
                            actualScore += 1
                        }
                    }

                    //détermination des collisions
                    for(obs in obstacles){
                        if(obs is CollisionSimple) obs.collision(joueur,direction,saut)
                    }
                } else cancelUp = false
            }
        }
        return true
    }



    // fonctionnement continu cyclique de l'application
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