package com.wizneylabs.kollie.composables

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.wizneylabs.kollie.core.Scene

@Composable
fun KollieApp(initialScene: Scene) {

    // TODO: this can be cleaned up more.  create a helper func to setup
    // the scene and viewmodel!

    val context = LocalContext.current;
    val configuration = LocalConfiguration.current;

    val screenWidthDp = configuration.screenWidthDp.dp;
    val screenHeightDp = configuration.screenHeightDp.dp;

    val screenWidth = (with (LocalDensity.current) { screenWidthDp.toPx() }).toInt();
    val screenHeight = (with (LocalDensity.current) { screenHeightDp.toPx() }).toInt();

    val viewModel = viewModel<com.wizneylabs.kollie.viewmodels.KollieAppViewModel>(
        factory = com.wizneylabs.kollie.viewmodels
            .KollieAppViewModelFactory(initialScene,
                screenWidth, screenHeight
            )
    );

    KollieCanvas(viewModel);
}