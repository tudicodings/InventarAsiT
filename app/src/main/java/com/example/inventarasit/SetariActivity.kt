package com.example.inventarasit

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class SetariActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setari)

        //buton verificare nescanate
        val btnProduseNescanate = findViewById<Button>(R.id.btnProduseNescanate)
        btnProduseNescanate.setOnClickListener {
            val intent = Intent(this, ProdNesActivity::class.java)
            startActivity(intent)
        }

        //buton home
        val btnInapoiMeniu = findViewById<Button>(R.id.btnInapoiMeniu)
        btnInapoiMeniu.setOnClickListener {
            val intent = Intent(this, MainMenuActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
            startActivity(intent)
            finish()
        }

        //buton verificare lot
        val btnVerificareLot = findViewById<Button>(R.id.btnVerificareLot)
        btnVerificareLot.setOnClickListener {
            val intent = Intent(this, VerifLotActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
            startActivity(intent)
        }

        //buton înregistrare greutăți
        val btnGreutateStc = findViewById<Button>(R.id.btnGreutateStc)
        btnGreutateStc.setOnClickListener {
            val intent = Intent(this, GreutateActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
            startActivity(intent)
        }
    }
}
