package com.example.setorandosen.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.setorandosen.R
import com.example.setorandosen.model.PaMahasiswa
import com.example.setorandosen.model.PaMahasiswaListItem

class PaMahasiswaAdapter(
    private var paMahasiswaList: List<PaMahasiswa>,
    private val onItemClick: (PaMahasiswa) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val TYPE_HEADER = 0
        private const val TYPE_ITEM = 1
    }

    private var itemList: List<PaMahasiswaListItem> = generateGroupedList(paMahasiswaList)

    fun updateData(newList: List<PaMahasiswa>) {
        this.paMahasiswaList = newList
        this.itemList = generateGroupedList(newList)
        notifyDataSetChanged()
    }

    private fun generateGroupedList(list: List<PaMahasiswa>): List<PaMahasiswaListItem> {
        val grouped = list.groupBy { it.angkatan }.toSortedMap()
        val result = mutableListOf<PaMahasiswaListItem>()
        grouped.forEach { (angkatan, mahasiswaList) ->
            result.add(PaMahasiswaListItem.Header(angkatan))
            mahasiswaList.forEach { mhs ->
                result.add(PaMahasiswaListItem.Mahasiswa(mhs))
            }
        }
        return result
    }

    override fun getItemViewType(position: Int): Int = when (itemList[position]) {
        is PaMahasiswaListItem.Header -> TYPE_HEADER
        is PaMahasiswaListItem.Mahasiswa -> TYPE_ITEM
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return if (viewType == TYPE_HEADER) {
            val view = inflater.inflate(R.layout.item_header_angkatan, parent, false)
            HeaderViewHolder(view)
        } else {
            val view = inflater.inflate(R.layout.item_pa_mahasiswa, parent, false)
            MahasiswaViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (val item = itemList[position]) {
            is PaMahasiswaListItem.Header -> {
                (holder as HeaderViewHolder).tvHeader.text = "Angkatan ${item.angkatan}"
            }
            is PaMahasiswaListItem.Mahasiswa -> {
                val mhs = item.data
                val vh = holder as MahasiswaViewHolder
                vh.tvNama.text = mhs.nama
                vh.tvNim.text = mhs.nim
                vh.tvAngkatan.text = mhs.angkatan
                vh.tvTotalSetoran.text = "Total Setoran: ${mhs.infoSetoran.totalSudahSetor}"
                vh.tvLastSetoran.text = "Terakhir: ${mhs.infoSetoran.tglTerakhirSetor ?: "Belum ada"}"
                vh.progressBar.progress = mhs.infoSetoran.persentaseProgresSetor.toInt()

                vh.itemView.setOnClickListener {
                    onItemClick(mhs)
                }
            }
        }
    }

    override fun getItemCount(): Int = itemList.size

    class HeaderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvHeader: TextView = itemView.findViewById(R.id.tvHeaderAngkatan)
    }

    class MahasiswaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvNama: TextView = itemView.findViewById(R.id.tvNama)
        val tvNim: TextView = itemView.findViewById(R.id.tvNim)
        val tvAngkatan: TextView = itemView.findViewById(R.id.tvAngkatan)
        val tvTotalSetoran: TextView = itemView.findViewById(R.id.tvTotalSetoran)
        val tvLastSetoran: TextView = itemView.findViewById(R.id.tvLastSetoran)
        val progressBar: ProgressBar = itemView.findViewById(R.id.progressBarSetoran)
    }
}
