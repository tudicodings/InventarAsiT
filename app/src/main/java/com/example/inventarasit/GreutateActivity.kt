package com.example.inventarasit

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.Bundle
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import java.io.BufferedReader
import java.io.InputStreamReader

class GreutateActivity : AppCompatActivity() {

    private lateinit var scanResultText: TextView
    public lateinit var listaProduse: List<Produs>
    companion object {
        lateinit var listaGlobalaProduse: List<Produs>
    }
    private lateinit var stocInput: EditText
    private lateinit var hiddenInput: EditText
    private lateinit var salveazaStocBtn: Button
    private var produsCurent: Produs? = null

    @SuppressLint("UnspecifiedRegisterReceiverFlag", "SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?){
        ListaProduseHolder.isModified = true
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_greutate)

        scanResultText = findViewById(R.id.scanResult)
        listaProduse = ListaProduseHolder.listaProduse.ifEmpty {
            citesteProduseDinCSV(this)
        }
        listaGlobalaProduse = listaProduse


        // asculta intenturile de la zebra
        val filter = IntentFilter()
        filter.addAction("com.example.inventarasit.SCAN") // la fel ca in DataWedge
        
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            registerReceiver(scanReceiver, filter, Context.RECEIVER_EXPORTED)
        } else {
            @Suppress("UnspecifiedRegisterReceiverFlag")
            registerReceiver(scanReceiver, filter)
        }

        hiddenInput = findViewById(R.id.hiddenScanInput)
        hiddenInput.requestFocus()
        
        // Permite introducerea manuală pentru telefoane normale
        hiddenInput.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_NEXT) {
                val data = hiddenInput.text.toString().trim()
                if (data.isNotEmpty()) {
                    cautaSiAfiseazaProdus(data)
                }
                true
            } else {
                false
            }
        }

        stocInput = findViewById<EditText>(R.id.stocInput)
        salveazaStocBtn = findViewById<Button>(R.id.salveazaStocBtn)

        salveazaStocBtn.setOnClickListener {
            val stocNou = stocInput.text.toString().replace(",", ".").toDoubleOrNull()

            if (stocNou != null && produsCurent != null) {
                produsCurent!!.stocScan = stocNou
                scanResultText.text = "${produsCurent!!.nume}: $stocNou buc."
                stocInput.text.clear()

                // Refresh detalii
                cautaSiAfiseazaProdus(produsCurent!!.codBare)

            } else {
                scanResultText.text = "Eroare: introduceți o valoare validă."
            }
            hiddenInput.requestFocus()
            stocInput.text.clear()
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

    private val scanReceiver: BroadcastReceiver = object : BroadcastReceiver(){
        @SuppressLint("SetTextI18n")
        override fun onReceive(context: Context?, intent: Intent?){
            val data = intent?.getStringExtra("com.symbol.datawedge.data_string")
            if (data != null) {
                hiddenInput.setText(data)
                cautaSiAfiseazaProdus(data)
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun cautaSiAfiseazaProdus(barcode: String) {
        produsCurent = listaProduse.find { it.codBare == barcode }
        val txtDetalii = findViewById<TextView>(R.id.txtDetaliiProdus)

        if (produsCurent != null) {
            val detalii = """
                Nume produs: ${produsCurent!!.nume}
                Cod marfă: ${produsCurent!!.codMarfa}
                Locație: ${produsCurent!!.locatie}
                Cod bare: ${produsCurent!!.codBare}
                UM: ${produsCurent!!.um}
                Stoc inițial: ${produsCurent!!.stocInit}
                Stoc scanat: ${produsCurent!!.stocScan}
            """.trimIndent()
            txtDetalii.text = detalii
            stocInput.requestFocus()
        } else {
            txtDetalii.text = "Produs negăsit pentru codul: $barcode"
        }
    }

    override fun onDestroy(){
        super.onDestroy()
        unregisterReceiver(scanReceiver)
    }

    // citire din fisier Excel
    fun citesteProduseDinCSV(context: Context): List<Produs> {
        val produse = mutableListOf<Produs>()
        try {
            val inputStream = context.assets.open("inventar_test.csv")
            val reader = BufferedReader(InputStreamReader(inputStream))

            reader.forEachLine { line ->
                val values = line.split(";")
                if (values.size >= 8) {
                    val codMarfa = values[0]
                    val nume = values[1]
                    val locatie = values[2]
                    val codBare = values[3]
                    val um = values[4]
                    val stocScan = values[5].toDoubleOrNull() ?: 0.0
                    val stocInit = values[6].toDoubleOrNull() ?: 0.0
                    val dataProd = values[7]

                    produse.add(
                        Produs(
                            codMarfa = codMarfa,
                            nume = nume,
                            locatie = locatie,
                            codBare = codBare,
                            um = um,
                            stocScan = stocScan,
                            stocInit = stocInit,
                            dataProdus = dataProd
                        )
                    )
                }
            }
            inputStream.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return produse
    }
}
