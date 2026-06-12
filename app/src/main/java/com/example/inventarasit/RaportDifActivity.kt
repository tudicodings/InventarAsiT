package com.example.inventarasit

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class RaportDifActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ProdusRaportAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_raportdif)

        recyclerView = findViewById(R.id.recyclerViewRaportDif)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val produseRaport = ListaProduseHolder.listaProduse.filter { (it.stocScan).toInt() != 0 }
        adapter = ProdusRaportAdapter(produseRaport)
        recyclerView.adapter = adapter

        // buton scanare
        val btnScanare = findViewById<Button>(R.id.btnScanare)
        btnScanare.setOnClickListener {
            val intent =  Intent(this, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
            startActivity(intent)
            finish()
        }
    }
}