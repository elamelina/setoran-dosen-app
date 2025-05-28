package com.example.setorandosen.api

import com.example.setorandosen.model.PaMahasiswaResponse
import com.example.setorandosen.model.MahasiswaSetoranResponse
import retrofit2.Response
import retrofit2.http.*

interface ApiService {
    @GET("api/pa-mahasiswa")
    suspend fun getPaMahasiswa(
        @Header("Authorization") token: String
    ): Response<PaMahasiswaResponse>

    @GET("api/setoran/{nim}")
    suspend fun getSetoranMahasiswa(
        @Header("Authorization") token: String,
        @Path("nim") nim: String
    ): Response<MahasiswaSetoranResponse>
}