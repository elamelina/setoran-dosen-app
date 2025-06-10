package com.example.setorandosen.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.setorandosen.R
import com.example.setorandosen.model.DetailSurat
import com.example.setorandosen.model.LogSetoran

class DetailSetoranAdapter(
    private val suratList: List<DetailSurat>,
    private val logList: List<LogSetoran>,
    private val onDeleteSetoran: (DetailSurat) -> Unit,
    private val onValidateSetoran: (DetailSurat) -> Unit
) : RecyclerView.Adapter<DetailSetoranAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvNamaSurat: TextView = itemView.findViewById(R.id.tvNamaSurat)
        val tvStatusSurat: TextView = itemView.findViewById(R.id.tvStatusSurat)
        val tvTanggalSetor: TextView = itemView.findViewById(R.id.tvTanggalSetor)
        val tvKeterangan: TextView = itemView.findViewById(R.id.tvKeterangan)

        val layoutActions: View = itemView.findViewById(R.id.layoutActions)
        val btnHapusSetoran: Button = itemView.findViewById(R.id.btnHapusSetoran)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_detail_setoran, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val surat = suratList[position]

        // Nama surat dan label
        holder.tvNamaSurat.text = "${surat.nama} (${surat.label})"

        if (surat.sudahSetor) {
            // Jika sudah setor
            holder.tvStatusSurat.text = "Sudah Setor"
            holder.tvStatusSurat.setBackgroundResource(R.drawable.bg_status_setor)

            val info = surat.infoSetoran

            val tanggal = info?.tgl_setoran.orEmpty()
            if (tanggal.isNotEmpty()) {
                holder.tvTanggalSetor.text = "Disetor: $tanggal"
                holder.tvTanggalSetor.visibility = View.VISIBLE
            } else {
                holder.tvTanggalSetor.visibility = View.GONE
            }

            val latestLog = findLatestLogForSurat(surat.externalId, logList)
            val keterangan = latestLog?.keterangan.orEmpty()
            if (keterangan.isNotEmpty()) {
                holder.tvKeterangan.text = "Keterangan: $keterangan"
                holder.tvKeterangan.visibility = View.VISIBLE
            } else {
                holder.tvKeterangan.visibility = View.GONE
            }

            // Tombol aksi ditampilkan
            holder.layoutActions.visibility = View.VISIBLE
            holder.btnHapusSetoran.visibility = View.VISIBLE

            holder.btnHapusSetoran.setOnClickListener {
                suratList.getOrNull(holder.bindingAdapterPosition)?.let(onDeleteSetoran)
            }

        } else {
            // Jika belum setor
            holder.tvStatusSurat.text = "Belum Setor"
            holder.tvStatusSurat.setBackgroundResource(R.drawable.bg_status_belum_setor)

            holder.tvTanggalSetor.visibility = View.GONE
            holder.tvKeterangan.visibility = View.GONE

            // Tombol aksi disembunyikan
            holder.layoutActions.visibility = View.GONE
        }
    }

    override fun getItemCount(): Int = suratList.size

    private fun findLatestLogForSurat(suratId: String, logs: List<LogSetoran>): LogSetoran? {
        return logs.filter { it.nim == suratId }
            .maxByOrNull { it.timestamp }
    }
}
