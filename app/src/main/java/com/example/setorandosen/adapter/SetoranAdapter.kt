package com.example.setorandosen.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.setorandosen.R
import com.example.setorandosen.model.Setoran

class SetoranAdapter(
    private val setoranList: List<Setoran>
) : RecyclerView.Adapter<SetoranAdapter.SetoranViewHolder>() {

    class SetoranViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvTanggal: TextView = itemView.findViewById(R.id.tvTanggal)
        val tvJenisSetoran: TextView = itemView.findViewById(R.id.tvJenisSetoran)
        val tvJumlahAyat: TextView = itemView.findViewById(R.id.tvJumlahAyat)
        val tvKeterangan: TextView = itemView.findViewById(R.id.tvKeterangan)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SetoranViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_setoran, parent, false)
        return SetoranViewHolder(view)
    }

    override fun onBindViewHolder(holder: SetoranViewHolder, position: Int) {
        val setoran = setoranList[position]

        holder.tvTanggal.text = setoran.tanggal
        holder.tvJenisSetoran.text = setoran.jenisSetoran
        holder.tvJumlahAyat.text = "${setoran.jumlahAyat} ayat"
        holder.tvKeterangan.text = setoran.keterangan ?: "Tidak ada keterangan"
    }

    override fun getItemCount(): Int = setoranList.size
}