package com.example.inventarasit

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.File
import java.io.FileWriter

class ExportActivity : AppCompatActivity() {

    private lateinit var listaProduse: List<Produs>
    private lateinit var fisierCsv: File

    override fun onCreate(savedInstanceState: Bundle?) {
        ListaProduseHolder.isModified = false
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_export)

        val emailInput = findViewById<EditText>(R.id.emailInput)
        val btnTrimiteEmail = findViewById<Button>(R.id.btnTrimiteEmail)

        // incarcam lista cu produsele actualizate
        fun incarcaListaDinSharedPrefs(context: Context): List<Produs> {
            val prefs = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
            val json = prefs.getString("listaProduse", null)
            val gson = Gson()

            val type = object : TypeToken<List<Produs>>() {}.type
            return gson.fromJson(json, type) ?: emptyList()
        }

        listaProduse = ListaProduseHolder.listaProduse

        // generam CSV-ul (salvat în directorul aplicației)
        fisierCsv = genereazaFisierCSV(listaProduse)

        // 3. Când apeși pe buton, trimite CSV-ul pe mail
        btnTrimiteEmail.setOnClickListener {
            val email = emailInput.text.toString()
            if (email.isNotEmpty()) {
                trimiteEmailCuFisier(email, fisierCsv)
            } else {
                Toast.makeText(this, "Introdu o adresă de email!", Toast.LENGTH_SHORT).show()
            }
        }

        // buton home
        val btnInapoiMeniu = findViewById<Button>(R.id.btnInapoiMeniu)
        btnInapoiMeniu.setOnClickListener {
            val intent = Intent(this, MainMenuActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
            startActivity(intent)
            finish()
        }
    }

    private fun genereazaFisierCSV(lista: List<Produs>): File {
        val csvFile = File(getExternalFilesDir(null), "export_stocuri.csv")
        val writer = FileWriter(csvFile)

        // add coloana diferenta
        writer.append("\nCodMarfa;Nume;Locatie;CodBara;UM;Stoc Scanat;Cantitate Scriptica;Diferenta\n")

        for (produs in lista) {
            val diferenta = produs.stocInit - produs.stocScan
            //val stocNou = String.format(Locale("ro", "RO"), "%4f", produs.stocScan)
            writer.append("${produs.codMarfa};${produs.nume};${produs.locatie};${produs.codBare};${produs.um};${produs.stocScan};${produs.stocInit};$diferenta\n")
        }

        writer.flush()
        writer.close()
        return csvFile
    }

    //TODO: TREBUIE DE ADAUGAT UN EXPORT SPECIAL PENTRU LOTURI


    private fun trimiteEmailCuFisier(destinatar: String, fisier: File) {
        val uri: Uri = FileProvider.getUriForFile(
            this,
            applicationContext.packageName + ".provider",
            fisier
        )

        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "text/csv"
            putExtra(Intent.EXTRA_EMAIL, arrayOf(destinatar))
            putExtra(Intent.EXTRA_SUBJECT, "Export date")
            putExtra(Intent.EXTRA_TEXT, "Ai atașat fișierul CSV actualizat.")
            putExtra(Intent.EXTRA_STREAM, uri)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }

        startActivity(Intent.createChooser(intent, "Trimite email cu..."))
    }
}
