package com.wizneylabs.freestyle.composables

import androidx.compose.foundation.layout.Arrangement
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
import com.wizneylabs.freestyle.FreestyleEditorViewModel

@Composable
fun FreestyleEditorApp() {

    val viewModel: FreestyleEditorViewModel = viewModel();

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
fun MainMenu(navController: NavHostController, viewModel: FreestyleEditorViewModel) {

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center) {

        viewModel.shaderIDs.forEach { id ->

            Button(onClick = {

                viewModel.editShader(id);
                navController.navigate("editor");
            }) {
                Text(id);
            }
        }

//        Text("Item 1")
//        Text("Item 2")
//        Text("Item 3")
//
//        Button(onClick = { navController.navigate("editor") }) {
//            Text("Editor");
//        }
    }
}

@Composable
fun FreestyleEditor(navController: NavHostController, viewModel: FreestyleEditorViewModel) {

    val editorText = viewModel.editorText.collectAsState();

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

// TODO: using this with a TextField composable causes crash...
class SyntaxHighlightVisualTransformation() : VisualTransformation {

    private val keywords = mapOf(
        "in" to SpanStyle(color = Color.Blue),
        "vec2" to SpanStyle(color = Color.Green),
        "float" to SpanStyle(color = Color.Green),
        "return" to SpanStyle(color = Color.Magenta)
    );

    // styles for comments
    private val stringStyle = SpanStyle(color = Color.Yellow);
    private val commentStyle = SpanStyle(color = Color.Yellow);

    // comment regex patterns
    private val singleLineComment = Regex("""//.*$""", RegexOption.MULTILINE)
    private val multiLineComment = Regex("""/\*\*.*?\*/""", RegexOption.DOT_MATCHES_ALL)

    // base token regex to match words, whitespace, and special characters
    private val tokenPattern = Regex("""\w+|\s+|[-=+*/(){};,]|"[^"]*"""");

    private fun highlightSyntax(input: String): AnnotatedString {
        return buildAnnotatedString {
            var remainingText = input
            var lastProcessedIndex = 0

            // Process comments first (they take precedence)
            while (remainingText.isNotEmpty()) {
                val singleLineMatch = singleLineComment.find(remainingText)
                val multiLineMatch = multiLineComment.find(remainingText)

                // Find the earliest match
                val earliestMatch = when {
                    singleLineMatch == null -> multiLineMatch
                    multiLineMatch == null -> singleLineMatch
                    else -> if (singleLineMatch.range.first <= multiLineMatch.range.first)
                        singleLineMatch else multiLineMatch
                }

                if (earliestMatch == null) {
                    // No more comments, process remaining tokens
                    processTokens(remainingText, 0)
                    break
                }

                val matchStart = earliestMatch.range.first
                val matchEnd = earliestMatch.range.last + 1

                // Process tokens before the comment
                if (matchStart > 0) {
                    processTokens(remainingText.substring(0, matchStart), lastProcessedIndex)
                }

                // Append the comment with style
                withStyle(commentStyle) {
                    append(earliestMatch.value)
                }

                // Update remaining text and index
                lastProcessedIndex += matchEnd
                remainingText = if (matchEnd < remainingText.length) {
                    remainingText.substring(matchEnd)
                } else {
                    ""
                }
            }
        }
    }

    // Helper to process non-comment tokens
    private fun AnnotatedString.Builder.processTokens(text: String, offset: Int) {
        val matches = tokenPattern.findAll(text)
        var lastEnd = 0

        matches.forEach { match ->
            if (match.range.first > lastEnd) {
                append(text.substring(lastEnd, match.range.first))
            }

            val token = match.value
            when {
                token.startsWith("\"") -> withStyle(stringStyle) { append(token) }
                token in keywords -> withStyle(keywords[token]!!) { append(token) }
                else -> append(token)
            }

            lastEnd = match.range.last + 1
        }

        if (lastEnd < text.length) {
            append(text.substring(lastEnd))
        }
    }

    override fun filter(text: AnnotatedString): TransformedText {

        return TransformedText(
            text = highlightSyntax(text.text),
            offsetMapping = OffsetMapping.Identity
        )
    }
}