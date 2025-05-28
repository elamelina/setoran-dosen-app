package com.example.setorandosen

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.setorandosen.adapter.MahasiswaSetoranAdapter
import com.example.setorandosen.api.RetrofitClient
import com.example.setorandosen.model.MahasiswaSetoran
import com.example.setorandosen.utils.SharedPreferencesHelper
import kotlinx.coroutines.launch

class DetailSetoranActivity : AppCompatActivity() {
    private lateinit var tvNamaMahasiswa: TextView
    private lateinit var tvNimMahasiswa: TextView
    private lateinit var rvSetoran: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var sharedPreferencesHelper: SharedPreferencesHelper
    private lateinit var mahasiswaSetoranAdapter: MahasiswaSetoranAdapter
    private val setoranList = mutableListOf<MahasiswaSetoran>()

    private var nim: String = ""
    private var nama: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_setoran)

        // Enable back button
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // Get data from intent
        nim = intent.getStringExtra("nim") ?: ""
        nama = intent.getStringExtra("nama") ?: ""

        supportActionBar?.title = "Detail Setoran - $nama"

        initViews()
        setupRecyclerView()
        loadSetoranMahasiswa()
    }

    private fun initViews() {
        tvNamaMahasiswa = findViewById(R.id.tvNamaMahasiswa)
        tvNimMahasiswa = findViewById(R.id.tvNimMahasiswa)
        rvSetoran = findViewById(R.id.rvSetoran)
        progressBar = findViewById(R.id.progressBar)
        sharedPreferencesHelper = SharedPreferencesHelper(this)

        tvNamaMahasiswa.text = nama
        tvNimMahasiswa.text = nim
    }

    private fun setupRecyclerView() {
        mahasiswaSetoranAdapter = MahasiswaSetoranAdapter(setoranList)
        rvSetoran.apply {
            layoutManager = LinearLayoutManager(this@DetailSetoranActivity)
            adapter = mahasiswaSetoranAdapter
        }
    }

    private fun loadSetoranMahasiswa() {
        val token = sharedPreferencesHelper.getToken()
        if (token == null) {
            Toast.makeText(this, "Token tidak ditemukan", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        showLoading(true)

        lifecycleScope.launch {
            try {
                val response = RetrofitClient.apiService.getSetoranMahasiswa("Bearer $token", nim)

                if (response.isSuccessful && response.body() != null) {
                    val mahasiswaSetoranResponse = response.body()!!

                    setoranList.clear()
                    setoranList.addAll(mahasiswaSetoranResponse.data)
                    mahasiswaSetoranAdapter.notifyDataSetChanged()

                    if (setoranList.isEmpty()) {
                        Toast.makeText(this@DetailSetoranActivity, "Belum ada data setoran", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this@DetailSetoranActivity, "Gagal memuat data setoran", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(this@DetailSetoranActivity, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            } finally {
                showLoading(false)
            }
        }
    }

    private fun showLoading(show: Boolean) {
        progressBar.visibility = if (show) View.VISIBLE else View.GONE
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}