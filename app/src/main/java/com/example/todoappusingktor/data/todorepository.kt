package com.example.todoappusingktor.data

import com.example.todoappusingktor.data.client.ktorclient
import com.example.todoappusingktor.data.model.Todo
import com.example.todoappusingktor.result
import kotlinx.coroutines.flow.flow

class todorepository {
    val ktorclient = ktorclient()
    fun getalltodos() = flow {
        emit(result.loading)
        try {
            val todos = ktorclient.getalltodos()a
            emit(result.success(todos))
        } catch (e: Exception) {
            emit(result.error(e.message ?: "unknown error"))
        }
    }

    fun deletetodo(id: Int) = flow {
        try {
            val isdeleted = ktorclient.deletetodo(id)
            if (isdeleted) {
                val todos = ktorclient.getalltodos()
                emit(result.success(todos))
            }else{
                emit(result.error("failed to delete todo"))
            }
        } catch (e: Exception) {
            emit(result.error(e.message ?: "unknown error"))
        }
    }

    fun addtodo(todo: Todo) = flow {
        try {
            val isadded = ktorclient.addtodo(todo)
            if (isadded) {
                val todos = ktorclient.getalltodos()
                emit(result.success(todos))
            }else{
                emit(result.error("failed to add todo"))
            }
        } catch (e: Exception) {
            emit(result.error(e.message ?: "unknown error"))
        }
    }

    fun updatetodo(id: Int,todo: Todo) = flow {
        try {
            val isupdated = ktorclient.updatetodo(id,todo)
            if (isupdated) {
                val todos = ktorclient.getalltodos()
                emit(result.success(todos))
            }else{
                emit(result.error("failed to update todo"))
            }
        } catch (e: Exception) {
            emit(result.error(e.message ?: "unknown error"))
        }
    }
}