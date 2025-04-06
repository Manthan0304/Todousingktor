package com.example.todoappusingktor.data.model

import kotlinx.serialization.Serializable

@Serializable
data class Todo(
    val description: String,
    val id: Int? = null,
    val iscompleted: Boolean,
    val title: String
)