package mx.itson.cheems

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import mx.itson.cheems.entities.Winner
import mx.itson.cheems.persistence.WinnerFormActivity

class MainActivity : AppCompatActivity(), View.OnClickListener {

    var gameOverCard = 0
    var selectedCards = mutableListOf<Int>()
    var winCard = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        //Winner().save(this, "Pedro Robles Martínez", "pedrin")
        //Winner().getAll(this)

        val btnNewWinner = findViewById<View>(R.id.btn_new_winner) as Button
        btnNewWinner.setOnClickListener(this)

        start()
        Toast.makeText(this,"Bienvenido al juego", Toast.LENGTH_LONG).show()
        val btnReset = findViewById<Button>(R.id.btn_reset)
        btnReset.setOnClickListener {
            start()
        }

    }
    fun start(){
        // Ocultar TextView y Button al reiniciar // View.GONE = oculta el elemento y no ocupa espacio en pantalla
        findViewById<TextView>(R.id.txt_congratulations).visibility = View.GONE
        findViewById<Button>(R.id.btn_new_winner).visibility = View.GONE
        selectedCards.clear()
        for (i in 1..12) {
            val btnCard = findViewById<View>(
                resources.getIdentifier("card$i", "id", this.packageName)

            ) as ImageButton
            btnCard.setOnClickListener(this)
            btnCard.setBackgroundResource(R.drawable.cheems_question)
            btnCard.isEnabled = true
        }

        gameOverCard = (1..12).random()
        do {
            winCard = (1..12).random()
        } while (winCard == gameOverCard)

        Log.d("Valor de la carta perdedora", "La carta perdedora es ${gameOverCard.toString()}")
        Log.d("Valor de la carta ganadora", "La carta ganadora es ${winCard.toString()}")
    }

    //Función para que vibre el telefono
    fun vibrator(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            // Si la versión del sistema operativo instalado en el teléfono es igual o mayor a Android 12 (API 31)
            val vibratorAdmin = applicationContext.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
            val vibrator = vibratorAdmin.defaultVibrator
            vibrator.vibrate(VibrationEffect.createOneShot(1500, VibrationEffect.DEFAULT_AMPLITUDE))
        } else {
            val vibrator = applicationContext.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
            vibrator.vibrate(1500)
        }
    }


    fun revealAll(){
        for(i in 1..12){
            val btnCard = findViewById<View>(
                resources.getIdentifier("card$i", "id", this.packageName)
            ) as ImageButton

            if(i == gameOverCard){
                btnCard.setBackgroundResource(R.drawable.cheems_bad)
            } else if(i == winCard){
                btnCard.setBackgroundResource(R.drawable.cheems_master)
            } else {
                btnCard.setBackgroundResource(R.drawable.cheems_ok)
            }
            // Deshabilitar todas las cartas
            btnCard.isEnabled = false
        }
    }


    fun flip(card :Int){
        if(card == gameOverCard) {
            //Llamado de función vibrator cuando pierda
            vibrator()
            Toast.makeText(this,
                "¡Perdiste! jaja intenta otra vez",
                Toast.LENGTH_LONG).show()
            revealAll()

        } else{

            if(card == winCard){
                vibrator()
                Toast.makeText(this,
                    "Encontraste la carta CHEEMS-MASTER!!! GANASTE!!!",
                    Toast.LENGTH_LONG).show()
                revealAll()

                // Mostrar TextView y Button al ganar con carta CHEEMS-MASTER
                findViewById<TextView>(R.id.txt_congratulations).visibility = View.VISIBLE
                findViewById<Button>(R.id.btn_new_winner).visibility = View.VISIBLE

                return
            }

            // Condición para evitar que se agregue la misma carta dos veces en la lista
            if (!selectedCards.contains(card)) {
                selectedCards.add(card)
            }

            val btnCard = findViewById<View>(
                resources.getIdentifier("card$card", "id", this.packageName )
            ) as ImageButton
            btnCard.setBackgroundResource(R.drawable.cheems_ok)

            // Si ya seleccionó las 11 cartas buenas ganó
            if(selectedCards.size == 11){

                //Llamado de función vibrator cuando gane
                vibrator()
                Toast.makeText(this,
                    "¡GANASTE!",
                    Toast.LENGTH_LONG).show()
                revealAll()

                // Mostrar TextView y Button al ganar volteando las 11 cartas
                findViewById<TextView>(R.id.txt_congratulations).visibility = View.VISIBLE
                findViewById<Button>(R.id.btn_new_winner).visibility = View.VISIBLE
            }

        }
    }



    override fun onClick(v: View) {
        when(v.id) {
            R.id.card1 -> { flip(1)}
            R.id.card2 -> { flip(2)}
            R.id.card3 -> { flip(3)}
            R.id.card4 -> { flip(4)}
            R.id.card5 -> { flip(5)}
            R.id.card6 -> { flip(6)}
            R.id.card7 -> { flip(7)}
            R.id.card8 -> { flip(8)}
            R.id.card9 -> { flip(9)}
            R.id.card10 -> {flip(card = 10)}
            R.id.card11 -> {flip(card = 11)}
            R.id.card12 -> {flip(card = 12)}
            R.id.btn_new_winner -> {
                val intentWinnerForm = Intent(this, WinnerFormActivity::class.java)
                startActivity(intentWinnerForm)
            }

        }
    }







}