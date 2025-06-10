package com.example.setorandosen.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.setorandosen.R
import com.example.setorandosen.model.DataSetoranItem

class SuratDipilihAdapter(
    private val listSurat: List<DataSetoranItem>
) : RecyclerView.Adapter<SuratDipilihAdapter.SuratViewHolder>() {

    inner class SuratViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvNamaSurat: TextView = itemView.findViewById(R.id.tvNamaSurat)
        val tvIdSurat: TextView = itemView.findViewById(R.id.tvIdSurat)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SuratViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_surat_dipilih, parent, false)
        return SuratViewHolder(view)
    }

    override fun onBindViewHolder(holder: SuratViewHolder, position: Int) {
        val surat = listSurat[position]
        holder.tvNamaSurat.text = surat.namaKomponenSetoran
        holder.tvIdSurat.text = surat.idKomponenSetoran.toString()
    }

    override fun getItemCount(): Int = listSurat.size
}
