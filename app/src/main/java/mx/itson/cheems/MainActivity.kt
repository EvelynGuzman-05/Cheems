package mx.itson.cheems

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity(), View.OnClickListener {

    var gameOverCard = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        start()

        val btnReset = findViewById<View>(R.id.btn_reset) as Button
        btnReset.setOnClickListener(this)
    }

    fun start() {

        for (i in 1..9) {

            val btnCard = findViewById<View>(
                resources.getIdentifier("card$i", "id", this.packageName)
            ) as ImageButton

            btnCard.setOnClickListener(this)
            btnCard.setBackgroundResource(R.drawable.cheems_question)
        }

        gameOverCard = (1..9).random()

        Log.d(
            "Valor de la carta perdedora",
            "La carta perdedora es ${gameOverCard.toString()}"
        )
    }

    fun flip(card: Int) {

        if (card == gameOverCard) {

            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                // Si la versión del sistema operativo instalado en el telefono es igual o mayor a Android 12 (API 31)
                val vibratorAdmin = applicationContext.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
                val vibrator = vibratorAdmin.defaultVibrator
                vibrator.vibrate(VibrationEffect.createOneShot(1500, VibrationEffect.DEFAULT_AMPLITUDE))
            } else{
                val vibrator = applicationContext.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
                vibrator.vibrate(1500)
            }

            Toast.makeText(
                this,
                "¡Perdiste! jaja intenta otra vez",
                Toast.LENGTH_LONG).show()

            for (i in 1..9) {

                val btnCard = findViewById<View>(
                    resources.getIdentifier("card$i", "id", this.packageName)
                ) as ImageButton

                if (i == gameOverCard) {
                    btnCard.setBackgroundResource(R.drawable.cheems_bad)
                } else {
                    btnCard.setBackgroundResource(R.drawable.cheems_ok)
                }
            }

        } else {

            val btnCard = findViewById<View>(
                resources.getIdentifier("card$card", "id", this.packageName)
            ) as ImageButton

            btnCard.setBackgroundResource(R.drawable.cheems_ok)
        }
    }

    override fun onClick(v: View?) {

        when (v?.id) {

            R.id.card1 -> flip(1)
            R.id.card2 -> flip(2)
            R.id.card3 -> flip(3)
            R.id.card4 -> flip(4)
            R.id.card5 -> flip(5)
            R.id.card6 -> flip(6)
            R.id.card7 -> flip(7)
            R.id.card8 -> flip(8)
            R.id.card9 -> flip(9)

            R.id.btn_reset -> start()
        }
    }
}
