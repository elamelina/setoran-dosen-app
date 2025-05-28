package com.example.setorandosen.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.setorandosen.R
import com.example.setorandosen.model.MahasiswaSetoran
import java.text.SimpleDateFormat
import java.util.*

class MahasiswaSetoranAdapter(
    private val setoranList: List<MahasiswaSetoran>
) : RecyclerView.Adapter<MahasiswaSetoranAdapter.MahasiswaSetoranViewHolder>() {

    class MahasiswaSetoranViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvJudul: TextView = itemView.findViewById(R.id.tvJudul)
        val tvDeskripsi: TextView = itemView.findViewById(R.id.tvDeskripsi)
        val tvTanggal: TextView = itemView.findViewById(R.id.tvTanggal)
        val tvStatus: TextView = itemView.findViewById(R.id.tvStatus)
        val tvFeedback: TextView = itemView.findViewById(R.id.tvFeedback)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MahasiswaSetoranViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_mahasiswa_setoran, parent, false)
        return MahasiswaSetoranViewHolder(view)
    }

    override fun onBindViewHolder(holder: MahasiswaSetoranViewHolder, position: Int) {
        val setoran = setoranList[position]

        holder.tvJudul.text = setoran.judul_setoran
        holder.tvDeskripsi.text = setoran.deskripsi ?: "Tidak ada deskripsi"
        holder.tvTanggal.text = formatDate(setoran.tanggal_setoran)
        holder.tvStatus.text = setoran.status

        // Set status color
        when (setoran.status.lowercase()) {
            "pending" -> holder.tvStatus.setTextColor(holder.itemView.context.getColor(android.R.color.holo_orange_dark))
            "approved" -> holder.tvStatus.setTextColor(holder.itemView.context.getColor(android.R.color.holo_green_dark))
            "rejected" -> holder.tvStatus.setTextColor(holder.itemView.context.getColor(android.R.color.holo_red_dark))
        }

        // Show/hide feedback
        if (setoran.feedback.isNullOrEmpty()) {
            holder.tvFeedback.visibility = View.GONE
        } else {
            holder.tvFeedback.visibility = View.VISIBLE
            holder.tvFeedback.text = "Feedback: ${setoran.feedback}"
        }
    }

    override fun getItemCount(): Int = setoranList.size

    private fun formatDate(dateString: String): String {
        return try {
            val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val outputFormat = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
            val date = inputFormat.parse(dateString)
            outputFormat.format(date ?: Date())
        } catch (e: Exception) {
            dateString
        }
    }
}