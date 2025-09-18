package com.example.inventarasit

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
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
    private var produsCurent: Produs? = null



    @SuppressLint("UnspecifiedRegisterReceiverFlag", "SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?){
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
        registerReceiver(scanReceiver, filter)

        hiddenInput = findViewById(R.id.hiddenScanInput)
        hiddenInput.requestFocus()

        stocInput = findViewById<EditText>(R.id.stocInput)
        val btnUpdate = findViewById<Button>(R.id.salveazaStocBtn)

        btnUpdate.setOnClickListener {
            //val stocNou = stocInput.text.toString().toIntOrNull()
            val stocNou = stocInput.text.toString().replace(",", ".").toDoubleOrNull()

            if (stocNou != null && produsCurent != null) {
                produsCurent!!.stocScan = stocNou
                scanResultText.text = "${produsCurent!!.nume}: $stocNou buc."
                stocInput.text.clear()

                // info's produsCurent
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
                } else {
                    txtDetalii.text = "Produs negăsit pentru codul scanat."
                }

            } else {
                scanResultText.text = "Eroare: introduceți o valoare validă."
            }
            hiddenInput.requestFocus()
            stocInput.text.clear()
        }

        fun salveazaListaInSharedPrefs(context: Context, lista: List<Produs>) {
            val prefs = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
            val editor = prefs.edit()
            val gson = Gson()
            val json = gson.toJson(lista)
            editor.putString("listaProduse", json)
            editor.apply()
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
            hiddenInput.setText(data ?: "")

            produsCurent = listaProduse.find { it.codBare == data }

            // info's produsCurent
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
            } else {
                txtDetalii.text = "Produs negăsit pentru codul scanat."
            }

        }
    }

    override fun onDestroy(){
        super.onDestroy()
        unregisterReceiver(scanReceiver)
    }

    // citire din fisier Excel
    fun citesteProduseDinCSV(context: Context): List<Produs> {
        val produse = mutableListOf<Produs>()
        val inputStream = context.assets.open("inventar_test.csv")
        val reader = BufferedReader(InputStreamReader(inputStream))

        reader.forEachLine { line ->
            val values = line.split(";")
            if (values.size >= 7) {
                val codMarfa = values[0]
                val nume = values[1]
                val locatie = values[2]
                val codBare = values[3]
                val um = values[4]
                val stocScan = values[5].toDoubleOrNull() ?: 0.0
                val stocInit = values[6].toDoubleOrNull() ?: 0.0

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

        inputStream.close()
        return produse
    }


}