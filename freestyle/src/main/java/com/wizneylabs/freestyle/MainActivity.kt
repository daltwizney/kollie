package com.wizneylabs.freestyle

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.wizneylabs.freestyle.ui.theme.KollieTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            KollieTheme {

                val viewModel: FreestyleViewModel = viewModel();

                FreestyleEditor(viewModel);
            }
        }
    }
}

@Composable
fun FreestyleEditor(viewModel: FreestyleViewModel) {

    TextField(
        value = viewModel.text.value,
        onValueChange = { viewModel.onTextChanged(it) },
        modifier = Modifier
            .fillMaxSize(),
        textStyle = MaterialTheme.typography.bodyLarge,
        colors = TextFieldDefaults.colors()
    )
}