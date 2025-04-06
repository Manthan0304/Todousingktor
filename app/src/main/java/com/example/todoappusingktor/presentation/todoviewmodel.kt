package com.example.todoappusingktor.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todoappusingktor.data.model.Todo
import com.example.todoappusingktor.data.todorepository
import com.example.todoappusingktor.result
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class todoviewmodel : ViewModel() {
    private val todorepository = todorepository()
    private val _state = MutableStateFlow<Screenstate>(Screenstate.loading)
    val state = _state.asStateFlow()

    init {
        viewModelScope.launch {
            todorepository.getalltodos().collectLatest {
                when (it) {
                    is result.error<*> -> _state.value = Screenstate.error(it.message)
                    result.loading -> _state.value = Screenstate.loading
                    is result.success<*> -> {
                        val todos = it.data
                        if (todos is List<*>) {
                            @Suppress("UNCHECKED_CAST")
                            _state.value = Screenstate.success(todos as List<Todo>)
                        } else {
                            _state.value = Screenstate.error("Invalid data format")
                        }
                    }

                }
            }
        }
    }

    fun deletetodo(id: Int) {
        viewModelScope.launch {
            todorepository.deletetodo(id).collectLatest {
                when (it) {
                    is result.error<*> -> {
                        _state.value = Screenstate.error(it.message)
                    }

                    result.loading -> {
                        _state.value = Screenstate.loading
                    }

                    is result.success<*> -> {
                        _state.value = Screenstate.success(it.data as List<Todo>)
                    }
                }
            }
        }
    }

    fun addtodo(todo: Todo) {
        viewModelScope.launch {
            todorepository.addtodo(todo).collectLatest {
                when (it) {
                    is result.error<*> -> {
                        _state.value = Screenstate.error(it.message)
                    }

                    result.loading -> {
                        _state.value = Screenstate.loading
                    }

                    is result.success<*> -> {
                        _state.value = Screenstate.success(it.data as List<Todo>)
                    }
                }
            }
        }
    }

    fun updatetodo(id:Int,todo: Todo) {
        viewModelScope.launch {
            todorepository.updatetodo(id,todo).collectLatest {
                when (it) {
                    is result.error<*> -> {
                        _state.value = Screenstate.error(it.message)
                    }

                    result.loading -> {
                        _state.value = Screenstate.loading
                    }

                    is result.success<*> -> {
                        _state.value = Screenstate.success(it.data as List<Todo>)
                    }
                }
            }
        }
    }
}

sealed class Screenstate {
    object loading : Screenstate()
    data class success(val todos: List<Todo>) : Screenstate()
    data class error(val message: String) : Screenstate()
}
