package com.example.setorandosen.model

import com.google.gson.annotations.SerializedName

data class PaMahasiswaResponse(
    @SerializedName("response")
    val response: Boolean,
    val message: String,
    val data: DosenData?
)

data class DosenData(
    val nip: String,
    val nama: String,
    val email: String,
    @SerializedName("info_mahasiswa_pa")
    val infoMahasiswaPa: InfoMahasiswaPa?
)

data class InfoMahasiswaPa(
    val ringkasan: List<Ringkasan>?,
    @SerializedName("daftar_mahasiswa")
    val daftarMahasiswa: List<PaMahasiswa>?
)

data class Ringkasan(
    val tahun: String,
    val total: Int
)

data class PaMahasiswa(
    val email: String,
    val nim: String,
    val nama: String,
    val angkatan: String,
    val semester: Int,
    @SerializedName("info_setoran")
    val infoSetoran: InfoSetoran
)

data class InfoSetoran(
    @SerializedName("total_wajib_setor")
    val totalWajibSetor: Int,
    @SerializedName("total_sudah_setor")
    val totalSudahSetor: Int,
    @SerializedName("total_belum_setor")
    val totalBelumSetor: Int,
    @SerializedName("persentase_progres_setor")
    val persentaseProgresSetor: Double,
    @SerializedName("tgl_terakhir_setor")
    val tglTerakhirSetor: String?,
    @SerializedName("terakhir_setor")
    val terakhirSetor: String
)