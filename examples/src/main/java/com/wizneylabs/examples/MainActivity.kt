package com.wizneylabs.examples

import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable

import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity

import androidx.compose.ui.unit.dp
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
                KollieApp()
            }
        }
    }
}

@Composable
fun KollieApp() {

    // TODO: this can be cleaned up more.  create a helper func to setup
    // the scene and viewmodel!

    val context = LocalContext.current;
    val configuration = LocalConfiguration.current;

    val screenWidthDp = configuration.screenWidthDp.dp;
    val screenHeightDp = configuration.screenHeightDp.dp;

    val screenWidth = (with (LocalDensity.current) { screenWidthDp.toPx() }).toInt();
    val screenHeight = (with (LocalDensity.current) { screenHeightDp.toPx() }).toInt();

    // TODO: this won't guarantee your scene persists across recompositions!
    // you need to have the viewmodel instantiate the demo scene!
    val demoScene = PathfinderDemoScene();

    val viewModel = viewModel<com.wizneylabs.kollie.viewmodels.KollieGameViewModel>(
        factory = com.wizneylabs.kollie.viewmodels
            .KollieGameViewModelFactory(demoScene,
                screenWidth, screenHeight
            )
    );

    KollieCanvas(viewModel);
}
