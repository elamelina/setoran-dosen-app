package com.example.setorandosen.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.setorandosen.R
import com.example.setorandosen.model.DataSetoranItem
import com.example.setorandosen.model.DetailSurat

class SuratPilihanAdapter(
    private val listSurat: List<DetailSurat>) : RecyclerView.Adapter<SuratPilihanAdapter.ViewHolder>() {

    private val suratDipilih = mutableSetOf<DetailSurat>()

    fun getSuratDipilih(): List<DetailSurat> = suratDipilih.toList()

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val namaSurat: TextView = itemView.findViewById(R.id.tvNamaSurat)
        val checkbox: CheckBox = itemView.findViewById(R.id.checkboxSurat)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_surat_pilihan, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = listSurat.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val surat = listSurat[position]
        // Tampilkan nama surat dan nama arab misalnya
        holder.namaSurat.text = "${surat.nama} (${surat.namaArab})"

        // Clear listener dulu sebelum setChecked supaya tidak kebakar trigger ulang
        holder.checkbox.setOnCheckedChangeListener(null)
        holder.checkbox.isChecked = suratDipilih.contains(surat)

        holder.checkbox.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) suratDipilih.add(surat)
            else suratDipilih.remove(surat)
        }

        holder.itemView.setOnClickListener {
            val newChecked = !holder.checkbox.isChecked
            holder.checkbox.isChecked = newChecked
        }
    }

}
