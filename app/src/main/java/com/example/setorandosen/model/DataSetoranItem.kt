package com.example.setorandosen.model

import com.google.gson.annotations.SerializedName

data class DataSetoranItem(
    @SerializedName("id_komponen_setoran")
    val idKomponenSetoran: Int,
    @SerializedName("nama_komponen_setoran")
    val namaKomponenSetoran: String,

    // opsional, kalau ada (seperti tanggal & keterangan)
    @SerializedName("tgl_setoran")
    val tglSetoran: String? = null,

    @SerializedName("keterangan")
    val keterangan: String? = null

)
