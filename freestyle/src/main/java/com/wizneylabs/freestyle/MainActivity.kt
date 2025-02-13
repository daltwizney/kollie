package com.wizneylabs.freestyle

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.wizneylabs.freestyle.ui.theme.KollieTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            KollieTheme {

                val viewModel: FreestyleViewModel = viewModel();

                FreeStyleApp(viewModel);
            }
        }
    }
}

@Composable
fun FreeStyleApp(viewModel: FreestyleViewModel) {

    val navController = rememberNavController();

    NavHost(
        navController = navController,
        startDestination = "home"
    ) {
        composable("home") {
            MainMenu(navController, viewModel);
        }
        composable("editor") {
            FreestyleEditor(navController, viewModel);
        }
    }
}

@Composable
fun MainMenu(navController: NavHostController, viewModel: FreestyleViewModel) {

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center) {

        Text("Item 1")
        Text("Item 2")
        Text("Item 3")

        Button(onClick = { navController.navigate("editor") }) {
            Text("Editor");
        }
    }
}

@Composable
fun FreestyleEditor(navController: NavHostController, viewModel: FreestyleViewModel) {

    val editorText = viewModel.editorText.collectAsState();

    TextField(
        value = editorText.value,
        onValueChange = { viewModel.onEditorTextChanged(it) },
        modifier = Modifier
            .fillMaxSize(),
        textStyle = MaterialTheme.typography.bodyLarge,
        colors = TextFieldDefaults.colors()
    )
}

// TODO: using this with a TextField composable causes crash...
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
                }
            },

            OffsetMapping.Identity
        );
    }
}