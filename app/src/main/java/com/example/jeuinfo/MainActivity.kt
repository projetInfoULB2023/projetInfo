package com.example.jeuinfo


// importations
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.util.DisplayMetrics
import androidx.appcompat.app.AppCompatActivity
import com.example.jeuinfo.databinding.ActivityMainBinding
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import kotlin.system.exitProcess



// Classe principale de l'application
class MainActivity : AppCompatActivity() {
    // VARIABLES DE CLASSE
    private lateinit var drawingView:DrawingView //obtention du code visuel de l'application
    var menuTest = 0 //variable d'état d'ouverture du menu "réglages"



    // METHODE OnCreate() (exécutée à l'activation de l'activity)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState) //appel de la méthode onCreate de la classe mère

        //jonction du layout avec l'activity
        val binding =ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //obtention des dimensions de l'appareil exécutant le code
        val displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetrics)
        //setup du layout de l'application
        drawingView = binding.vMain
        drawingView.setWillNotDraw(false)
        drawingView.invalidate()
    }


    // fonction exécutée à l'activation du menu "réglages"
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu,menu)
        return super.onCreateOptionsMenu(menu)
    }



    // fonction exécutée à l'ouverture du menu "réglages"
    override fun onMenuOpened(featureId: Int, menu: Menu): Boolean {
        drawingView.pause() // mise en pause du jeu
        return super.onMenuOpened(featureId, menu)
    }



    // fonction exécutée à la fermeture du menu "réglages" (ou un de ses sous-menus)
    override fun onPanelClosed(featureId: Int, menu: Menu) {
        if(menuTest == 0) drawingView.resume() // sortie de la pause si aucun bouton du menu "réglages" n'a été cliqué
        super.onPanelClosed(featureId, menu)
    }



    // fonction exécutée si un bouton du menu réglages est sélectionné
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            //bouton "about" sélectionné
            R.id.about -> {
                menuTest = 1 //interdiction de remettre le jeu en route par la méthode onPanelClosed()
                Toast.makeText(this, "About Selected", Toast.LENGTH_SHORT).show() //écriture d'un toast à l'écran indiquant le bouton sélectionné

                //initialisation d'une boîte de dialogue de type AlertDialog
                val builder = AlertDialog.Builder(this)
                builder.setTitle("About")
                builder.setMessage("An app brought to you by Poly polyp inc.")
                builder.apply { //mise en place du bouton retour sur la boîte de dialogue
                    setNeutralButton("return", DialogInterface.OnClickListener{ dialog, id ->
                        drawingView.resume()
                        menuTest = 0 //autorisation à remettre le jeu en route par la méthode onPanelClosed()
                    })
                }

                //affichage de la boîte de dialogue
                val alertDialog: AlertDialog = builder.create()
                alertDialog.show()
            }
            //bouton "settings" sélectionné
            R.id.settings -> {
                Toast.makeText(this, "Settings Selected", Toast.LENGTH_SHORT).show() //écriture d'un toast à l'écran indiquant le bouton sélectionné
            }
            //bouton "exit" sélectionné
            R.id.exit -> {
                Toast.makeText(this, "Exit Selected", Toast.LENGTH_SHORT).show() //écriture d'un toast à l'écran indiquant le bouton sélectionné
                exitProcess(0) //sortie propre de l'application
            }
        }
        return super.onOptionsItemSelected(item) //appel de la fonction de la classe mère et return de son résultat
    }



    // fonction exécutée lors d'une pause dans l'Activity
    override fun onPause() {
        super.onPause()
        drawingView.pause() //mise en pause du jeu
    }



    // fonction exécutée à la remise en route de l'Activity
    override fun onResume() {
        super.onResume()
        drawingView.resume() //remise en route du jeu
    }
}
