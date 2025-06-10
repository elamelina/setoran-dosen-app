package com.example.setorandosen.ui

import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.setorandosen.R
import com.example.setorandosen.adapter.SuratDipilihAdapter
import com.example.setorandosen.model.DataSetoranItem
import com.example.setorandosen.model.SimpanSetoranRequest
import com.example.setorandosen.api.RetrofitClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class TambahSetoranDialog(
    context: Context,
    private val nim: String,
    private val token: String,
    private val listSurat: List<DataSetoranItem>,
    private val onSuccess: () -> Unit
) : Dialog(context) {

    private lateinit var rvSuratPilihan: RecyclerView
    private lateinit var etTanggalMurojaah: EditText
    private lateinit var etKeterangan: EditText
    private lateinit var btnSimpan: Button

    private val calendar = Calendar.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val view = LayoutInflater.from(context).inflate(R.layout.dialog_tambah_setoran, null)
        setContentView(view)

        rvSuratPilihan = findViewById(R.id.rvSuratPilihan)
        etKeterangan = findViewById(R.id.etKeterangan)

        btnSimpan = Button(context).apply { text = "Simpan" }
        // Tambahkan tombol simpan ke layout jika memang belum ada di XML
        (view as? LinearLayout)?.addView(btnSimpan)

        setupRecyclerView()
        setupDatePicker()

        btnSimpan.setOnClickListener {
            simpanSetoran()
        }
    }

    private fun setupRecyclerView() {
        rvSuratPilihan.layoutManager = LinearLayoutManager(context)
        rvSuratPilihan.adapter = SuratDipilihAdapter(listSurat)
    }

    private fun setupDatePicker() {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        etTanggalMurojaah.setOnClickListener {
            DatePickerDialog(context, { _, year, month, day ->
                calendar.set(year, month, day)
                etTanggalMurojaah.setText(dateFormat.format(calendar.time))
            },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            ).show()
        }
    }

    private fun simpanSetoran() {
        if (listSurat.isEmpty()) {
            Toast.makeText(context, "Surat belum dipilih", Toast.LENGTH_SHORT).show()
            return
        }

        val tanggal = etTanggalMurojaah.text.toString()
        val keterangan = etKeterangan.text.toString()

        // Buat request dari listSurat yang sudah ada
        val request = SimpanSetoranRequest(
            dataSetoran = listSurat.map { surat ->
                DataSetoranItem(
                    namaKomponenSetoran = surat.namaKomponenSetoran,
                    idKomponenSetoran = surat.idKomponenSetoran
                )
            },
            tglSetoran = if (tanggal.isNotEmpty()) tanggal else null,
            keterangan = if (keterangan.isNotEmpty()) keterangan else null
        )

        CoroutineScope(Dispatchers.Main).launch {
            try {
                val response = RetrofitClient.apiService.simpanSetoran("Bearer $token", nim, request)
                if (response.isSuccessful && response.body()?.status == true) {
                    Toast.makeText(context, "Setoran berhasil disimpan", Toast.LENGTH_SHORT).show()
                    dismiss()
                    onSuccess()
                } else {
                    Toast.makeText(context, "Gagal simpan: ${response.body()?.message}", Toast.LENGTH_LONG).show()
                }
            } catch (e: Exception) {
                Toast.makeText(context, "Terjadi kesalahan: ${e.message}", Toast.LENGTH_LONG).show()
            }
        }
    }
}
