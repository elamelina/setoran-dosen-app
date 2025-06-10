package com.example.setorandosen.model

import com.google.gson.annotations.SerializedName

data class DetailSurat(

    val nama: String,
    val jumlah: Int,

    @SerializedName("external_id")
    val externalId: String,
    @SerializedName("nama_arab")
    val namaArab: String,
    val label: String,
    @SerializedName("sudah_setor")
    val sudahSetor: Boolean,
    @SerializedName("info_setoran")
    val infoSetoran: InfoSetoran?,

    @SerializedName("surat_id")
    val suratId: Int,

    @SerializedName("ayat")
    val ayat: String
)
