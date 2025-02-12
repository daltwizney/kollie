package com.wizneylabs.freestyle

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TextField

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.withStyle
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

class LanguageKeywordColorTransformation() : VisualTransformation {

    override fun filter(text: AnnotatedString): TransformedText {

        val textString = text.toString();

        return TransformedText(

            // TODO: this can be in a function...
            buildAnnotatedString {

                withStyle(SpanStyle(color = Color.Green)) {
                    appendLine(textString);
                }
                appendLine("==========");
                withStyle(SpanStyle(color = Color.Red)) {
                    append("Hello ");
                    appendLine("World");
                }
                appendLine("----")
                withStyle(SpanStyle(color = Color.Blue)) {
                    appendLine("it's about to go down");
                }            },

            OffsetMapping.Identity);
    }
}

@Composable
fun FreestyleEditor(viewModel: FreestyleViewModel) {

    val editorText = viewModel.editorText.collectAsState();

    TextField(
        value = editorText.value,
        onValueChange = { viewModel.onEditorTextChanged(it) },
        modifier = Modifier
            .fillMaxSize(),
        textStyle = MaterialTheme.typography.bodyLarge,
        visualTransformation = LanguageKeywordColorTransformation()
//        colors = TextFieldDefaults.colors()
    )
}