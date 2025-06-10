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

    private lateinit var sharedPref: SharedPreferencesHelper

    private var nim: String? = null
    private var token: String? = null

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

        nim = intent.getStringExtra("nim")
        sharedPref = SharedPreferencesHelper(this)
        token = sharedPref.getToken()

        if (nim.isNullOrEmpty() || token.isNullOrEmpty()) {
            Toast.makeText(this, "Token atau NIM tidak valid", Toast.LENGTH_SHORT).show()
            finish()
            return
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
            onAddSetoran = { surat ->
                Toast.makeText(this, "Tambah setoran: ${surat.nama}", Toast.LENGTH_SHORT).show()
            }
        )
        rvDetailSetoran.layoutManager = LinearLayoutManager(this)
        rvDetailSetoran.adapter = adapter
    }
}
