package com.example.setorandosen

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ProgressBar
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.setorandosen.adapter.PaMahasiswaAdapter
import com.example.setorandosen.api.RetrofitClient
import com.example.setorandosen.model.PaMahasiswa
import com.example.setorandosen.utils.SharedPreferencesHelper
import kotlinx.coroutines.launch

class DashboardActivity : AppCompatActivity() {
    private lateinit var rvPaMahasiswa: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var spinnerAngkatan: Spinner
    private lateinit var sharedPreferencesHelper: SharedPreferencesHelper
    private lateinit var paMahasiswaAdapter: PaMahasiswaAdapter

    private val paMahasiswaList = mutableListOf<PaMahasiswa>()
    private var semuaMahasiswa: List<PaMahasiswa> = emptyList()

    companion object {
        private const val TAG = "DashboardActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        supportActionBar?.title = "Dashboard Dosen"

        initViews()
        setupRecyclerView()
        loadPaMahasiswa()
    }

    private fun initViews() {
        rvPaMahasiswa = findViewById(R.id.rvPaMahasiswa)
        progressBar = findViewById(R.id.progressBar)
        spinnerAngkatan = findViewById(R.id.spinnerAngkatan)
        sharedPreferencesHelper = SharedPreferencesHelper(this)
    }

    private fun setupRecyclerView() {
        paMahasiswaAdapter = PaMahasiswaAdapter(paMahasiswaList) { mhs ->
            startActivity(Intent(this, DetailSetoranActivity::class.java).apply {
                putExtra("nim", mhs.nim)
                putExtra("nama", mhs.nama)
            })
        }
        rvPaMahasiswa.layoutManager = LinearLayoutManager(this)
        rvPaMahasiswa.adapter = paMahasiswaAdapter
    }

    private fun setupSpinnerFilter(daftar: List<PaMahasiswa>) {
        val angkatanList = daftar.map { it.angkatan.toString() }.distinct().sorted()
        val options = listOf("Semua") + angkatanList

        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, options)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerAngkatan.adapter = adapter

        spinnerAngkatan.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>, view: View?, position: Int, id: Long
            ) {
                val selected = options[position]
                val filtered = if (selected == "Semua") semuaMahasiswa
                else semuaMahasiswa.filter { it.angkatan == selected }

                paMahasiswaList.clear()
                paMahasiswaList.addAll(filtered)
                paMahasiswaAdapter.notifyDataSetChanged()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }
    }

    private fun loadPaMahasiswa() {
        val token = sharedPreferencesHelper.getToken()
        if (token.isNullOrEmpty()) {
            logout(); return
        }
        showLoading(true)
        lifecycleScope.launch {
            try {
                val resp = RetrofitClient.apiService.getPaMahasiswa("Bearer $token")
                Log.d(TAG, "Response code: ${resp.code()}")

                if (resp.isSuccessful) {
                    val body = resp.body()
                    val daftar = body?.data?.infoMahasiswaPa?.daftarMahasiswa

                    if (!daftar.isNullOrEmpty()) {
                        Log.d(TAG, "Received ${daftar.size} PA mahasiswa")
                        paMahasiswaList.clear()
                        paMahasiswaList.addAll(daftar)
                        paMahasiswaAdapter.notifyDataSetChanged()

                        semuaMahasiswa = daftar
                        setupSpinnerFilter(daftar)
                    } else {
                        Toast.makeText(this@DashboardActivity,"Tidak ada data PA mahasiswa",Toast.LENGTH_SHORT).show()
                        Log.e(TAG, "items atau data null → daftarMahasiswa = $daftar")
                    }
                } else {
                    handleErrorCode(resp.code(), resp.errorBody()?.string())
                }
            } catch (e: Exception) {
                handleException(e)
            } finally {
                showLoading(false)
            }
        }
    }

    private fun handleErrorCode(code: Int, body: String?) {
        Log.e(TAG, "Failed load PA mahasiswa – Code: $code, body: $body")
        when (code) {
            401 -> { Toast.makeText(this, "Token expired, silakan login kembali", Toast.LENGTH_SHORT).show(); logout() }
            403 -> Toast.makeText(this, "Akses ditolak", Toast.LENGTH_SHORT).show()
            404 -> Toast.makeText(this, "Endpoint tidak ditemukan", Toast.LENGTH_SHORT).show()
            else -> Toast.makeText(this, "Gagal memuat data ($code)", Toast.LENGTH_SHORT).show()
        }
    }

    private fun handleException(e: Exception) {
        Log.e(TAG, "Exception", e)
        val msg = when {
            e.message?.contains("timeout", true) == true -> "Koneksi timeout"
            e.message?.contains("Unable to resolve host", true) == true -> "Tidak dapat terhubung ke server"
            else -> e.message
        }
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show()
    }

    private fun showLoading(show: Boolean) {
        progressBar.visibility = if (show) View.VISIBLE else View.GONE
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.dashboard_menu, menu); return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when (item.itemId) {
        R.id.action_logout -> { logout(); true }
        R.id.action_refresh -> { loadPaMahasiswa(); true }
        else -> super.onOptionsItemSelected(item)
    }

    private fun logout() {
        sharedPreferencesHelper.clearAll()
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }
}