package com.example.setorandosen.model

data class DetailSetoranResponse(
    val response: Boolean,
    val message: String,
    val data: DataDetailSetoran
)

data class DataDetailSetoran(
    val info: InfoMahasiswa,
    val setoran: Setoran
)

data class InfoMahasiswa(
    val nama: String,
    val nim: String,
    val email: String,
    val angkatan: String,
    val semester: Int,
    val dosen_pa: Dosen
)

data class Dosen(
    val nip: String,
    val nama: String,
    val email: String
)

data class Setoran(
    val log: List<LogSetoran>,
    val info_dasar: InfoDasarSetoran,
    val ringkasan: List<RingkasanSetoran>,
    val detail: List<DetailSurat>
)

data class LogSetoran(
    val id: Int,
    val keterangan: String?,
    val aksi: String,
    val ip: String,
    val user_agent: String,
    val timestamp: String,
    val nim: String,
    val dosen_yang_mengesahkan: Dosen
)

data class InfoDasarSetoran(
    val total_wajib_setor: Int,
    val total_sudah_setor: Int,
    val total_belum_setor: Int,
    val persentase_progres_setor: Double,
    val tgl_terakhir_setor: String,
    val terakhir_setor: String
)

data class RingkasanSetoran(
    val label: String,
    val total_wajib_setor: Int,
    val total_sudah_setor: Int,
    val total_belum_setor: Int,
    val persentase_progres_setor: Double
)
