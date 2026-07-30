package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import com.example.ui.navigation.DermAINavGraph
import com.example.ui.theme.DermAITheme
import com.example.ui.viewmodel.SkinViewModel

class MainActivity : ComponentActivity() {
  private val viewModel: SkinViewModel by viewModels()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()
    setContent {
      DermAITheme {
        DermAINavGraph(viewModel = viewModel)
      }
    }
  }
}

