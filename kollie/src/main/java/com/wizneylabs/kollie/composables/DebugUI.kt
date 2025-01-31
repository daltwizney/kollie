package com.wizneylabs.kollie.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.unit.dp

@Composable
fun DebugUI(viewModel: com.wizneylabs.kollie.viewmodels.KollieAppViewModel) {

    Column(modifier = Modifier
        .offset(x = 0.dp, y = 20.dp)
        .background(Color.Black.copy(alpha = 0.7f))
        .padding(2.dp, 2.dp)
        .clearAndSetSemantics {  },
    ) {
        val fps = viewModel.fps.value;
        val frameTime = viewModel.frameTime.value * 1000;

        var fpsColor = Color.Red;

        if (fps > 58.0f)
        {
            fpsColor = Color.Green;
        }
        else if (fps > 50.0f)
        {
            fpsColor = Color.Yellow;
        }

        Text(
            text = "FPS: ${("%.1f").format(fps)}",
            modifier = Modifier,
            color = fpsColor,
        );

        Text(text = "dT: ${("%.1f").format(frameTime) }",
            modifier = Modifier,
            color = fpsColor);
    }
}