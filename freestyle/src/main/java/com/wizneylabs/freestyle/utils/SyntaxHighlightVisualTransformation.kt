package com.wizneylabs.freestyle.utils

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.withStyle

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