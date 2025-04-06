package com.example.todoappusingktor

sealed class result<T> {
    data class success<T>(val data: T) : result<T>()
    data class error<T>(val message: String) : result<T>()
    object loading : result<Nothing>()
}