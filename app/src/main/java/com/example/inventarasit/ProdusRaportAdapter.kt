package com.example.inventarasit

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ProdusRaportAdapter(private val produse: List<Produs>) :
    RecyclerView.Adapter<ProdusRaportAdapter.ProdusViewHolder>() {

        class ProdusViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val codMarfaView: TextView = itemView.findViewById(R.id.txtCodMarfa)
            val numeView: TextView = itemView.findViewById(R.id.txtNume)
            val diferentaView: TextView = itemView.findViewById(R.id.txtDiferenta)
            val stiView: TextView = itemView.findViewById(R.id.txtStocInit)
            val stsView: TextView = itemView.findViewById(R.id.txtStocScan)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProdusViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_produs_raportdif, parent, false)
            return ProdusViewHolder(view)
        }

    /*@SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ProdusViewHolder, position: Int) {
        val produs = produse[position]
        holder.locatieView.text = "Locatie: ${produs.locatie}"
        holder.numeView.text = "Nume: ${produs.nume}"
        holder.codMarfaView.text = "Cod: ${produs.codMarfa}"
    }*/

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ProdusViewHolder, position: Int) {
        val produs = produse[position]
            holder.numeView.text = "Nume: ${produs.nume}"
            holder.codMarfaView.text = "Cod produs: ${produs.codMarfa}"
            holder.stiView.text = "Stoc scriptic: ${produs.stocInit}"
            holder.stsView.text = "Stoc faptic: ${produs.stocScan}"
            holder.diferentaView.text = "Diferenta: ${produs.stocInit - produs.stocScan}"
    }

        override fun getItemCount(): Int = produse.size
}