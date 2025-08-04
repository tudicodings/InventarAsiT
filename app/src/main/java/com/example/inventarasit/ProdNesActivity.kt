package com.example.inventarasit

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class ProdNesActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ProdusNescanatAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_prod_nes)

        recyclerView = findViewById(R.id.recyclerViewNescanate)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val produseNescanate = ListaProduseHolder.listaProduse.filter { it.stocScan == 0}
        adapter = ProdusNescanatAdapter(produseNescanate)
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
