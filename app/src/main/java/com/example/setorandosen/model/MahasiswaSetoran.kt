package com.example.setorandosen.model
import java.util.Date

data class MahasiswaSetoran(
    val id: String,
    val nim: String,
    val nama_mahasiswa: String,
    val judul_setoran: String,
    val deskripsi: String?,
    val tanggal_setoran: String,
    val status: String,
    val feedback: String?,
    val created_at: String?,
    val updated_at: String?
)

data class MahasiswaSetoranResponse(
    val status: String,
    val message: String,
    val data: List<MahasiswaSetoran>
)