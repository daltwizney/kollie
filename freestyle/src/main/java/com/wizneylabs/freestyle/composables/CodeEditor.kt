package com.wizneylabs.freestyle.composables

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavHostController
import com.wizneylabs.freestyle.FreestyleEditorViewModel
import com.wizneylabs.freestyle.utils.SyntaxHighlightVisualTransformation

@Composable
fun CodeEditor(navController: NavHostController,
               viewModel: FreestyleEditorViewModel
) {
    val editorText = viewModel.editorText.collectAsState();

    if (!viewModel.isFullyLoaded.value)
    {
        AppLoadingScreen();
    }
    else
    {
        TextField(
            value = editorText.value,
            onValueChange = { viewModel.onEditorTextChanged(it) },
            modifier = Modifier
                .fillMaxSize(),
            textStyle = MaterialTheme.typography.bodyLarge,
            colors = TextFieldDefaults.colors(
                unfocusedContainerColor = Color(0xFF6482AD),
                focusedContainerColor = Color(0xFF6482AD),
                disabledContainerColor = Color(0xFF6482AD)
            ),
            visualTransformation = SyntaxHighlightVisualTransformation()
        )
    }
}
