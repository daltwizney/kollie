package com.wizneylabs.kollie.composables

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import com.wizneylabs.kollie.core.Canvas

@Composable
fun KollieCanvas() {
    AndroidView(
        factory = { context ->
            Canvas(context)
        },
        modifier = Modifier
            .fillMaxSize()
    )
}