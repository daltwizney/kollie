package com.wizneylabs.freestyle

import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.withStyle
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class FreestyleViewModel: ViewModel() {

    private val _editorText = MutableStateFlow("");

    val editorText: StateFlow<String> = _editorText.asStateFlow();

    fun onEditorTextChanged(newText: String) {

        _editorText.value = newText;
    }
}