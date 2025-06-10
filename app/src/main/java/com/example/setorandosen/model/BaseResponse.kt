package com.example.setorandosen.model

data class BaseResponse<T>(
    val status: Boolean,
    val message: String,
    val data: T? = null
)
