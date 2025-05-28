package com.example.setorandosen.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.setorandosen.R
import com.example.setorandosen.model.PaMahasiswa

class PaMahasiswaAdapter(
    private val paMahasiswaList: List<PaMahasiswa>,
    private val onItemClick: (PaMahasiswa) -> Unit
) : RecyclerView.Adapter<PaMahasiswaAdapter.PaMahasiswaViewHolder>() {

    class PaMahasiswaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvNama: TextView = itemView.findViewById(R.id.tvNama)
        val tvNim: TextView = itemView.findViewById(R.id.tvNim)
        val tvProdi: TextView = itemView.findViewById(R.id.tvProdi)
        val tvTotalSetoran: TextView = itemView.findViewById(R.id.tvTotalSetoran)
        val tvLastSetoran: TextView = itemView.findViewById(R.id.tvLastSetoran)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PaMahasiswaViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_pa_mahasiswa, parent, false)
        return PaMahasiswaViewHolder(view)
    }

    override fun onBindViewHolder(holder: PaMahasiswaViewHolder, position: Int) {
        val mahasiswa = paMahasiswaList[position]

        holder.tvNama.text = mahasiswa.nama
        holder.tvNim.text = mahasiswa.nim
        holder.tvProdi.text = mahasiswa.prodi ?: "-"
        holder.tvTotalSetoran.text = "Total Setoran: ${mahasiswa.totalSetoran}"
        holder.tvLastSetoran.text = "Terakhir: ${mahasiswa.lastSetoran ?: "Belum ada"}"

        holder.itemView.setOnClickListener {
            onItemClick(mahasiswa)
        }
    }

    override fun getItemCount(): Int = paMahasiswaList.size
}