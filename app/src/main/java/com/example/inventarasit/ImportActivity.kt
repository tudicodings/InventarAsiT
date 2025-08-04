package com.example.inventarasit

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.io.BufferedReader
import java.io.InputStreamReader

class ImportActivity : AppCompatActivity() {

    private val PICK_CSV_FILE = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_import)

        val btnAlegeCSV = findViewById<Button>(R.id.btnAlegeCSV)

        btnAlegeCSV.setOnClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "text/*"
            intent.addCategory(Intent.CATEGORY_OPENABLE)
            startActivityForResult(Intent.createChooser(intent, "Selectează fișier CSV"), PICK_CSV_FILE)
        }

        //buton home
        val btnInapoiMeniu = findViewById<Button>(R.id.btnInapoiMeniu)
        btnInapoiMeniu.setOnClickListener {
            val intent = Intent(this, MainMenuActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
            startActivity(intent)
            finish()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_CSV_FILE && resultCode == Activity.RESULT_OK) {
            data?.data?.let { uri ->
                val produseImportate = citesteProduseDinCSV(uri)
                ListaProduseHolder.listaProduse = produseImportate

                Toast.makeText(this, "Importat ${produseImportate.size} produse!", Toast.LENGTH_LONG).show()

                // Navighează înapoi către MainActivity după import
                val intent = Intent(this, MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                startActivity(intent)
                finish()
            }
        }
    }

    private fun citesteProduseDinCSV(uri: Uri): List<Produs> {
        val produse = mutableListOf<Produs>()
        try {
            val inputStream = contentResolver.openInputStream(uri)
            val reader = BufferedReader(InputStreamReader(inputStream))

            reader.forEachLine { line ->
                val values = line.split(";")
                if (values.size >= 7) {
                    val codMarfa = values[0]
                    val nume = values[1]
                    val locatie = values[2]
                    val codBare = values[3]
                    val um = values[4]
                    val stocScan = values[5].toIntOrNull() ?: 0
                    val stocInit = values[6].toIntOrNull() ?: 0

                    produse.add(
                        Produs(
                            codMarfa = codMarfa,
                            nume = nume,
                            locatie = locatie,
                            codBare = codBare,
                            um = um,
                            stocScan = stocScan,
                            stocInit = stocInit
                        )
                    )
                }
            }

            reader.close()
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(this, "Eroare la citirea fișierului.", Toast.LENGTH_SHORT).show()
        }

        return produse
    }
}
