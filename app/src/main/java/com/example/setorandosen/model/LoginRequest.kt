package com.example.setorandosen.model

data class LoginRequest(
    val grant_type: String = "password",
    val client_id: String = "setoran-mobile-dev",
    val client_secret: String= "aqJp3xnXKudgC7RMOshEQP7ZoVKWzoSl",
    val username: String,
    val password: String,
    val scope: String = "openid profile email"
)