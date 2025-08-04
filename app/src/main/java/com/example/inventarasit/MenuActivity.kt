package com.example.inventarasit

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainMenuActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_menu)

        val btnScanare = findViewById<Button>(R.id.btnScanare)
        val btnImport = findViewById<Button>(R.id.btnImport)
        val btnExport = findViewById<Button>(R.id.btnExport)
        val btnSetari = findViewById<Button>(R.id.btnSetari)

        // scanare
        btnScanare.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)  // activitatea de scanare
            startActivity(intent)
        }

        // export
        btnExport.setOnClickListener {
            val intent = Intent(this, ExportActivity::class.java) // activitatea de export
            startActivity(intent)
        }

        // import
        btnImport.setOnClickListener {
            val intent = Intent(this, ImportActivity::class.java) // activitatea de import
            startActivity(intent)
        }

        // setari
        btnSetari.setOnClickListener {
            val intent = Intent(this, SetariActivity::class.java) // activitatea de setari
            startActivity(intent)
        }
    }
}
