package com.wizneylabs.kollie.composables

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import com.google.ar.core.Session
import com.wizneylabs.kollie.core.Canvas

@Composable
fun KollieCanvas(arSession: Session) {
    AndroidView(
        factory = { context ->
            Canvas(context, arSession)
        },
        modifier = Modifier
            .fillMaxSize()
    )
}