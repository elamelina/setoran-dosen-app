package com.example.setorandosen.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.setorandosen.R
import com.example.setorandosen.model.LogSetoran
import java.text.SimpleDateFormat
import java.util.*

class RiwayatLogAdapter(
    private val logList: List<LogSetoran>
) : RecyclerView.Adapter<RiwayatLogAdapter.RiwayatLogViewHolder>() {

    class RiwayatLogViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvTanggalRiwayat: TextView = itemView.findViewById(R.id.tvTanggalRiwayat)
        val tvStatusRiwayat: TextView = itemView.findViewById(R.id.tvStatusRiwayat)
        val tvNamaSuratRiwayat: TextView = itemView.findViewById(R.id.tvNamaSuratRiwayat)
        val tvKeteranganRiwayat: TextView = itemView.findViewById(R.id.tvKeteranganRiwayat)
        val tvWaktuRiwayat: TextView = itemView.findViewById(R.id.tvWaktuRiwayat)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RiwayatLogViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_riwayat, parent, false)
        return RiwayatLogViewHolder(view)
    }

    override fun onBindViewHolder(holder: RiwayatLogViewHolder, position: Int) {
        val log = logList[position]

        // Format tanggal dan waktu
        holder.tvTanggalRiwayat.text = formatDate(log.timestamp)
        holder.tvWaktuRiwayat.text = formatTime(log.timestamp)

        // Status (aksi)
        holder.tvStatusRiwayat.text = log.aksi.uppercase()

        // Nama surat (atau pengesah jika tidak ada namaSurat di model)
        holder.tvNamaSuratRiwayat.text = log.dosen_yang_mengesahkan.nama ?: "Tidak diketahui"

        // Keterangan
        if (log.keterangan.isNullOrBlank()) {
            holder.tvKeteranganRiwayat.visibility = View.GONE
        } else {
            holder.tvKeteranganRiwayat.visibility = View.VISIBLE
            holder.tvKeteranganRiwayat.text = "Keterangan: ${log.keterangan}"
        }

        // Set warna background status (harus punya drawable sesuai)
        when (log.aksi.uppercase()) {
            "VALIDASI" -> holder.tvStatusRiwayat.setBackgroundResource(R.drawable.bg_status_validasi)
            "HAPUS" -> holder.tvStatusRiwayat.setBackgroundResource(R.drawable.bg_status_hapus)
            "TAMBAH" -> holder.tvStatusRiwayat.setBackgroundResource(R.drawable.bg_status_tambah)
            else -> holder.tvStatusRiwayat.setBackgroundResource(R.drawable.bg_status_default)
        }
    }

    override fun getItemCount(): Int = logList.size

    private fun formatDate(dateTimeString: String): String {
        return try {
            val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
            inputFormat.timeZone = TimeZone.getTimeZone("UTC")
            val outputFormat = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
            val date = inputFormat.parse(dateTimeString)
            outputFormat.format(date ?: Date())
        } catch (e: Exception) {
            dateTimeString
        }
    }

    private fun formatTime(dateTimeString: String): String {
        return try {
            val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
            inputFormat.timeZone = TimeZone.getTimeZone("UTC")
            val outputFormat = SimpleDateFormat("HH:mm 'WIB'", Locale.getDefault())
            val date = inputFormat.parse(dateTimeString)
            outputFormat.format(date ?: Date())
        } catch (e: Exception) {
            ""
        }
    }
}
