package com.example.setorandosen.model

data class LoginRequest(
    val grant_type: String = "password",
    val client_id: String = "setoran-dosen",
    val username: String,
    val password: String
)