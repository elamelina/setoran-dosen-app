package com.example.setorandosen.ui

import android.app.DatePickerDialog
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.setorandosen.R
import com.example.setorandosen.adapter.DetailSetoranAdapter
import com.example.setorandosen.adapter.SuratPilihanAdapter
import com.example.setorandosen.api.RetrofitClient
import com.example.setorandosen.model.*
import com.example.setorandosen.utils.SharedPreferencesHelper
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class DetailSetoranActivity : AppCompatActivity() {

    private lateinit var rvDetailSetoran: RecyclerView
    private lateinit var adapter: DetailSetoranAdapter
    private lateinit var tvNamaMahasiswa: TextView
    private lateinit var tvNimMahasiswa: TextView
    private lateinit var tvEmail: TextView
    private lateinit var tvAngkatan: TextView
    private lateinit var tvSemester: TextView
    private lateinit var tvDosenPa: TextView
    private lateinit var btnTambah: Button

    private lateinit var sharedPref: SharedPreferencesHelper
    private lateinit var suratPilihanAdapter: SuratPilihanAdapter

    private var nim: String? = null
    private var token: String? = null
    private val suratPilihanList = mutableListOf<DetailSurat>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_setoran)

        rvDetailSetoran = findViewById(R.id.rvContent)
        tvNamaMahasiswa = findViewById(R.id.tvNamaMahasiswa)
        tvNimMahasiswa = findViewById(R.id.tvNimMahasiswa)
        tvEmail = findViewById(R.id.tvEmail)
        tvAngkatan = findViewById(R.id.tvAngkatan)
        tvSemester = findViewById(R.id.tvSemester)
        tvDosenPa = findViewById(R.id.tvDosenPa)
        btnTambah = findViewById(R.id.btnTambah)

        nim = intent.getStringExtra("nim")
        sharedPref = SharedPreferencesHelper(this)
        token = sharedPref.getToken()

        if (nim.isNullOrEmpty() || token.isNullOrEmpty()) {
            Toast.makeText(this, "Token atau NIM tidak valid", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        btnTambah.setOnClickListener {
            showTambahDialog()
        }

        getDetailSetoran(token!!, nim!!)
    }

    private fun getDetailSetoran(token: String, nim: String) {
        lifecycleScope.launch {
            try {
                val response = RetrofitClient.apiService.getDetailSetoranMahasiswa("Bearer $token", nim)
                if (response.isSuccessful) {
                    val detail = response.body()
                    if (detail != null && detail.response) {
                        val info = detail.data.info
                        tvNamaMahasiswa.text = info.nama
                        tvNimMahasiswa.text = "NIM: ${info.nim}"
                        tvEmail.text = "Email: ${info.email}"
                        tvAngkatan.text = "Angkatan ${info.angkatan}"
                        tvSemester.text = "Semester ${info.semester}"
                        tvDosenPa.text = "PA: ${info.dosen_pa.nama}"

                        setupRecyclerView(detail.data.setoran.detail, detail.data.setoran.log)
                    } else {
                        Toast.makeText(this@DetailSetoranActivity, "Data kosong", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this@DetailSetoranActivity, "Gagal fetch detail", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(this@DetailSetoranActivity, "Gagal koneksi: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setupRecyclerView(suratList: List<DetailSurat>, logList: List<LogSetoran>) {
        adapter = DetailSetoranAdapter(
            suratList,
            logList,
            onDeleteSetoran = { surat ->
                Toast.makeText(this, "Hapus setoran: ${surat.nama}", Toast.LENGTH_SHORT).show()
            },
            onValidateSetoran = { surat ->
                Toast.makeText(this, "Validasi setoran: ${surat.nama}", Toast.LENGTH_SHORT).show()
            }
        )
        rvDetailSetoran.layoutManager = LinearLayoutManager(this)
        rvDetailSetoran.adapter = adapter
    }

    private fun showTambahDialog() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_tambah_setoran, null)
        val etTanggal = dialogView.findViewById<EditText>(R.id.etTanggal)
        val etKeterangan = dialogView.findViewById<EditText>(R.id.etKeterangan)
        val rvSuratPilihan = dialogView.findViewById<RecyclerView>(R.id.rvSuratPilihan)

        suratPilihanAdapter = SuratPilihanAdapter(suratPilihanList)
        rvSuratPilihan.layoutManager = LinearLayoutManager(this)
        rvSuratPilihan.adapter = suratPilihanAdapter

        loadSuratPilihan()

        val calendar = Calendar.getInstance()
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        etTanggal.setOnClickListener {
            DatePickerDialog(this, { _, y, m, d ->
                calendar.set(y, m, d)
                etTanggal.setText(dateFormat.format(calendar.time))
            }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show()
        }

        MaterialAlertDialogBuilder(this)
            .setTitle("Tambah Setoran")
            .setView(dialogView)
            .setPositiveButton("Simpan") { _, _ ->
                val tgl = etTanggal.text.toString()
                val ket = etKeterangan.text.toString()
                val dipilihSurat = suratPilihanAdapter.getSuratDipilih()

                val dipilih: List<DataSetoranItem> = dipilihSurat.map {
                    DataSetoranItem(
                        idKomponenSetoran = it.suratId,              // Asumsikan ini cocok
                        namaKomponenSetoran = "${it.nama} (${it.ayat})"
                    )
                }



                if (dipilih.isEmpty()) {
                    Toast.makeText(this, "Pilih minimal 1 surat", Toast.LENGTH_SHORT).show()
                    return@setPositiveButton
                }

                val body = SimpanSetoranRequest(
                    dataSetoran = dipilih,
                    tglSetoran = if (tgl.isNotEmpty()) tgl else null,
                    keterangan = if (ket.isNotEmpty()) ket else null
                )

                postSetoran(nim!!, token!!, body)
            }
            .setNegativeButton("Batal", null)
            .show()
    }

    private fun loadSuratPilihan() {
        // Dummy data, bisa diganti ambil dari API
        suratPilihanList.clear()
        suratPilihanAdapter.notifyDataSetChanged()
    }

    private fun postSetoran(nim: String, token: String, data: SimpanSetoranRequest) {
        lifecycleScope.launch {
            try {
                val response = RetrofitClient.apiService.simpanSetoran("Bearer $token", nim, data)
                if (response.isSuccessful) {
                    Toast.makeText(this@DetailSetoranActivity, "Setoran berhasil ditambahkan", Toast.LENGTH_SHORT).show()
                    getDetailSetoran(token, nim)
                } else {
                    Toast.makeText(this@DetailSetoranActivity, "Gagal simpan: ${response.code()}", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(this@DetailSetoranActivity, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
