package com.example.inventarasit

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ProdusNescanatAdapter(private val produse: List<Produs>) :
    RecyclerView.Adapter<ProdusNescanatAdapter.ProdusViewHolder>() {

    class ProdusViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val codMarfaView: TextView = itemView.findViewById(R.id.txtCodMarfa)
        val numeView: TextView = itemView.findViewById(R.id.txtNume)
        val locatieView: TextView = itemView.findViewById(R.id.txtLocatie)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProdusViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_produs_nescanat, parent, false)
        return ProdusViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ProdusViewHolder, position: Int) {
        val produs = produse[position]
        holder.locatieView.text = "Locatie: ${produs.locatie}"
        holder.numeView.text = "Nume: ${produs.nume}"
        holder.codMarfaView.text = "Cod: ${produs.codMarfa}"
    }

    override fun getItemCount(): Int = produse.size
}
