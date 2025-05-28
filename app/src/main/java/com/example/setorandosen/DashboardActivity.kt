package com.example.setorandosen

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ProgressBar
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
    private lateinit var sharedPreferencesHelper: SharedPreferencesHelper
    private lateinit var paMahasiswaAdapter: PaMahasiswaAdapter
    private val paMahasiswaList = mutableListOf<PaMahasiswa>()

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
        sharedPreferencesHelper = SharedPreferencesHelper(this)
    }

    private fun setupRecyclerView() {
        paMahasiswaAdapter = PaMahasiswaAdapter(paMahasiswaList) { mahasiswa ->
            // Handle click to view detail setoran mahasiswa
            val intent = Intent(this, DetailSetoranActivity::class.java)
            intent.putExtra("nim", mahasiswa.nim)
            intent.putExtra("nama", mahasiswa.nama)
            startActivity(intent)
        }
        rvPaMahasiswa.apply {
            layoutManager = LinearLayoutManager(this@DashboardActivity)
            adapter = paMahasiswaAdapter
        }
    }

    private fun loadPaMahasiswa() {
        val token = sharedPreferencesHelper.getToken()
        if (token == null) {
            Log.e(TAG, "Token is null, redirecting to login")
            logout()
            return
        }

        Log.d(TAG, "Loading PA Mahasiswa with token")
        showLoading(true)

        lifecycleScope.launch {
            try {
                val response = RetrofitClient.apiService.getPaMahasiswa("Bearer $token")

                Log.d(TAG, "Response code: ${response.code()}")
                Log.d(TAG, "Response message: ${response.message()}")

                if (response.isSuccessful && response.body() != null) {
                    val paMahasiswaResponse = response.body()!!
                    Log.d(TAG, "Received ${paMahasiswaResponse.data.size} PA mahasiswa")

                    paMahasiswaList.clear()
                    paMahasiswaList.addAll(paMahasiswaResponse.data)
                    paMahasiswaAdapter.notifyDataSetChanged()

                    if (paMahasiswaList.isEmpty()) {
                        Toast.makeText(this@DashboardActivity, "Tidak ada data PA mahasiswa", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    val errorBody = response.errorBody()?.string()
                    Log.e(TAG, "Failed to load PA mahasiswa - Code: ${response.code()}")
                    Log.e(TAG, "Error body: $errorBody")

                    when (response.code()) {
                        401 -> {
                            Toast.makeText(this@DashboardActivity, "Token expired, silakan login kembali", Toast.LENGTH_SHORT).show()
                            logout()
                        }
                        403 -> Toast.makeText(this@DashboardActivity, "Akses ditolak", Toast.LENGTH_SHORT).show()
                        404 -> Toast.makeText(this@DashboardActivity, "Endpoint tidak ditemukan", Toast.LENGTH_SHORT).show()
                        else -> Toast.makeText(this@DashboardActivity, "Gagal memuat data (${response.code()})", Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: Exception) {
                Log.e(TAG, "Exception while loading PA mahasiswa", e)
                val errorMessage = when {
                    e.message?.contains("timeout") == true -> "Koneksi timeout"
                    e.message?.contains("Unable to resolve host") == true -> "Tidak dapat terhubung ke server"
                    else -> "Error: ${e.message}"
                }
                Toast.makeText(this@DashboardActivity, errorMessage, Toast.LENGTH_LONG).show()
            } finally {
                showLoading(false)
            }
        }
    }

    private fun showLoading(show: Boolean) {
        progressBar.visibility = if (show) View.VISIBLE else View.GONE
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.dashboard_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_logout -> {
                logout()
                true
            }
            R.id.action_refresh -> {
                loadPaMahasiswa()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun logout() {
        sharedPreferencesHelper.clearAll()
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }
}