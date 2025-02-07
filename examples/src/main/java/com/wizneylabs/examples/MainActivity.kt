package com.wizneylabs.examples

import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity

import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.wizneylabs.examples.ui.theme.KollieTheme

import androidx.lifecycle.viewmodel.compose.viewModel
import com.wizneylabs.examples.scenes.PathfinderDemoScene

import com.wizneylabs.kollie.composables.KollieCanvas

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {

            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;

            KollieTheme {
                KollieTest()
            }
        }
    }
}

@Composable
fun KollieTest() {
    // TODO: make the composable with an AndroidView that wraps the
    // GLSurfaceView with a custom GLSurfaceView.Renderer!
    MyGLView();
}

@Composable
fun MyGLView() {
    AndroidView(
        factory = { context ->
            MyGLSurfaceView(context)
        },
        modifier = Modifier
            .fillMaxSize()
    )
}