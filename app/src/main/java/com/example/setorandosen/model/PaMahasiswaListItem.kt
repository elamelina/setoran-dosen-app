package com.example.setorandosen.model

sealed class PaMahasiswaListItem {
    data class Header(val angkatan: String) : PaMahasiswaListItem()
    data class Mahasiswa(val data: PaMahasiswa) : PaMahasiswaListItem()
}
