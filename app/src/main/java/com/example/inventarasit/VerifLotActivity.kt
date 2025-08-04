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
import com.example.inventarasit.ListaProduseHolder.listaProduse


class VerifLotActivity : AppCompatActivity() {

    private lateinit var txtDetaliiLot: TextView
    private lateinit var inputDataExpirare: EditText
    private lateinit var inputCantitate: EditText
    private lateinit var hiddenScanInput: EditText
    private lateinit var produsCurent: Produs

    @SuppressLint("UnspecifiedRegisterReceiverFlag", "SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_verifica_lot)

        txtDetaliiLot = findViewById(R.id.txtDetaliiLot)
        inputDataExpirare = findViewById(R.id.inputDataExpirare)
        inputCantitate = findViewById(R.id.inputCantitate)
        hiddenScanInput = findViewById(R.id.hiddenScanInput)
        hiddenScanInput.requestFocus()

        registerReceiver(scanReceiver, IntentFilter("com.example.inventarasit.SCAN"))

        findViewById<Button>(R.id.btnSalveazaLot).setOnClickListener {
            val dataNoua = inputDataExpirare.text.toString()
            val cantitate = inputCantitate.text.toString().toIntOrNull()

            @Suppress("SENSELESS_COMPARISON")
            if (produsCurent != null && dataNoua.isNotBlank() && cantitate != null) {
                txtDetaliiLot.text = "${produsCurent.nume} - ${produsCurent.locatie}\n" +
                        "Data adăugată: $dataNoua - Cantitate: $cantitate"
                inputDataExpirare.text.clear()
                inputCantitate.text.clear()
            } else {
                txtDetaliiLot.text = "Date incomplete sau produs negăsit."
            }

            hiddenScanInput.requestFocus()
        }
    }

    private val scanReceiver = object : BroadcastReceiver() {
        @SuppressLint("SetTextI18n")
        override fun onReceive(context: Context?, intent: Intent?) {
            val codScanat = intent?.getStringExtra("com.symbol.datawedge.data_string")
            hiddenScanInput.setText(codScanat ?: "")

            produsCurent = listaProduse.find { it.codBare == codScanat }!!

            @Suppress("SENSELESS_COMPARISON")
            if (produsCurent != null) {
                txtDetaliiLot.text = """
                    Nume: ${produsCurent.nume}
                    Locație: ${produsCurent.locatie}
                    Cod: ${produsCurent.codMarfa}
                    Introdu data expirare și cantitatea.
                """.trimIndent()
            } else {
                txtDetaliiLot.text = "Produs negăsit pentru codul: $codScanat"
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(scanReceiver)
    }
}
