package com.wizneylabs.freestyle.composables

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

@Composable
fun DeleteShaderDialog(title: String, text: String, resultHandler: ((Boolean) -> Unit)) {

    var openDialog by remember { mutableStateOf(true) };

    if (openDialog)
    {
        AlertDialog(
            onDismissRequest = { openDialog = false },
            title = { Text(title) },
            text = { Text(text) },
            confirmButton = {
                TextButton(
                    onClick = {
                        resultHandler(true);
                    }
                ) {
                    Text("Delete");
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    resultHandler(false);
                }) {
                    Text("Cancel");
                }
            }
        )
    }
}
