package com.example.todoappusingktor

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.todoappusingktor.presentation.screens.TodoScreen
import com.example.todoappusingktor.presentation.todoviewmodel
import com.example.todoappusingktor.ui.theme.TodoappusingktorTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TodoappusingktorTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    val viewmodel = viewModels<todoviewmodel>()
                    Column (modifier = Modifier.padding(innerPadding).fillMaxSize()){
                        TodoScreen(viewModel = viewmodel.value)
                    }
                }
            }
        }
    }
}
