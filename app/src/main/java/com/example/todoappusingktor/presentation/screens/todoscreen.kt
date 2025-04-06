package com.example.todoappusingktor.presentation.screens

import androidx.compose.ui.window.Dialog
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ModifierLocalBeyondBoundsLayout
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.todoappusingktor.data.model.Todo
import com.example.todoappusingktor.presentation.Screenstate
import com.example.todoappusingktor.presentation.todoviewmodel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TodoScreen(viewModel: todoviewmodel) {
    val state = viewModel.state.collectAsStateWithLifecycle()
    val isDialogopen = remember { mutableStateOf(false) }
    val title = remember { mutableStateOf("") }
    val disp = remember { mutableStateOf("") }
    val iseditingid = remember { mutableStateOf(0) }

    when (state.value) {
        is Screenstate.error -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(text = (state.value as Screenstate.error).message)
            }
        }

        Screenstate.loading -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }

        is Screenstate.success -> {
            if (isDialogopen.value) {
                Dialog(onDismissRequest = {
                    title.value = ""
                    disp.value = ""
                    isDialogopen.value = false
                    iseditingid.value = 0
                }) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .padding(16.dp)
                                .fillMaxWidth()
                        ) {
                            Text(
                                text = if (iseditingid.value != 0) "Edit Todo" else "Add Todo",
                                style = MaterialTheme.typography.titleLarge
                            )
                            Spacer(Modifier.height(16.dp))
                            OutlinedTextField(
                                value = title.value,
                                onValueChange = { title.value = it },
                                label = { Text(text = "Title") },
                                modifier = Modifier.fillMaxWidth()
                            )
                            Spacer(Modifier.height(8.dp))
                            OutlinedTextField(
                                value = disp.value,
                                onValueChange = { disp.value = it },
                                label = { Text(text = "Description") },
                                modifier = Modifier.fillMaxWidth()
                            )
                            Spacer(Modifier.height(16.dp))
                            Button(
                                onClick = {
                                    if (iseditingid.value != 0) {
                                        val todo = Todo(
                                            description = disp.value,
                                            iscompleted = false,
                                            title = title.value
                                        )
                                        viewModel.updatetodo(iseditingid.value, todo)
                                    } else {
                                        val todo = Todo(
                                            description = disp.value,
                                            iscompleted = false,
                                            title = title.value
                                        )
                                        viewModel.addtodo(todo)
                                    }
                                    title.value = ""
                                    disp.value = ""
                                    isDialogopen.value = false
                                    iseditingid.value = 0
                                },
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(text = if (iseditingid.value != 0) "Update" else "Add")
                            }
                        }
                    }
                }
            }

            Scaffold(
                topBar = {
                    TopAppBar(
                        windowInsets = WindowInsets(0.dp),
                        title = { Text(text = "Todos") },
                        actions = {
                            IconButton(onClick = {
                                title.value = ""
                                disp.value = ""
                                isDialogopen.value = true
                                iseditingid.value = 0
                            }) {
                                Icon(imageVector = Icons.Rounded.Add, contentDescription = "Add Todo")
                            }
                        }
                    )
                }
            ) { paddingValues ->
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                ) {
                    items((state.value as Screenstate.success).todos) { todo ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 8.dp)
                        ) {
                            Row(
                                modifier = Modifier
                                    .padding(16.dp)
                                    .fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = todo.title,
                                        style = MaterialTheme.typography.titleMedium
                                    )
                                    Spacer(Modifier.height(4.dp))
                                    Text(
                                        text = todo.description,
                                        style = MaterialTheme.typography.bodyMedium
                                    )
                                    Spacer(Modifier.height(8.dp))
                                    Checkbox(
                                        checked = todo.iscompleted,
                                        onCheckedChange = { /* TODO: Implement completion toggle */ }
                                    )
                                }
                                IconButton(onClick = {
                                    title.value = todo.title
                                    disp.value = todo.description
                                    isDialogopen.value = true
                                    iseditingid.value = todo.id!!
                                }) {
                                    Icon(
                                        imageVector = Icons.Rounded.Edit,
                                        contentDescription = "Edit Todo"
                                    )
                                }
                                IconButton(onClick = { viewModel.deletetodo(id = todo.id!!) }) {
                                    Icon(
                                        imageVector = Icons.Rounded.Delete,
                                        contentDescription = "Delete Todo"
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}